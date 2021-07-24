package com.leo.aidl.service;

import com.google.gson.Gson;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.util.BaseInvocationHandler;
import com.leo.aidl.util.IpcConvert;

import java.lang.reflect.Method;

public class ServiceInvocationHandler extends BaseInvocationHandler {

    private final Gson mGson;
    private final String interfacesName;

    public ServiceInvocationHandler(String interfacesName) {
        this.interfacesName = interfacesName;
        mGson = new Gson();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*
          当代理对象执行方法时，会走到这里，然后构造请求，转发给客户端
         */
        IPCRequest ipcRequest = IpcConvert.build(interfacesName, method, args);
        IPCResponse ipcResponse = IpcService.getInstance().sendRequest(ipcRequest);
        Class<?> returnType = method.getReturnType();
        if (ipcResponse != null && ipcResponse.isSuccess()) {
            if (returnType == void.class || returnType == Void.class) {
                return null;
            }
            return mGson.fromJson(ipcResponse.getResult(), returnType);
        } else {
            return createDefaultResult(returnType);
        }
    }
}
