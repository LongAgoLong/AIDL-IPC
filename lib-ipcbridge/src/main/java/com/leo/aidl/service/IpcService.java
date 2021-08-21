package com.leo.aidl.service;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.util.IpcLog;
import com.leo.ipcbridge.BuildConfig;
import com.leo.protocol.BaseListener;
import com.leo.protocol.callback.IBindStatusListener;

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
    private final HashMap<String, Object> mInvocationMap = new HashMap<>();
    private static volatile IpcService mIpcService;
    /**
     * 连接状态监听
     */
    private IBindStatusListener mIBindStatusListener;
    private boolean mIsIpc;

    private IpcService() {
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

    public void init(Context context, boolean isIpc) {
        this.mIsIpc = isIpc;
        IpcLog.setLevel(BuildConfig.DEBUG ? Log.VERBOSE : Log.INFO);
        if (isIpc) {
            context.startService(new Intent(context, BridgeService.class));
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
     * 通知服务端连接状态
     *
     * @param isSuccess
     */
    private void notifyBindStatus(boolean isSuccess) {
        if (mIBindStatusListener != null) {
            mIBindStatusListener.onBindStatus(isSuccess);
        }
    }

    void setClientBridge(IClientBridge iClientBridge) {
        this.mIClientBridge = iClientBridge;
        notifyBindStatus(iClientBridge != null);
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

    /**
     * 是否与客户端建立通信
     *
     * @return
     */
    public boolean isClientAttach() {
        if (mIsIpc) {
            return mIClientBridge != null;
        }
        return true;
    }

    /**
     * 获取客户端的代理
     *
     * @param inter
     * @param <T>
     * @return
     */
    public <T extends BaseListener> T getClient(Class<T> inter) {
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
                        new ServiceInvocationHandler(name, mIsIpc));
                mInvocationMap.put(name, t);
                return t;
            }
        }
    }
}
