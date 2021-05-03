package com.leo.client;

import android.util.Log;

import com.leo.lib_interface.client.IAttachSuccessListener;

public class InitResult implements IAttachSuccessListener {
    private static final String TAG = "IAttachSuccessListener";
    private static volatile InitResult mInstance;
    private boolean isInit;

    private InitResult() {
    }

    public static InitResult getInstance() {
        if (null == mInstance) {
            synchronized (InitResult.class) {
                if (null == mInstance) {
                    mInstance = new InitResult();
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