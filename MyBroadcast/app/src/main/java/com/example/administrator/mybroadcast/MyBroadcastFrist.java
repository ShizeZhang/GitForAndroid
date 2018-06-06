package com.example.administrator.mybroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2018/6/5.
 */

public class MyBroadcastFrist extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = getResultExtras(true).getString("msg");
        Log.e("zsz",msg+"  "+2);
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg + "2");
        setResultExtras(bundle);
    }
}
