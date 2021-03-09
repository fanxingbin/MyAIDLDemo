package com.fanxb.aidlserviceclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * <p>Class: com.fanxb.aidlserviceclient.ServiceApplication</p>
 * <p>Description: ServiceApplication</p>
 * <pre>
 *  ServiceApplication
 * </pre>
 *
 * @author fanxingbin
 * @date 2021/3/5/005/17:43
 */


public class ServiceApplication extends Application {
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        startService(new Intent(this, AidlService.class));
    }

    public static Context getContext() {
        return sContext;
    }
}
