// IICallBackInterface.aidl
package com.fanxb.aidlserviceclient;

// Declare any non-default types here with import statements

interface ICallBackInterface {
   void onSuccess(String result);
   void onFailed(String errorMessage);
}
