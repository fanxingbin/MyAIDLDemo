package com.fanxb.aidlclient;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.fanxb.aidlserviceclient.IAidlServiceInterface;

/**
 * <p>Class: com.fanxb.aidlclient.ClientAppliction</p>
 * <p>Description: ClientAppliction</p>
 * <pre>
 *  ClientAppliction
 * </pre>
 *
 * @author fanxingbin
 * @date 2021/3/8/008/15:37
 */


public class ClientAppliction extends Application {

    private static Context sContext;

    private static IAidlServiceInterface sIAidlServiceInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
//        bindService();
    }

    /**
     * bind AIDL Service
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.fanxb.aidlserviceclient.service");
        intent.setPackage("com.fanxb.aidlserviceclient");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    /**
     * Service Connection callback
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            sIAidlServiceInterface = IAidlServiceInterface.Stub.asInterface(iBinder);
            Log.i("===========>", "Server connected successfully!!!");
            try {
                String message = sIAidlServiceInterface.getMessages();
                sIAidlServiceInterface.sendMessageToService("this is client send message!!!");
                Log.i("===========>", "the message：" + message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sIAidlServiceInterface = null;
        }
    };

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context;
    }

    public static IAidlServiceInterface getIAidlServiceInterface() {
        return sIAidlServiceInterface;
    }

    public static void setIAidlServiceInterface(IAidlServiceInterface IAidlServiceInterface) {
        sIAidlServiceInterface = IAidlServiceInterface;
    }
}
