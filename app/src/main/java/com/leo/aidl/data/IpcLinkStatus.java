package com.leo.aidl.data;

import com.leo.aidl.util.IpcLog;
import com.leo.protocol.callback.IBindStatusListener;

public class IpcLinkStatus implements IBindStatusListener {
    private static final String TAG = "IBindStatusListener";
    private static volatile IpcLinkStatus mInstance;
    private boolean isInit;

    private IpcLinkStatus() {
    }

    public static IpcLinkStatus getInstance() {
        if (null == mInstance) {
            synchronized (IpcLinkStatus.class) {
                if (null == mInstance) {
                    mInstance = new IpcLinkStatus();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onBindStatus(boolean isSuccess) {
        IpcLog.i(TAG, "onBindStatus:" + isSuccess);
        isInit = isSuccess;
    }

    public boolean isInit() {
        return isInit;
    }
}
