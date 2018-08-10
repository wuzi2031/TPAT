package com.testplatform.tpat.runnable;

import android.content.Context;
import android.util.Log;


import com.testplatform.tpat.bean.TrafficInfo;
import com.testplatform.tpat.common.Constants;
import com.testplatform.tpat.performance.PerInfo;
import com.testplatform.tpat.utils.DateUtil;
import com.testplatform.tpat.utils.DoubleUtil;
import com.testplatform.tpat.utils.ExcelUtil;

import java.util.TimerTask;

public class CollectTimer extends TimerTask {
    Context context;
    PerInfo perInfo;
    ExcelUtil excelUtil;

    public CollectTimer(Context context) {
        this.context = context;
        perInfo = new PerInfo(context);
        excelUtil = new ExcelUtil();
        createExcel();
    }

    private void createExcel() {
        String[] headers = {"CPU", "内存剩余(MB)", "上行流量(KB)", "下行流量(KB)", "时间"};
        excelUtil.createExcel(Constants.PER_EXCEL_DIR, Constants.PER_EXCEL_NAME, Constants.PER_EXCEL_SHEET_NAME, headers);
    }

    @Override
    public void run() {
        String uid = perInfo.getUid(Constants.DESIGN_PACAGE);
        String time = DateUtil.getCurrTime();
        double cpuUsage = perInfo.getCpuUsage();//cpu使用情况
        Long availMem = perInfo.getAvailMemory();//剩余内存
        TrafficInfo trafficInfo=perInfo.getTrafficInfo();
        Long rxTraffic = perInfo.getRxTraffic(uid);
        Long txRraffic = perInfo.getTxTraffic(uid);
        String[][] datalist = new String[1][5];
        datalist[0][0] = cpuUsage + "";
        Double mavailMem = DoubleUtil.div(availMem.doubleValue(), 1024 * 1024.0, 2);//转化为MB
        datalist[0][1] = mavailMem + "";

        datalist[0][2] = rxTraffic + "";
        datalist[0][3] = txRraffic + "";
        datalist[0][4] = time;
        excelUtil.appendData(Constants.PER_EXCEL_DIR, Constants.PER_EXCEL_NAME, Constants.PER_EXCEL_SHEET_NAME, datalist);
        Log.e("per", "收集数据中");
        Log.e("per", cpuUsage + "");
        Log.e("per", mavailMem + "Mb");
        Log.e("per", rxTraffic + "Kb");
        Log.e("per", txRraffic + "Kb");
    }
}
