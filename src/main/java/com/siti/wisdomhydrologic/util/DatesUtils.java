package com.siti.wisdomhydrologic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2019/7/18.
 */
public class DatesUtils {

    //JAVA获取某段时间内的所有日期
    public List<String> findDates(String dStart, String dEnd) throws ParseException {
        Calendar cStart = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy");
        Date dS = formatter.parse(dStart);
        Date dE = formatter.parse(dEnd);
        cStart.setTime(dS);

        List dateList = new ArrayList();
        //别忘了，把起始日期加上
        dateList.add(formatter.format(dS));
        // 此日期是否在指定日期之后
        while (dE.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.YEAR, 1);
            dateList.add(formatter.format(cStart.getTime()));
        }
        return dateList;
    }
    //JAVA获取某段时间内的所有日期
    public static List<String> findMonthDates(String dStart, String dEnd) throws ParseException {
        Calendar cStart = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM");
        Date dS = formatter.parse(dStart);
        Date dE = formatter.parse(dEnd);
        cStart.setTime(dS);

        List dateList = new ArrayList();
        //别忘了，把起始日期加上
        dateList.add(formatter.format(dS));
        // 此日期是否在指定日期之后
        while (dE.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.MONTH, 1);
            dateList.add(formatter.format(cStart.getTime()));
        }
        return dateList;
    }
    /**
     * 获取某个时间段内所有月份
     * @param minDate
     * @param maxDate
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthBetweenDates(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat( "YYYY-MM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

/*
    //JAVA获取某段时间内的所有日期
    public List<String> findLastMonth(String month) throws ParseException {
        Calendar cStart = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM");
        Date monthDate = formatter.parse(month);
        cStart.setTime(monthDate);

        cStart.add(Calendar.MONTH,1);

        // 此日期是否在指定日期之后
        while (dE.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.YEAR, 1);
            dateList.add(formatter.format(cStart.getTime()));
        }
        return dateList;
    }
*/


    public static void main(String[] args) throws ParseException {

        SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM");
        String sdate = "2002-06";
        String edate = dateformat.format(new Date());
        System.out.println(sdate+"////"+edate);
        Date dS = dateformat.parse(sdate);
        Date dE = dateformat.parse(edate);
        System.out.println(dS+"////"+dE);

        System.out.println(findMonthDates(sdate,edate));
    }



}
