package com.leo.client.data;

import com.leo.aidl.util.IpcLog;
import com.leo.protocol.callback.IBindStatusListener;

public class IpcLinkStatus implements IBindStatusListener {
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

    public boolean isInit() {
        return isInit;
    }

    @Override
    public void onBindStatus(boolean isSuccess) {
        IpcLog.i(TAG, "onInitStatus:" + isSuccess);
        isInit = isSuccess;
    }
}
