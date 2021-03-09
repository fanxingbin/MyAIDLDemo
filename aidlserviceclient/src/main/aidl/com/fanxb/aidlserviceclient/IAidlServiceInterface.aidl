// IAidlServiceInterface.aidl
package com.fanxb.aidlserviceclient;

// Declare any non-default types here with import statements
import com.fanxb.aidlserviceclient.ICallBackInterface;

interface IAidlServiceInterface {
    String getMessages();
    void sendMessageToService(String message);
    void registerCallBack(ICallBackInterface callBackInterface);
    void unregisterCallBack(ICallBackInterface callBackInterface);
}
