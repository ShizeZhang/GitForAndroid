package com.example.administrator.myapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
    protected static final String TAG = "wzj";
    private Button startBtn;
    private Button stopBtn,stopServiceall;
    private ServiceConnection conn;
    private SimpleService mService;
    HashMap hashMap = new HashMap();
    final SimpleService.LocalBinder[] binder = new SimpleService.LocalBinder[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn= (Button) findViewById(R.id.startService);
        stopBtn= (Button) findViewById(R.id.stopService);
        stopServiceall= (Button) findViewById(R.id.stopServiceall);
        //创建绑定对象
        final Intent intent = new Intent(this, SimpleService.class);
        hashMap.put("","");
        // 开启绑定
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用绑定方法
                bindService(intent, conn, Service.BIND_AUTO_CREATE);
//                  startService(intent);

            }
        });
        // 解除绑定
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 解除绑定
                if(mService!=null) {
                    mService = null;
                    unbindService(conn);

                }
                //stopService(intent);
            }
        });
        stopServiceall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(binder!=null){
                   System.out.print(binder[0]);
               }
            }
        });

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println( "绑定成功调用：onServiceConnected");
                // 获取Binder
                binder[0] = (SimpleService.LocalBinder) service;
                mService = binder[0].getService();
                System.out.println(binder[0]);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService=null;
            }
        };
//        // 获取数据
//        btnGetDatas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mService != null) {
//                    // 通过绑定服务传递的Binder对象，获取Service暴露出来的数据
//
//                    Log.d(TAG, "从服务端获取数据：" + mService.getCount());
//                } else {
//
//                    Log.d(TAG, "还没绑定呢，先绑定,无法从服务端获取数据");
//                }
//            }
//        });
    }
//    @Override
//    public void onClick(View v) {
//        Intent it=new Intent(this, SimpleService.class);
//        switch (v.getId()){
//            case R.id.startService:
//                startService(it);
//                break;
//            case R.id.stopService:
//                stopService(it);
//                break;
//        }
//    }

}
