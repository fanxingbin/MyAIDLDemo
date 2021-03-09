package com.fanxb.aidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fanxb.aidlserviceclient.IAidlServiceInterface;
import com.fanxb.aidlserviceclient.ICallBackInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBindService;
    private Button mUnBindService;
    private Button mGetMessage;
    private Button mSendMessageToService;
    private static IAidlServiceInterface sIAidlServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBindService = findViewById(R.id.bindService);
        mUnBindService = findViewById(R.id.unBindService);
        mGetMessage = findViewById(R.id.getMessage);
        mSendMessageToService = findViewById(R.id.sendMessageToService);

        mBindService.setOnClickListener(this);
        mUnBindService.setOnClickListener(this);
        mGetMessage.setOnClickListener(this);
        mSendMessageToService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bindService:
                bindService();
                break;
            case R.id.unBindService:
                unBindService();
                break;
            case R.id.getMessage:
                if (sIAidlServiceInterface != null) {
                    try {
                        String message = sIAidlServiceInterface.getMessages();
                        Log.i("============>", "client getMessage:" + message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("============>", "sIAidlServiceInterface is null");
                }

                break;
            case R.id.sendMessageToService:
                if (sIAidlServiceInterface != null) {
                    try {
                        sIAidlServiceInterface.sendMessageToService("this is client send message!!!");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("============>", "sIAidlServiceInterface is null");
                }

                break;
            default:
                break;
        }
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
                //注册callBack事件
                sIAidlServiceInterface.registerCallBack(mICallBackInterface);

                //设置Binder死亡代理
                sIAidlServiceInterface.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("==============>", "onServiceDisconnected!!!");
        }
    };


    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (null == sIAidlServiceInterface) {
                return;
            }
            sIAidlServiceInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
            sIAidlServiceInterface = null;
            Log.i("========>", "开始重连！");
            bindService();
        }
    };

    ICallBackInterface mICallBackInterface = new ICallBackInterface.Stub() {
        @Override
        public void onSuccess(String result) throws RemoteException {
            Log.i("============>", "mICallBackInterface message : " + result);
        }

        @Override
        public void onFailed(String errorMessage) throws RemoteException {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    private void unBindService() {
        if (null != sIAidlServiceInterface && sIAidlServiceInterface.asBinder().isBinderAlive()) {
            try {
                sIAidlServiceInterface.unregisterCallBack(mICallBackInterface);
                unbindService(mServiceConnection);
                sIAidlServiceInterface = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
