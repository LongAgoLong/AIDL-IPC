package com.leo.aidl.data;

import android.util.Log;

import com.leo.lib_interface.provider.ISInitListener;

public class InitResultHelper implements ISInitListener {
    private static final String TAG = "IInitListener";
    private static volatile InitResultHelper mInstance;
    private boolean isInit;

    private InitResultHelper() {
    }

    public static InitResultHelper getInstance() {
        if (null == mInstance) {
            synchronized (InitResultHelper.class) {
                if (null == mInstance) {
                    mInstance = new InitResultHelper();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onInitSuccess() {
        Log.i(TAG, "onInitSuccess");
        isInit = true;
    }

    public boolean isInit() {
        return isInit;
    }
}
