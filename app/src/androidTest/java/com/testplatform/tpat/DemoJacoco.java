package com.testplatform.tpat;

import android.annotation.SuppressLint;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


public class DemoJacoco extends
        ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    @SuppressLint("NewApi")
    public DemoJacoco() {
        super(MainActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /*------ Test Core Function ------*/
    public void testOnAdd() throws Exception {
        solo.clickOnText("开始");
        solo.clickOnText("停止");
    }

}

