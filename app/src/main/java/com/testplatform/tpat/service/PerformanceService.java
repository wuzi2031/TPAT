package com.testplatform.tpat.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


import com.testplatform.tpat.common.Constants;
import com.testplatform.tpat.runnable.CollectTimer;

import java.util.Timer;

public class PerformanceService extends Service {
    private BroadcastReceiver broadcastReceiver;
    Timer timer;

    public PerformanceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    int level = intent.getIntExtra("level", 0);
                    //  level加%就是当前电量了
                } else if (action.equals(Constants.STAT_COLLET_BROADCAT_ACTION)) {
                    startCollectTimer();
                } else if (action.equals(Constants.STOP_COLLET_BROADCAT_ACTION)) {
                    stopTimer();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Constants.STAT_COLLET_BROADCAT_ACTION);
        intentFilter.addAction(Constants.STOP_COLLET_BROADCAT_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        return START_STICKY;
    }

    private void startCollectTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new CollectTimer(this.getApplicationContext()), Constants.COLLET_DELAY_TIME,
                Constants.COLLET_PERIOD_TIME);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
