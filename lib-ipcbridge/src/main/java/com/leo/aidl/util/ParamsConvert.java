package com.leo.aidl.util;

import com.google.gson.Gson;
import com.leo.aidl.IPCParameter;

public class ParamsConvert {

    public static Gson mGson = new Gson();

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
                objects[i] = mGson.fromJson(pa.getValue(), pa.getType());
            }
        }
        return objects;
    }

    public static IPCParameter[] serializationParams(Object[] params, Class<?>[] parameterTypes) {
        IPCParameter[] p;
        if (params == null) {
            p = new IPCParameter[0];
        } else {
            p = new IPCParameter[params.length];
            for (int i = 0; i < params.length; i++) {
                Object o = params[i];
                p[i] = new IPCParameter(o.getClass(), mGson.toJson(o), parameterTypes[i]);
            }
        }
        return p;
    }
}
