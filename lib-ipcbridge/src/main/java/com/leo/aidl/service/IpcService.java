package com.leo.aidl.service;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCCache;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;

import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Create by MaZaizhong
 * on 2021/3/15
 * at 10:48 上午
 * in Baidu Company
 * 提供给service使用的帮助类
 */
public class IpcService {
    private static final Object LOCK = new Object();
    private IClientBridge mIClientBridge;
    private final IPCCache mIpcCache;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();
    private static volatile IpcService mIpcService;

    private IpcService() {
        mIpcCache = new IPCCache();
    }

    public static IpcService getInstance() {
        if (null == mIpcService) {
            synchronized (IpcService.class) {
                if (null == mIpcService) {
                    mIpcService = new IpcService();
                }
            }
        }
        return mIpcService;
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

    void setClientBridge(IClientBridge iClientBridge) {
        this.mIClientBridge = iClientBridge;
    }

    IPCResponse sendRequest(IPCRequest ipcRequest) {
        if (null != mIClientBridge) {
            try {
                return mIClientBridge.sendRequest(ipcRequest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T getClient(Class<T> inter) {
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
                        new ServiceInvocationHandler(name));
                mInvocationMap.put(name, t);
                return t;
            }
        }
    }
}
