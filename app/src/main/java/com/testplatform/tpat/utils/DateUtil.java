package com.testplatform.tpat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getCurrTime() {
        Date date = new Date();
        SimpleDateFormat currTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        String currTimeStr = currTime.format(date);
        return currTimeStr;
    }
}
