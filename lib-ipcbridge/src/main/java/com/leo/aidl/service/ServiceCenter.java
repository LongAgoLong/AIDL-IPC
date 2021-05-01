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
public class ServiceCenter {
    private IClientBridge mIClientBridge;
    private final IPCCache mIpcCache;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();
    private static volatile ServiceCenter mServiceCenter;

    private ServiceCenter() {
        mIpcCache = new IPCCache();
    }

    public static ServiceCenter getInstance() {
        if (null == mServiceCenter) {
            synchronized (ServiceCenter.class) {
                if (null == mServiceCenter) {
                    mServiceCenter = new ServiceCenter();
                }
            }
        }
        return mServiceCenter;
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

    public void setClientBridge(IClientBridge iClientBridge) {
        this.mIClientBridge = iClientBridge;
    }

    public IPCResponse sendRequest(IPCRequest ipcRequest) {
        if (null != mIClientBridge) {
            try {
                return mIClientBridge.sendRequest(ipcRequest);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T get(Class<T> inter) {
        String name = inter.getName();
        if (null == mIClientBridge) {
            return null;
        }
        // 获取处理客户端请求的对象的Key，以此在服务端找出对应的处理者
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
