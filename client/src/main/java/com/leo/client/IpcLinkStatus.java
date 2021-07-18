package com.leo.client;

import com.leo.aidl.util.XLog;
import com.leo.lib_interface.client.IAttachStatusListener;

public class IpcLinkStatus implements IAttachStatusListener {
    private static final String TAG = "IAttachStatusListener";
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
    public void onInitStatus(boolean isSuccess) {
        XLog.i(TAG, "onInitStatus:" + isSuccess);
        isInit = isSuccess;
    }

    public boolean isInit() {
        return isInit;
    }
}
