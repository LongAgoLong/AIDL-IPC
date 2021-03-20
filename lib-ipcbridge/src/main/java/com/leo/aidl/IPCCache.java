package com.leo.aidl;

import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IPCCache {

    /**
     * 保存服务端处理客户端请求的interfaces对应Class映射和内部的方法
     */
    private final Map<String, Class<?>> mClazzs = new HashMap<>();
    private final Map<Class<?>, HashMap<String, Method>> mMethods = new HashMap<>();
    /**
     * 保存服务端处理客户端请求的实例
     */
    private final Map<String, WeakReference<Object>> mInstance = new HashMap<>();

    /**
     * 缓存对象及其方法
     *
     * @param object
     */
    public void register(Object object) {
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> cls : interfaces) {
            mClazzs.put(cls.getName(), clazz);
        }
        // 缓存Method
        HashMap<String, Method> method = new HashMap<>();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            method.put(m.getName(), m);
        }
        mMethods.put(clazz, method);
        addObject(clazz.getName(), object);
    }

    public void unRegister(Object object) {
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> cls : interfaces) {
            mClazzs.remove(cls.getName());
        }
        mMethods.remove(clazz);
        removeObject(clazz.getName());
    }

    public Class<?> getClass(String interfacesName) {
        if (TextUtils.isEmpty(interfacesName)) {
            return null;
        }
        return mClazzs.get(interfacesName);
    }

    public Method getMethod(Class<?> clazz, String methodName) {
        HashMap<String, Method> methods = mMethods.get(clazz);
        return methods == null ? null : methods.get(methodName);
    }

    private void addObject(String className, Object object) {
        mInstance.put(className, new WeakReference<>(object));
    }

    private void removeObject(String className) {
        mInstance.remove(className);
    }

    public Object getObject(String className) {
        return mInstance.containsKey(className) ? mInstance.get(className).get() : null;
    }
}
