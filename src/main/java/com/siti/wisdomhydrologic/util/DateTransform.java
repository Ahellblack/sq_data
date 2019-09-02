package com.siti.wisdomhydrologic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTransform {

    /**
     * 将字符串类型的日期，转换为Date对象
     *
     * @param pattern str的格式，如：yyyy-MM-dd HH:mm:ss
     */
    public static Date String2Date(String str, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (str == null || str.equals(""))
            return null;
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            System.out.println("string2Date出错");
            e.printStackTrace();
        }
        return date;
    }

    public static String Date2String(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 比较两个日期相差月数
     */
    public static int monthsOfTwo(Date fDate, Date tDate) {
        Calendar fCal = Calendar.getInstance();
        fCal.setTime(fDate);
        int fYear = fCal.get(Calendar.YEAR);
        int fMonth = fCal.get(Calendar.MONTH);
        Calendar tCal = Calendar.getInstance();
        tCal.setTime(tDate);
        int tYear = tCal.get(Calendar.YEAR);
        int tMonth = tCal.get(Calendar.MONTH);
        return (tYear - fYear) * 12 + (tMonth - fMonth);
    }

    /**
     * 比较两个日期相差天数
     */
    public static Integer daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        Integer day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        Integer day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 获取某月的最后一天
     */
    public static int getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);//获取某月最大天数
    }

    /**
     * 获取上月时间
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //日期减一,取得上月最后一天时间对象
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 根据日期年月，获取该月最大天数，并返回年月日
     * 参数str：年月字符串（例如："2012-01"）
     */
    public static Date getDayByMonth(String str) {
        Date month = String2Date(str, "yyyy-MM");
        int lastDay = getLastDayOfMonth(month);
        str = str + "-" + lastDay;
        return String2Date(str, "yyyy-MM-dd");
    }
    //        String str2=format(now, "YYYY-MM-dd HH:mm:ss", Locale.CHINA);

    public static String format(Date date) {
        if(date == null ) {
            return null;
        }
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.CHINA).format(date);
    }
    public static String format(String date) {
        if(date == null ) {
            return null;
        }
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.CHINA).format(date);
    }
    /**
     * 根据传入的日期返回指定天数的前后日期
     *
     * @param strDate:指定的日期，字符串
     * @param　days:天数，负值时向前，正值向后
     */
    public static String beforNumDay(String strDate, int days) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    /**
     * 秒转换为时间格式：xx:xx:xx
     * */
    public static String conversSecond2Time(String seconds) {
//        String str = "221";
        long tmp_second= Long.parseLong(seconds);
        long temp = 0;
        StringBuffer sb = new StringBuffer();
        temp = tmp_second / 3600;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = tmp_second % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = tmp_second % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);

//        System.out.println(sb.toString());
        return sb.toString();
    }
}
