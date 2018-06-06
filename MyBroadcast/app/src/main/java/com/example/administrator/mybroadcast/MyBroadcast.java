package com.example.administrator.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2018/6/5.
 */

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Log.e("zsz",msg+"  "+1);
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg + "@1");
        setResultExtras(bundle);
    }
}
