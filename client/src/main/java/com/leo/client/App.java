package com.leo.client;

import android.app.Application;

import com.leo.aidl.client.IpcClient;
import com.leo.client.data.IpcLinkStatus;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IpcClient.getInstance().setIBindStatusListener(IpcLinkStatus.getInstance());
        IpcClient.getInstance().init(this, "com.leo.aidl", true);
    }
}
