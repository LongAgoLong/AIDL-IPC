package com.leo.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;
import com.leo.aidl.util.DeathRecipientImpl;
import com.leo.aidl.util.IpcLog;
import com.leo.ipcbridge.BuildConfig;
import com.leo.protocol.BaseListener;
import com.leo.protocol.callback.IBindStatusListener;

import java.lang.ref.SoftReference;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class IpcClient {
    private static final String TAG = "IPCBridge";
    private static volatile IpcClient mIpcClient;
    private static final Object LOCK = new Object();

    private IService mIpcService;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();
    private final Handler mUIHandler = new Handler(Looper.getMainLooper());
    private final ClientImpl clientImpl = new ClientImpl();

    /**
     * 连接状态监听
     */
    private IBindStatusListener mIBindStatusListener;

    private SoftReference<Context> mContextRef;
    private boolean mIsIpc;
    private String mTargetPkgName;

    private IpcClient() {
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
            IpcLog.i(TAG, "bind service success");
            mUIHandler.removeCallbacksAndMessages(null);
            IService iService = IService.Stub.asInterface(service);
            DeathRecipientImpl deathRecipient = new DeathRecipientImpl(iService.asBinder()) {
                @Override
                public void binderDied() {
                    this.unbind();
                    IpcLog.e(TAG, "service died.");
                    notifyBindStatus(false);
                    // 重新绑定操作
                    mIpcService = null;
                    rebind();
                }
            };
            deathRecipient.bind();
            try {
                iService.attach(clientImpl);
                notifyBindStatus(true);
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
        IpcLog.i(TAG, "bind.");
        Intent intent = new Intent("com.leo.aidl");
        ComponentName componentName = new ComponentName(mTargetPkgName,
                "com.leo.aidl.service.BridgeService");
        intent.setComponent(componentName);
        intent.setPackage(mTargetPkgName);
        Context context = mContextRef.get();
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void init(Context context) {
        init(context, "", false);
    }

    public void init(Context context, String pkgName, boolean isIpc) {
        if (isIpc && TextUtils.isEmpty(pkgName)) {
            throw new RuntimeException("The parameter 'pkgName' cannot be empty when isIpc is true.");
        }
        this.mContextRef = new SoftReference<>(context);
        this.mIsIpc = isIpc;
        this.mTargetPkgName = pkgName;
        IpcLog.setLevel(BuildConfig.DEBUG ? Log.VERBOSE : Log.INFO);
        if (isIpc) {
            rebind();
        } else {
            notifyBindStatus(true);
        }
    }

    /**
     * 设置连接状态监听
     *
     * @param mIBindStatusListener
     */
    public void setIBindStatusListener(IBindStatusListener mIBindStatusListener) {
        this.mIBindStatusListener = mIBindStatusListener;
    }

    /**
     * 通知客户端绑定状态
     *
     * @param isSuccess
     */
    private void notifyBindStatus(boolean isSuccess) {
        if (mIBindStatusListener != null) {
            mIBindStatusListener.onBindStatus(isSuccess);
        }
    }

    public void unbindService(Context context) {
        if (null == mIpcService) {
            return;
        }
        IpcLog.i(TAG, "unbind");
        context.unbindService(connection);
    }

    IPCResponse sendRequest(IPCRequest ipcRequest) {
        if (null != mIpcService) {
            try {
                return mIpcService.sendRequest(ipcRequest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T extends BaseListener> T getService(Class<T> inter) {
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
                        new ClientInvocationHandler(name, mIsIpc));
                mInvocationMap.put(name, t);
                return t;
            }
        }
    }
}
