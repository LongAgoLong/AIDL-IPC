package com.leo.aidl.data;

import com.leo.aidl.util.XLog;
import com.leo.lib_interface.provider.IBindSuccessListener;

public class InitResult implements IBindSuccessListener {
    private static final String TAG = "IBindSuccessListener";
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
        XLog.i(TAG, "onInitSuccess");
        isInit = true;
    }

    public boolean isInit() {
        return isInit;
    }
}
