package com.leo.aidl.util;

import android.os.IBinder;
import android.os.RemoteException;

public abstract class DeathRecipientImpl implements IBinder.DeathRecipient {
    private IBinder mBinder;

    public DeathRecipientImpl(IBinder mBinder) {
        this.mBinder = mBinder;
    }

    public void bind() {
        try {
            if (mBinder != null) {
                mBinder.linkToDeath(this, 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unbind() {
        if (mBinder != null) {
            mBinder.unlinkToDeath(this, 0);
            mBinder = null;
        }
    }
}
