package com.cshen.tiyu.utils;



import java.io.File;

import android.os.Environment;
import android.util.Log;

public class DirsUtil {
    public static Boolean hasSd = Boolean.valueOf(false);
    public static String COMMOMDIR;
    public static String AppFileDir;
    public static String SDFileDir;

    static {
        COMMOMDIR = "/Cloudy/com.cshen.tiyu";
        AppFileDir = "/data/data/com.cshen.tiyu/files";
        SDFileDir = Environment.getExternalStorageDirectory().toString();
    }

    public DirsUtil() {
    }

    public static String getSD_DIRS() {
        StringBuilder SD_DIRS = new StringBuilder();
        hasSd = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        if(hasSd.booleanValue()) {
            SD_DIRS = SD_DIRS.append(SDFileDir).append(COMMOMDIR);
        } else {
            SD_DIRS = SD_DIRS.append(AppFileDir);
        }

        return SD_DIRS.toString();
    }

    public static String getSD_PHOTOS() {
        StringBuilder SD_PHOTOS = new StringBuilder();
        hasSd = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        if(hasSd.booleanValue()) {
            SD_PHOTOS = SD_PHOTOS.append(SDFileDir).append(COMMOMDIR).append("/photos");
        } else {
            SD_PHOTOS = SD_PHOTOS.append(AppFileDir).append(COMMOMDIR).append("/photos");
        }

        File file = new File(SD_PHOTOS.toString());
        if(!file.exists()) {
            file.mkdirs();
        }

        Log.d("SD_PHOTOS", "-->" + SD_PHOTOS);
        return SD_PHOTOS.toString();
    }

    public static String getSD_DOWNLOADS() {
        StringBuilder SD_DOWNLOADS = new StringBuilder();
        hasSd = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        if(hasSd.booleanValue()) {
            SD_DOWNLOADS = SD_DOWNLOADS.append(SDFileDir).append(COMMOMDIR).append("/downloads/");
        } else {
            SD_DOWNLOADS = SD_DOWNLOADS.append(AppFileDir).append(COMMOMDIR).append("/downloads/");
        }

        File file = new File(SD_DOWNLOADS.toString());
        if(!file.exists()) {
            file.mkdirs();
        }

        return SD_DOWNLOADS.toString();
    }

    public static String getSD_DOWNLOADSPhoto() {
        StringBuilder SD_VEDIOS = new StringBuilder();
        hasSd = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        if(hasSd.booleanValue()) {
            SD_VEDIOS = SD_VEDIOS.append(SDFileDir).append("/vedio");
        } else {
            SD_VEDIOS = SD_VEDIOS.append(AppFileDir).append("/vedio");
        }

        File file = new File(SD_VEDIOS.toString());
        if(!file.exists()) {
            file.mkdirs();
        }

        Log.d("SD_DOWNLOADS", "-->" + SD_VEDIOS);
        return SD_VEDIOS.toString();
    }

    public static String getSD_LOG() {
        StringBuilder SD_LOG = new StringBuilder();
        hasSd = Boolean.valueOf(Environment.getExternalStorageState().equals("mounted"));
        if(hasSd.booleanValue()) {
            SD_LOG = SD_LOG.append(SDFileDir).append(COMMOMDIR).append("/Log");
        } else {
            SD_LOG = SD_LOG.append(AppFileDir).append(COMMOMDIR).append("/Log");
        }

        Log.d("SD_LOG", "-->" + SD_LOG);
        return SD_LOG.toString();
    }
}
