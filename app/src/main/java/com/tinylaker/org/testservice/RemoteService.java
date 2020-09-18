package com.tinylaker.org.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    
    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return stub;
    }

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getMessage() throws RemoteException {
            Log.i(TAG, "call getMessage");
            return null;
        }
    };

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    public void connect() {
        Log.i(TAG, "RemoteService connect");
    }

    public void disconnect() {
        Log.i(TAG, "RemoteService disconnect");
    }
}
