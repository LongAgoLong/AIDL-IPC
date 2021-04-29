package com.leo.aidl.client;

import android.os.RemoteException;

import com.leo.aidl.IPCCache;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Create by MaZaizhong
 * on 2021/3/15
 * at 10:47 上午
 * in Baidu Company
 * 提供给client使用的帮助类
 */
public class ClientManager {
    private IService mIpcService;
    private IPCCache mIpcCache;
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();

    private ClientManager() {
        mIpcCache = new IPCCache();
    }

    public static ClientManager getInstance() {
        return SingleHold.SINGLE;
    }

    private static class SingleHold {
        public static final ClientManager SINGLE = new ClientManager();
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

    public void putIpcService(IService ipcService) {
        this.mIpcService = ipcService;
    }

    public IService getIpcService() {
        return mIpcService;
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
