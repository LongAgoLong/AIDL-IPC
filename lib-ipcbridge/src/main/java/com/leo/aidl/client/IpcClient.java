package com.leo.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.leo.aidl.IPCCache;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;
import com.leo.aidl.util.DeathRecipientImpl;
import com.leo.aidl.util.XLog;
import com.leo.lib_interface.client.IAttachStatusListener;

import java.lang.reflect.Proxy;
import java.util.HashMap;

public class IpcClient {
    private static final String TAG = "IPCBridge";
    private static volatile IpcClient mIpcClient;
    private static final Object LOCK = new Object();

    private IService mIpcService;
    private final IPCCache mIpcCache;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();
    private final Handler mUIHandler = new Handler(Looper.getMainLooper());
    private final ClientImpl clientImpl = new ClientImpl();

    private Context mContext;

    private IpcClient() {
        mIpcCache = new IPCCache();
    }

    public static IpcClient getInstance() {
        if (null == mIpcClient) {
            synchronized (IpcClient.class) {
                if (null == mIpcClient) {
                    mIpcClient = new IpcClient();
                }
            }
        }
        return mIpcClient;
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            XLog.i(TAG, "绑定成功");
            mUIHandler.removeCallbacksAndMessages(null);
            IService iService = IService.Stub.asInterface(service);
            DeathRecipientImpl deathRecipient = new DeathRecipientImpl(iService.asBinder()) {
                @Override
                public void binderDied() {
                    this.unbind();
                    XLog.e(TAG, "service died.");
                    Class<?> aClass = getInstance().getClass(IAttachStatusListener.class.getName());
                    if (null != aClass) {
                        IAttachStatusListener initListener = (IAttachStatusListener) getInstance()
                                .getObject(aClass.getName());
                        initListener.onInitStatus(false);
                    }
                    // 重新绑定操作
                    mIpcService = null;
                    rebind();
                }
            };
            deathRecipient.bind();
            try {
                iService.attach(clientImpl);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mIpcService = iService;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIpcService = null;
        }
    };

    private void rebind() {
        mUIHandler.removeCallbacksAndMessages(null);
        bind();
        mUIHandler.postDelayed(this::rebind, 3000);
    }

    private void bind() {
        XLog.i(TAG, "bind.");
        Intent intent = new Intent("com.leo.aidl");
        intent.setPackage("com.leo.aidl");
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void init(Context context) {
        mContext = context;
        rebind();
    }

    public void unbindService(Context context) {
        if (null == mIpcService) {
            return;
        }
        XLog.i(TAG, "取消绑定");
        context.unbindService(connection);
    }

    public void register(Object object) {
        mIpcCache.register(object);
    }

    public void unRegister(Object object) {
        mIpcCache.unRegister(object);
    }

    Class<?> getClass(String interfacesName) {
        return mIpcCache.getClass(interfacesName);
    }

    Object getObject(String className) {
        return mIpcCache.getObject(className);
    }

    public IPCResponse sendRequest(IPCRequest ipcRequest) {
        if (null != mIpcService) {
            try {
                return mIpcService.sendRequest(ipcRequest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T getService(Class<T> inter) {
        if (inter == null) {
            throw new RuntimeException("inter is null.");
        }
        if (!inter.isInterface()) {
            throw new RuntimeException("inter must be interface.");
        }
        String name = inter.getName();
        // 获取处理客户端请求的对象的Key，以此在服务端找出对应的处理者
        synchronized (LOCK) {
            if (mInvocationMap.containsKey(name)) {
                return (T) mInvocationMap.get(name);
            } else {
                T t = (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                        new Class[]{inter},
                        new ClientInvocationHandler(name));
                mInvocationMap.put(name, t);
                return t;
            }
        }
    }
}
