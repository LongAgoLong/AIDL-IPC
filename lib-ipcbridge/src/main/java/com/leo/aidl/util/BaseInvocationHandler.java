package com.leo.aidl.util;

import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public abstract class BaseInvocationHandler implements InvocationHandler {

    protected Object createDefaultResult(Class<?> returnType) {
        if (returnType == void.class || returnType == Void.class) {
            return null;
        }
        if (returnType == String.class) {
            return "";
        } else if (returnType == boolean.class || returnType == Boolean.class) {
            return false;
        } else if (returnType == int.class || returnType == Integer.class
                || returnType == short.class || returnType == Short.class
                || returnType == float.class || returnType == Float.class
                || returnType == double.class || returnType == Double.class
                || returnType == char.class) {
            return 0;
        } else if (returnType == long.class || returnType == Long.class) {
            return 0L;
        } else if (returnType == ArrayList.class || returnType == LinkedList.class) {
            return Collections.EMPTY_LIST;
        } else if (returnType == HashMap.class || returnType == LinkedHashMap.class) {
            return Collections.EMPTY_MAP;
        } else if (returnType == HashSet.class || returnType == LinkedHashSet.class) {
            return Collections.EMPTY_SET;
        } else {
            try {
                return returnType.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
