package com.leo.aidl.service;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCCache;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Create by MaZaizhong
 * on 2021/3/15
 * at 10:48 上午
 * in Baidu Company
 * 提供给service使用的帮助类
 */
public class ServiceManager {
    private IClientBridge mIClientBridge;
    private IPCCache mIpcCache;

    private ServiceManager() {
        mIpcCache = new IPCCache();
    }

    public static ServiceManager getInstance() {
        return SingleHold.SINGLE;
    }

    private static class SingleHold {
        public static final ServiceManager SINGLE = new ServiceManager();
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

    public Method getMethod(Class<?> clazz, String methodName) {
        return mIpcCache.getMethod(clazz, methodName);
    }

    public Object getObject(String className) {
        return mIpcCache.getObject(className);
    }

    public void putClientBridge(IClientBridge iClientBridge) {
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
        if (null == mIClientBridge) {
            return null;
        }
        // 获取处理客户端请求的对象的Key，以此在服务端找出对应的处理者
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{inter},
                new ServiceInvocationHandler(inter.getName()));
    }
}
