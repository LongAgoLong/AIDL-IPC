package com.leo.aidl.util;

import android.util.Log;

public class XLog {
    private static final String TAG = "ipc_bridge_";
    private static int mLevel;

    public static void setLevel(int level) {
        mLevel = level;
    }

    public static void d(String tag, String msg) {
        if (mLevel < Log.DEBUG) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mLevel < Log.INFO) {
            Log.i(TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (mLevel < Log.VERBOSE) {
            Log.v(TAG + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mLevel < Log.WARN) {
            Log.w(TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mLevel < Log.ERROR) {
            Log.e(TAG + tag, msg);
        }
    }
}
