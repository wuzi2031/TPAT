package com.testplatform.tpat.common;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static String DESIGN_PACAGE = "com.shishike.calm";
    public static String STAT_COLLET_BROADCAT_ACTION = "com.performance.start";
    public static String STOP_COLLET_BROADCAT_ACTION = "com.performance.stop";
    public static long COLLET_DELAY_TIME = 3 * 1000;
    public static long COLLET_PERIOD_TIME = 5 * 1000;

    //设备mnt/sdcard路径
    public static final String SDCARD = Environment.getExternalStorageDirectory() + "";
    public static final String PER_EXCEL_DIR = Environment.getExternalStorageDirectory() + File.separator + "pffs";
    public static final String PER_EXCEL_NAME = "performance_info.xls";
    public static final String PER_EXCEL_SHEET_NAME = "自动化性能数据";
}
