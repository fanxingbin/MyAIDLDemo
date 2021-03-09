package com.fanxb.aidlserviceclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * <p>Class: com.fanxb.aidlserviceclient.AidlService</p>
 * <p>Description: AidlService</p>
 * <pre>
 *  AidlService
 * </pre>
 *
 * @author fanxingbin
 * @date 2021/3/5/005/17:02
 */


public class AidlService extends Service {

    private IAidlInterfaceImpl mIAidlInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mIAidlInterface = new IAidlInterfaceImpl();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIAidlInterface;
    }
}
