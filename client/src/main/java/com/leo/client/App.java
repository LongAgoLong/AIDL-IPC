package com.leo.client;

import android.app.Application;

import com.leo.aidl.client.IPCBridge;
import com.leo.client.data.IpcLinkStatus;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IPCBridge.getInstance().register(IpcLinkStatus.getInstance());
        IPCBridge.getInstance().init(this);
    }
}
