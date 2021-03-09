package com.fanxb.aidlserviceclient;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

/**
 * <p>Class: com.fanxb.aidlserviceclient.IAidlInterfaceImpl</p>
 * <p>Description: IAidlInterfaceImpl</p>
 * <pre>
 *  IAidlInterfaceImpl
 * </pre>
 *
 * @author fanxingbin
 * @date 2021/3/5/005/17:13
 */


public class IAidlInterfaceImpl extends IAidlServiceInterface.Stub {
    private RemoteCallbackList<ICallBackInterface> mCallbackList = new RemoteCallbackList<>();

    @Override
    public String getMessages() throws RemoteException {
        return "This is the AIDL ServiceClient message!";
    }

    @Override
    public void sendMessageToService(String message) throws RemoteException {
        Log.i("===========>", "service receive :" + message);
        if (TextUtils.isEmpty(message)) {
            dispatchResult(false, "receive message failed, message isEmpty ");
        } else {
            String msg = message;
            dispatchResult(true, "receive message successfully");
        }
    }

    @Override
    public void registerCallBack(ICallBackInterface callBackInterface) throws RemoteException {
        if (callBackInterface != null) {
            mCallbackList.register(callBackInterface);
        }
    }

    @Override
    public void unregisterCallBack(ICallBackInterface callBackInterface) throws RemoteException {
        if (callBackInterface != null) {
            mCallbackList.unregister(callBackInterface);
        }
    }


    /**
     * 结果分发
     *
     * @param result this is result
     * @param msg    this is msg
     */
    private void dispatchResult(boolean result, String msg) {
        int length = mCallbackList.beginBroadcast();
        for (int i = 0; i < length; i++) {
            ICallBackInterface callBackInterface = mCallbackList.getBroadcastItem(i);
            try {
                if (result) {
                    callBackInterface.onSuccess(msg);
                } else {
                    callBackInterface.onFailed(msg);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbackList.finishBroadcast();
    }
}
