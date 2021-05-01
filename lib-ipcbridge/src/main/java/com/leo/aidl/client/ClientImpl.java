package com.leo.aidl.client;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.util.ParamsConvert;

import java.lang.reflect.Method;

public class ClientImpl extends IClientBridge.Stub {
    @Override
    public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
        try {
            Class<?> aClass = IPCBridge.getInstance().getClass(request.getInterfacesName());
            Object object = IPCBridge.getInstance().getObject(aClass.getName());
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
}