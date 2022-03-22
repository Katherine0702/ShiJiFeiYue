package com.bojing.gathering.base;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import java.util.List;

/**
 * Created by admin on 2018/8/29.
 */

public class NotificationCollectorMonitorService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        ensureCollectorRunning();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    //确认NotificationMonitor是否开启
    private void ensureCollectorRunning() {
        ComponentName collectorComponent = new ComponentName(this, MyNotificationListenService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == android.os.Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) {
            return;
        }
        toggleNotificationListenerService();
    }

    //重新开启NotificationMonitor
    private void toggleNotificationListenerService() {
        ComponentName thisComponent = new ComponentName(this, MyNotificationListenService.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}