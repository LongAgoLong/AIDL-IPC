package com.leo.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BridgeService extends Service {
    private final ServiceImpl serviceImpl = new ServiceImpl();

    public BridgeService() {
        // 如若需要，可设置成前台服务
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceImpl;
    }
}
