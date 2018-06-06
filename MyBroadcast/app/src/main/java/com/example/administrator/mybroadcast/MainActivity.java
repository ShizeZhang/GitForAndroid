package com.example.administrator.mybroadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        IntentFilter intentFilter = new IntentFilter();
//        MyBroadcast myBroadcast = new MyBroadcast();
//        MyBroadcastFrist myBroadcast1 = new MyBroadcastFrist();
//        MyBroadcastSecond myBroadcast2 = new MyBroadcastSecond();
//        intentFilter.addAction("android.intent.action.MY_BROADCAST");
//        intentFilter.setPriority(1000);
//        registerReceiver(myBroadcast,intentFilter);
//        IntentFilter intentFilter1 = new IntentFilter();
//        intentFilter1.addAction("android.intent.action.MY_BROADCAST");
//        intentFilter1.setPriority(999);
//        registerReceiver(myBroadcast1,intentFilter1);
//        IntentFilter intentFilter2 = new IntentFilter();
//        intentFilter2.addAction("android.intent.action.MY_BROADCAST");
//        intentFilter2.setPriority(998);
//        registerReceiver(myBroadcast2,intentFilter2);
        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
        intent.putExtra("msg","hello world.");
        sendOrderedBroadcast(intent, "scott.permission.MY_BROADCAST_PERMISSION");
        ViewGroup
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
