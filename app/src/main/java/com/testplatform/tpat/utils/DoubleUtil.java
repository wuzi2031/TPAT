package com.testplatform.tpat.utils;

import java.math.BigDecimal;

/**
 * Double数据的操作 使用Java，double 进行运算时，经常出现精度丢失的问题，总是在一个正确的结果左右偏0.0000**1。
 * 特别在实际项目中，通过一个公式校验该值是否大于0，如果大于0我们会做一件事情，小于0我们又处理其他事情。
 * 这样的情况通过double计算出来的结果去和0比较大小，尤其是有小数点的时候，经常会因为精度丢失而导致程序处理流程出错。
 */
public class DoubleUtil {
    /**
     * double 乘法
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        try {
            return bd1.multiply(bd2).doubleValue();
        } catch (Exception e) {
            // 根据bugly观测，在进入GTOpMulPerfActivity页时有极小概率crash，故加上异常保护
            // @see http://bugly.qq.com/detail?app=900010910&pid=1&ii=152#stack
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * double 除法
     *
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1, double d2, int scale) {
        // 当然在此之前，你要判断分母是否为0，
        // 为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
//		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 直接向下取整，保持和UI展示一致
        try {
            return bd1.divide(bd2, scale, BigDecimal.ROUND_DOWN).doubleValue();
        } catch (Exception e) {
            // 根据bugly观测，在进入GTOpMulPerfActivity页时有极小概率crash，故加上异常保护
            // @see http://bugly.qq.com/detail?app=900010910&pid=1&ii=46#stack
            e.printStackTrace();
            return 0;
        }

    }
}