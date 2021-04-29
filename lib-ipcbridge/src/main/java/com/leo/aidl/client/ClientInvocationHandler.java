package com.leo.aidl.client;

import com.google.gson.Gson;
import com.leo.aidl.IPCRequest;
import com.leo.aidl.IPCResponse;
import com.leo.aidl.util.ParamsConvert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ClientInvocationHandler implements InvocationHandler {

    private final Gson mGson;
    private final String interfacesName;

    public ClientInvocationHandler(String interfacesName) {
        this.interfacesName = interfacesName;
        mGson = new Gson();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*
          当代理对象执行方法时，会走到这里
          然后构造请求
          转发给服务端
         */
        IPCRequest ipcRequest = new IPCRequest();
        ipcRequest.setInterfacesName(interfacesName);
        ipcRequest.setMethodName(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        ipcRequest.setParameters(ParamsConvert.serializationParams(args, parameterTypes));
        IPCResponse ipcResponse = ClientManager.getInstance().sendRequest(ipcRequest);
        if (ipcResponse != null && ipcResponse.isSuccess()) {
            Class<?> returnType = method.getReturnType();
            if (returnType != void.class && returnType != Void.class) {
                return mGson.fromJson(ipcResponse.getResult(), returnType);
            }
        }
        return null;
    }
}