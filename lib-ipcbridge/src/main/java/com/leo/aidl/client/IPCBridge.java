package com.leo.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.leo.aidl.IPCCache;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;

import java.lang.reflect.Proxy;
import java.util.HashMap;

public class IPCBridge {
    private static final String TAG = "IPCBridge";
    private static IPCBridge IPCBridge;

    private IService mIpcService;
    private IPCCache mIpcCache;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();

    private IPCBridge() {
        mIpcCache = new IPCCache();
    }

    public static IPCBridge getInstance() {
        if (null == IPCBridge) {
            synchronized (IPCBridge.class) {
                if (null == IPCBridge) {
                    IPCBridge = new IPCBridge();
                }
            }
        }
        return IPCBridge;
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "绑定成功");
            IService iService = IService.Stub.asInterface(service);
            try {
                iService.asBinder().linkToDeath(() -> {
                }, 0);
                iService.attach(new ClientImpl());
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

    public void init(Context context) {
        Intent intent = new Intent("com.leo.aidl");
        intent.setPackage("com.leo.aidl");
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (null == mIpcService) {
            return;
        }
        Log.i(TAG, "取消绑定");
        context.unbindService(connection);
    }

    public void register(Object object) {
        mIpcCache.register(object);
    }

    public void unRegister(Object object) {
        mIpcCache.unRegister(object);
    }

    public Class<?> getClass(String interfacesName) {
        return mIpcCache.getClass(interfacesName);
    }

    public Object getObject(String className) {
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

    public <T> T get(Class<T> inter) {
        String name = inter.getName();
        if (null == mIpcService) {
            return null;
        }
        // 获取处理客户端请求的对象的Key，以此在服务端找出对应的处理者
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
