package com.leo.aidl.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.leo.aidl.IService;
import com.leo.aidl.client.ClientBridge;
import com.leo.aidl.client.ClientManager;

public class AIDLUtil {
    private static AIDLUtil aidlUtil;
    private AIDLUtil() {
    }

    public static AIDLUtil getInstance() {
        if (null == aidlUtil) {
            synchronized (AIDLUtil.class) {
                if (null == aidlUtil) {
                    aidlUtil = new AIDLUtil();
                }
            }
        }
        return aidlUtil;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("LEO-TEST", "绑定成功");
            IService iService = IService.Stub.asInterface(service);
            try {
                iService.asBinder().linkToDeath(() -> {},0);
                iService.attach(new ClientBridge());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            ClientManager.getInstance().putIpcService(iService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ClientManager.getInstance().putIpcService(null);
        }
    };

    public void bindService(Context context) {
        Intent intent = new Intent("com.leo.aidl");
        intent.setPackage("com.leo.aidl");
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        IService iService = ClientManager.getInstance().getIpcService();
        if (null == iService) {
            return;
        }
        Log.i("LEO-TEST", "取消绑定");
        context.unbindService(connection);
    }
}
