package com.leo.aidl.client;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.util.ParamsConvert;
import com.leo.aidl.util.XLog;

import java.lang.reflect.Method;

public class ClientImpl extends IClientBridge.Stub {
    private static final String TAG = "ClientImpl";

    @Override
    public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
        try {
            Class<?> aClass = IPCBridge.getInstance().getClass(request.getInterfacesName());
            if (aClass == null) {
                XLog.e(TAG, "The implementation class was not found.[" + request.getInterfacesName() + "]");
                return new IPCResponse("", false);
            }
            Object object = IPCBridge.getInstance().getObject(aClass.getName());
            Method me = aClass.getMethod(request.getMethodName(),
                    ParamsConvert.getParameterTypes(request.getParameters()));

            Object[] params = ParamsConvert.unSerializationParams(request.getParameters());
            Object result = me.invoke(object, params);
            String r = ParamsConvert.mGson.toJson(result);
            return new IPCResponse(r, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IPCResponse("", false);
    }
}
