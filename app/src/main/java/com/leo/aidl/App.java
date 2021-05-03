package com.leo.aidl;

import android.app.Application;

import com.leo.aidl.data.InitResult;
import com.leo.aidl.service.ServiceCenter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceCenter.getInstance().register(InitResult.getInstance());
    }
}
