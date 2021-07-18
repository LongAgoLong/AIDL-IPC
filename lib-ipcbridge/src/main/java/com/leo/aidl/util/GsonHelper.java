package com.leo.aidl.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonHelper {
    private static final Gson X_GSON = new Gson();

    public static String toJson(Object src) {
        return X_GSON.toJson(src);
    }

    public static String toJson(JsonElement jsonElement) {
        return X_GSON.toJson(jsonElement);
    }

    public static <T> T fromJson(String src, Class<T> cls) {
        return X_GSON.fromJson(src, cls);
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> cls) {
        return X_GSON.fromJson(jsonElement, cls);
    }

    public static <T> List<T> toJavaList(String json, Class<T> cls) {
        Type type = new ParameterizedTypeImpl(cls);
        return X_GSON.fromJson(json, type);
    }

    public static <T> List<T> toJavaList(JsonElement jsonElement, Class<T> cls) {
        Type type = new ParameterizedTypeImpl(cls);
        return X_GSON.fromJson(jsonElement, type);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class<?> clazz;

        public ParameterizedTypeImpl(Class<?> clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
