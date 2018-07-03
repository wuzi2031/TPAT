package com.testplatform.tpat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.testplatform.tpat.common.Constants;
import com.testplatform.tpat.service.PerformanceService;


public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startPerformanceService();
    }

    private void initView() {
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCollect();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCollect();
            }
        });
    }

    private void startPerformanceService() {
        Intent intent = new Intent(this, PerformanceService.class);
        startService(intent);
    }

    /**
     * 停止数据收集
     */
    private void stopCollect() {
        Intent intent = new Intent();
        intent.setAction(Constants.STOP_COLLET_BROADCAT_ACTION);
        intent.putExtra("msg", "stop");
        sendBroadcast(intent);
    }

    /**
     * 开始数据收集
     */
    private void startCollect() {
        Intent intent = new Intent();
        intent.setAction(Constants.STAT_COLLET_BROADCAT_ACTION);
        intent.putExtra("msg", "start");
        sendBroadcast(intent);

//        PerInfo perInfo = new PerInfo(this);
//        perInfo.getRxTraffic(perInfo.getUid(Constants.DESIGN_PACAGE));
//        perInfo.getTxTraffic(perInfo.getUid(Constants.DESIGN_PACAGE));
//        perInfo.getTrafficInfo();

//        ExcelUtil excelUtil = new ExcelUtil();
//        String[] headers = {"CPU", "内存剩余", "上行流量", "下行流量", "时间"};
//        excelUtil.createExcel(Constants.PER_EXCEL_DIR, Constants.PER_EXCEL_NAME, Constants.PER_EXCEL_SHEET_NAME, headers);

//        Runner.run();
    }
}
