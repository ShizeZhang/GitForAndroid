package com.example.administrator.myapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/8.
 */

public class SimpleService extends Service {
    private final static String TAG = "wzj";
    private int count;
    private boolean quit;
    private Thread thread;
    private LocalBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        SimpleService getService(){
            return SimpleService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind invoke");
        return binder;
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate invoke");
        super.onCreate();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 每间隔一秒count加1 ，直到quit为true。
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        });
        thread.start();
    }
    /**
     * 公共方法
     * @return
     */
    public int getCount(){
        return count;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("onStart invoke");
        super.onStart(intent, startId);
        int i = startId;
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("Service is invoke onUnbind"+getCount());
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println("Service is invoke onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(){
        Notification.Builder mBulder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("123456")
                .setContentText("123123132123");
        Intent intent = new Intent(this,MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_CANCEL_CURRENT);
        mBulder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBulder.build();
        notificationManager.notify(0,notification);
        startForeground(0,notification);
    }
}
