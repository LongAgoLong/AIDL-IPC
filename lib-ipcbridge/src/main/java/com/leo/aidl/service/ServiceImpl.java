package com.leo.aidl.service;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;
import com.leo.aidl.util.ParamsConvert;
import com.leo.lib_interface.client.IAttachSuccessListener;
import com.leo.lib_interface.provider.IBindSuccessListener;

import java.lang.reflect.Method;

public class ServiceImpl extends IService.Stub {
    @Override
    public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
        try {
            Class<?> aClass = ServiceCenter.getInstance().getClass(request.getInterfacesName());
            Object object = ServiceCenter.getInstance().getObject(aClass.getName());
            Method me = aClass.getMethod(request.getMethodName(),
                    ParamsConvert.getParameterTypes(request.getParameters()));

            Object[] params = ParamsConvert.unSerializationParams(request.getParameters());
            Object result = me.invoke(object, params);
            String r = ParamsConvert.mGson.toJson(result);
            return new IPCResponse(r, "执行方法成功", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void attach(IClientBridge iClientBridge) throws RemoteException {
        ServiceCenter.getInstance().setClientBridge(iClientBridge);
        // 通知客户端连接成功
        ServiceCenter.getInstance().get(IAttachSuccessListener.class).onInitSuccess();
        // 通知服务端连接成功
        Class<?> aClass = ServiceCenter.getInstance().getClass(IBindSuccessListener.class.getName());
        if (null != aClass) {
            IBindSuccessListener initListener = (IBindSuccessListener) ServiceCenter.getInstance()
                    .getObject(aClass.getName());
            initListener.onInitSuccess();
        }
    }
}
