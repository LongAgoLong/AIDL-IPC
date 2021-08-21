package com.leo.aidl;

import android.app.Application;

import com.leo.aidl.data.DataManager;
import com.leo.aidl.data.IpcLinkStatus;
import com.leo.aidl.service.IpcService;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IpcDataCenter.getInstance().register(DataManager.getInstance());
        IpcService.getInstance().setIBindStatusListener(IpcLinkStatus.getInstance());
        IpcService.getInstance().init(this, true);
    }
}
