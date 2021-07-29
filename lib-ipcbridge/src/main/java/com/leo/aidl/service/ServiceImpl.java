package com.leo.aidl.service;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;
import com.leo.aidl.util.DeathRecipientImpl;
import com.leo.aidl.util.GsonHelper;
import com.leo.aidl.util.IpcConvert;
import com.leo.aidl.util.IpcLog;
import com.leo.lib_interface.client.IAttachStatusListener;
import com.leo.lib_interface.provider.IBindStatusListener;

import java.lang.reflect.Method;

public class ServiceImpl extends IService.Stub {
    private static final String TAG = "ServiceImpl";

    @Override
    public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
        try {
            Class<?> aClass = IpcService.getInstance().getClass(request.getInterfacesName());
            if (aClass == null) {
                IpcLog.e(TAG, "The implementation class was not found.[" + request.getInterfacesName() + "]");
                return new IPCResponse("", false);
            }
            Object object = IpcService.getInstance().getObject(aClass.getName());
            Method me = aClass.getMethod(request.getMethodName(),
                    IpcConvert.getParameterTypes(request.getParameters()));
            if (me == null) {
                IpcLog.e(TAG, "The method was not found.[" + request.getMethodName() + "]");
                return new IPCResponse("", false);
            }
            Object[] params = IpcConvert.unSerializationParams(request.getParameters());
            Object result = me.invoke(object, params);
            String r = GsonHelper.toJson(result);
            return new IPCResponse(r, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IPCResponse("", false);
    }

    @Override
    public void attach(IClientBridge iClientBridge) throws RemoteException {
        DeathRecipientImpl deathRecipient = new DeathRecipientImpl(iClientBridge.asBinder()) {
            @Override
            public void binderDied() {
                this.unbind();
                // 客户端挂了
                IpcLog.e(TAG, "client died.");
                IpcService.getInstance().setClientBridge(null);
                // 通知服务端连接断开
                notifyBindStatus(false);
            }
        };
        deathRecipient.bind();
        IpcService.getInstance().setClientBridge(iClientBridge);
        // 通知客户端连接成功
        IpcService.getInstance().getClient(IAttachStatusListener.class).onInitStatus(true);
        // 通知服务端连接成功
        notifyBindStatus(true);
    }

    /**
     * 通知服务端连接状态
     *
     * @param isSuccess
     */
    private void notifyBindStatus(boolean isSuccess) {
        Class<?> aClass = IpcService.getInstance().getClass(IBindStatusListener.class.getName());
        if (null != aClass) {
            IBindStatusListener initListener = (IBindStatusListener) IpcService.getInstance()
                    .getObject(aClass.getName());
            initListener.onBindStatus(isSuccess);
        }
    }
}
