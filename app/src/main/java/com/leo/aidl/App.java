package com.leo.aidl;

import android.app.Application;
import android.content.Intent;

import com.leo.aidl.data.DataManager;
import com.leo.aidl.data.IpcLinkStatus;
import com.leo.aidl.service.BridgeService;
import com.leo.aidl.service.ServiceCenter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceCenter.getInstance().register(IpcLinkStatus.getInstance());
        ServiceCenter.getInstance().register(DataManager.getInstance());
        startService(new Intent(this, BridgeService.class));
    }
}
