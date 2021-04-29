package com.leo.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BridgeService extends Service {
    private final ServiceImpl serviceImpl = new ServiceImpl();

    public BridgeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceImpl;
    }
}
