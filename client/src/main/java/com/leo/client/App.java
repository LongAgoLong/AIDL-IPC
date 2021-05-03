package com.leo.client;

import android.app.Application;

import com.leo.aidl.client.IPCBridge;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IPCBridge.getInstance().register(InitResult.getInstance());
        IPCBridge.getInstance().init(this);
    }
}
