package com.testplatform.tpat.caserunner;

import android.util.Log;

import com.testplatform.tpat.utils.ShellUtil;


public class Runner {


    public static void run() {
        String cmd = "am instrument --user 0 -w -r   -e debug false -e class 'com.shishike.calm.testcases.fastfood.FastFoodBalance#test_MemberDiscountTimeLimit_027' com.shishike.calm.test/com.zutubi.android.junitreport.JUnitReportTestRunner";
        ShellUtil.CommandResult commandResult = ShellUtil.execCommand(cmd, false);
        Log.e("per", commandResult.successMsg);
    }
}
