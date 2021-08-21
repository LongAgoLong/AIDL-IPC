package com.leo.aidl.client;

import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IpcDataCenter;
import com.leo.aidl.util.GsonHelper;
import com.leo.aidl.util.IpcConvert;
import com.leo.aidl.util.IpcLog;

import java.lang.reflect.Method;

public class ClientImpl extends IClientBridge.Stub {
    private static final String TAG = "ClientImpl";

    @Override
    public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
        try {
            Class<?> aClass = IpcDataCenter.getInstance().getClass(request.getInterfacesName());
            if (aClass == null) {
                IpcLog.e(TAG, "The implementation class was not found.[" + request.getInterfacesName() + "]");
                return new IPCResponse("", false);
            }
            Object object = IpcDataCenter.getInstance().getObject(aClass.getName());
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
}
