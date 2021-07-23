package com.leo.aidl.util;

import com.leo.aidl.IPCParameter;
import com.leo.aidl.IPCRequest;

import java.lang.reflect.Method;

public class IpcConvert {

    public static IPCRequest build(String interfacesName, Method method, Object[] args) {
        IPCRequest ipcRequest = new IPCRequest();
        ipcRequest.setInterfacesName(interfacesName);
        ipcRequest.setMethodName(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        ipcRequest.setParameters(IpcConvert.serializationParams(args, parameterTypes));
        return ipcRequest;
    }

    public static Class<?>[] getParameterTypes(IPCParameter[] parameters) {
        Class<?>[] parameterTypes;
        if (parameters == null || parameters.length == 0) {
            parameterTypes = new Class<?>[0];
        } else {
            parameterTypes = new Class<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                IPCParameter pa = parameters[i];
                parameterTypes[i] = pa.getParameterType();
            }
        }
        return parameterTypes;
    }

    public static Object[] unSerializationParams(IPCParameter[] parameters) {
        Object[] objects;
        if (parameters == null || parameters.length == 0) {
            objects = new Object[0];
        } else {
            objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                IPCParameter pa = parameters[i];
                objects[i] = GsonHelper.fromJson(pa.getValue(), pa.getParameterType());
            }
        }
        return objects;
    }

    private static IPCParameter[] serializationParams(Object[] params, Class<?>[] parameterTypes) {
        IPCParameter[] p;
        if (params == null) {
            p = new IPCParameter[0];
        } else {
            p = new IPCParameter[params.length];
            for (int i = 0; i < params.length; i++) {
                p[i] = new IPCParameter(GsonHelper.toJson(params[i]), parameterTypes[i]);
            }
        }
        return p;
    }
}
