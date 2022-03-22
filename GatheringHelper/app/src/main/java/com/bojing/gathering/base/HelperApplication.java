package com.bojing.gathering.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.appcompat.BuildConfig;

import org.xutils.x;

public class HelperApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        startService(new Intent(this, NotificationCollectorMonitorService.class));
    }
}
