package com.leo.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.leo.aidl.IClientBridge;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IService;
import com.leo.aidl.ParamsConvert;

import java.lang.reflect.Method;

public class AIDLService extends Service {
    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    IService.Stub stub = new IService.Stub() {
        @Override
        public IPCResponse sendRequest(IPCRequest request) throws RemoteException {
            try {
                Class<?> aClass = ServiceManager.getInstance().getClass(request.getInterfacesName());
                Object object = ServiceManager.getInstance().getObject(aClass.getName());
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
            ServiceManager.getInstance().putClientBridge(iClientBridge);
        }
    };
}
