package com.leo.aidl;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class IpcDataCenter {
    private static final String SERVER_PATH_PREFIX = "com.leo.protocol.provider";
    private static final String CLIENT_PATH_PREFIX = "com.leo.protocol.client";
    /**
     * 保存服务端处理客户端请求的interfaces对应Class映射和内部的方法
     */
    private final Map<String, Class<?>> mClazzs = new HashMap<>();
    /**
     * 保存服务端处理客户端请求的实例
     */
    private final Map<String, Object> mInstance = new HashMap<>();

    private volatile static IpcDataCenter INSTANCE;

    public static IpcDataCenter getInstance() {
        if (INSTANCE == null) {
            synchronized (IpcDataCenter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IpcDataCenter();
                }
            }
        }
        return INSTANCE;
    }

    private IpcDataCenter() {
    }

    /**
     * 缓存对象及其方法
     *
     * @param object
     */
    public void register(Object object) {
        if (object == null) {
            return;
        }
        Class<?> clazz = object.getClass();
        boolean result = mappingInterface(clazz);
        if (!result) {
            throw new RuntimeException("Does not implement any interface. -> " + clazz.getName());
        }
        addObject(clazz.getName(), object);
    }

    /**
     * 遍历父类，寻找所有实现的adapter-protocol协议接口
     * 解决继承问题
     * tip：一个协议接口只能有一个实现类，会覆盖
     *
     * @param clazz
     * @return
     */
    private boolean mappingInterface(Class<?> clazz) {
        boolean isCache = false;
        Class<?> currentCls = clazz;
        do {
            Class<?>[] interfaces = currentCls.getInterfaces();
            if (interfaces.length != 0) {
                for (Class<?> inter : interfaces) {
                    // 加上接口名的path限制
                    if (inter.getName().startsWith(CLIENT_PATH_PREFIX)
                            || inter.getName().startsWith(SERVER_PATH_PREFIX)) {
                        isCache = true;
                        mClazzs.put(inter.getName(), clazz);
                    }
                }
            }
            currentCls = currentCls.getSuperclass();
        } while (currentCls != null);
        return isCache;
    }

    public void unRegister(Object object) {
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> cls : interfaces) {
            mClazzs.remove(cls.getName());
        }
        removeObject(clazz.getName());
    }

    public Class<?> getClass(String interfacesName) {
        if (TextUtils.isEmpty(interfacesName)) {
            return null;
        }
        return mClazzs.get(interfacesName);
    }

    private void addObject(String className, Object object) {
        mInstance.put(className, object);
    }

    private void removeObject(String className) {
        mInstance.remove(className);
    }

    public Object getObject(String className) {
        return mInstance.containsKey(className) ? mInstance.get(className) : null;
    }
}
