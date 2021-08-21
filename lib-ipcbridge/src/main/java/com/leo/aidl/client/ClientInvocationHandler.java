package com.leo.aidl.client;

import com.google.gson.Gson;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.IpcDataCenter;
import com.leo.aidl.util.BaseInvocationHandler;
import com.leo.aidl.util.IpcConvert;
import com.leo.aidl.util.IpcLog;

import java.lang.reflect.Method;

public class ClientInvocationHandler extends BaseInvocationHandler {
    private static final String TAG = "ClientInvocationHandler";

    private final Gson mGson;
    private final String interfacesName;
    private final boolean isIpc;

    public ClientInvocationHandler(String interfacesName, boolean isIpc) {
        this.interfacesName = interfacesName;
        this.isIpc = isIpc;
        mGson = new Gson();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*
          当代理对象执行方法时，会走到这里，然后构造请求，转发给服务端
         */
        Class<?> returnType = method.getReturnType();
        if (isIpc) {
            IPCRequest ipcRequest = IpcConvert.build(interfacesName, method, args);
            IPCResponse ipcResponse = IpcClient.getInstance().sendRequest(ipcRequest);
            if (ipcResponse != null && ipcResponse.isSuccess()) {
                if (returnType == void.class || returnType == Void.class) {
                    return null;
                }
                return mGson.fromJson(ipcResponse.getResult(), returnType);
            } else {
                return createDefaultResult(returnType);
            }
        } else {
            Class<?> aClass = IpcDataCenter.getInstance().getClass(interfacesName);
            if (aClass == null) {
                IpcLog.e(TAG, "The implementation class was not found.[" + method.getName() + "]");
                return createDefaultResult(returnType);
            }
            Object object = IpcDataCenter.getInstance().getObject(aClass.getName());
            Method me = aClass.getMethod(method.getName(), method.getParameterTypes());
            if (me == null) {
                IpcLog.e(TAG, "The method was not found.[" + method.getName() + "]");
                return createDefaultResult(returnType);
            }
            return me.invoke(object, args);
        }
    }
}