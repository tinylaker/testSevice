package com.tinylaker.org.testservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //local service connect
    private boolean mLocalBinder = false;
    private LocalService mBindService;

    //Remote aidl
    private IMyAidlInterface myAidlInterface;
    private boolean mRemoteBinder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initLayoutView();
    }

    private void initLayoutView() {

        //local service binder
        Button mBind = findViewById(R.id.bind_local_service);
        mBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBindService();
            }
        });

        Button mUnbind = findViewById(R.id.unbind_local_service);
        mUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUnbindService();
            }
        });

        //remote service binder
        Button mRemoteBind = findViewById(R.id.bind_remote_service);
        mRemoteBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doBindRemoteService();
            }
        });

        Button mRemoteUnbind = findViewById(R.id.unbind_remote_service);
        mRemoteUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUnbindRemoteService();
            }
        });
    }

    private ServiceConnection mLocalConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBindService = ((LocalService.LocalBinder)iBinder).getService();
            mBindService.connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBindService.disconnect();
            mBindService = null;
        }
    };

    void doBindService() {
        //1.直接启用LocalService，调用onStartCommand方法
        //startService(new Intent(MainActivity.this, LocalService.class));

        //2.绑定服务实现控制
        mLocalBinder = bindService(new Intent(MainActivity.this, LocalService.class),
                mLocalConnection, Context.BIND_AUTO_CREATE);

    }

    void doUnbindService() {
        if (mLocalBinder) {
            mLocalBinder = false;
            //解绑服务
            unbindService(mLocalConnection);
        }
    }

    private ServiceConnection mRemoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //从连接中获取Binder Stub对象
            myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);

            //调用Remote Service方法
            try {
                myAidlInterface.getMessage();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myAidlInterface = null;
        }
    };

    void doBindRemoteService() {
        mRemoteBinder = bindService(new Intent(MainActivity.this, RemoteService.class), mRemoteServiceConnection, BIND_AUTO_CREATE);
    }

    void doUnbindRemoteService() {
        if (mRemoteBinder) {
            mRemoteBinder = false;
            unbindService(mRemoteServiceConnection);
        }
    }
}