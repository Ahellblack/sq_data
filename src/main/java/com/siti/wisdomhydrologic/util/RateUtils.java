package com.siti.wisdomhydrologic.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by zyw on 2019/10/23.
 */
public class RateUtils {

    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num) + "%";
    }

    public static Double accuracyTimes(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total ;
        String times = df.format(accuracy_num);
        //数字超过1000会出现蜜汁逗号
         times = times.replaceAll(",", "");
        return Double.parseDouble(times);
    }

    //修改double字符的精度
    public static Double setPrecision(double num ,int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        String times = df.format(num);
        //数字超过1000会出现蜜汁逗号
        times = times.replaceAll(",", "");
        return Double.parseDouble(times);
    }


}
