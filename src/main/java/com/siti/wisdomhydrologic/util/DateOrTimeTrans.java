package com.siti.wisdomhydrologic.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateOrTimeTrans {

    public DateOrTimeTrans() {

    }

    public static String TimeStamp2Date(Timestamp timestamp) {
        long timest = timestamp.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timest));
    }

    public static String TimeStamp2Cal(Timestamp timestamp) {
        long timest = timestamp.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timest));
    }

    public static String nowTimetoString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }


    public static String nowDatetoString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }


    public static String TimetoString(Long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        return simpleDateFormat.format(date);
    }

    public static Timestamp String2Timestamp(String time) {
        return Timestamp.valueOf(time);
    }

    /**
     * 按照yyyy-MM-dd将String转换为Date
     *
     * @return 若异常则返回null。
     */
    public static Date String2Date(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date String2DateTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date String2Date2(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 按照format格式，如yyyy-MM-dd将String转换为Date
     *
     * @return 若异常则返回null。
     */
    public static Date String2Date(String format, String date) {
        if (date.contains("-"))
            format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String Date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String Date2TimeString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String Date2TimeString2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String Date2TimeString3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String Year2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    public static String TimeStamp2String(Timestamp timestamp) {
        long timest = timestamp.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timest));
    }

    public static String nowTimetoStringWithout() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获得指定日期的后 n 天
     *
     * @param specifiedDay 指定日期
     * @param n            后几天
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int n) {
        Calendar c = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            if (date != null) {
                c.setTime(date);
                c.set(Calendar.DATE, c.get(Calendar.DATE) + n);
                return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            }
        } catch (ParseException e) {
            throw new RuntimeException("日期格式错误！");
        }
        return null;
    }

    //由出生日期获得年龄
    public static Integer getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return null;
        }
        int yearNow = cal.get(Calendar.YEAR);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        return yearNow - yearBirth;
    }

    /**
     * 根据日期取得星期几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    /**
     * 获取某月的某一天：
     * n=1,表示当前月
     * day=0,表示最后一天
     *
     * @return
     */
    public static String getSpecifiedDayOfMonth(int m, int n) {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, m);
        cale.set(Calendar.DAY_OF_MONTH, n);//设置为1号,当前日期既为本月第一天；0,表示最后一天
        return new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());
    }

    /**
     * 获取前几个月
     *
     * @return
     */
    public static String getSpecifiedMonth(int n) {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, n);
        return new SimpleDateFormat("yyyy-MM").format(cale.getTime());
    }

    /**
     * 当前季度的开始时间，即2018-01-1 00:00:00
     *
     * @return
     */
    public static String getCurrentSeasonStartTime() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            cal.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            cal.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            cal.set(Calendar.MONTH, 4);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            cal.set(Calendar.MONTH, 9);
        }
        cal.set(Calendar.DATE, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static String getCurrentSeasonEndTime() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            cal.set(Calendar.MONTH, 2);
            cal.set(Calendar.DATE, 31);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            cal.set(Calendar.MONTH, 5);
            cal.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            cal.set(Calendar.MONTH, 8);
            cal.set(Calendar.DATE, 30);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 23:59:59";
    }

    /**
     * 所在周开始日期
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedWeekStartDay(String specifiedDay) {
        Calendar cal = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            if (date != null) {
                cal.setTime(date);
                int d;
                if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                    d = -6;
                } else {
                    d = 2 - cal.get(Calendar.DAY_OF_WEEK);
                }
                cal.add(Calendar.DAY_OF_WEEK, d);
                return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            }
        } catch (ParseException e) {
            throw new RuntimeException("日期格式错误！");
        }
        return null;
    }

    /**
     * 所在周开始日期
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedWeekEndDay(String specifiedDay) {
        Calendar cal = Calendar.getInstance();
        try {
            Date date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
            if (date != null) {
                cal.setTime(date);
                int d;
                if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                    d = -6;
                } else {
                    d = 2 - cal.get(Calendar.DAY_OF_WEEK);
                }
                cal.add(Calendar.DAY_OF_WEEK, d);
                cal.add(Calendar.DAY_OF_WEEK, 6);
                return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            }
        } catch (ParseException e) {
            throw new RuntimeException("日期格式错误！");
        }
        return null;
    }

    /**
     * 字符串日期格式的计算
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取某月的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            return 31;
        }
    }

    /**
     * 传入日期，得到改日期所在月份的第一天
     *
     * @param date 日期
     * @return
     */
    public static String getFirstDayByDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar now = Calendar.getInstance();
            now.setTime(sdf.parse(date));
            int minDay = now.getActualMinimum(Calendar.DAY_OF_MONTH);
            now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), minDay);
            return sdf.format(now.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 传入日期，得到改日期所在月份的最后一天
     *
     * @param date 日期
     * @return
     */
    public static String getLastDayByDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar now = Calendar.getInstance();
            now.setTime(sdf.parse(date));
            int maxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
            now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), maxDay);
            return sdf.format(now.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 指定日期所在周
     * @param strDate yyyy-mm-dd
     * */
    public static String calCurrentDateOfWeek(String strDate) throws ParseException {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String data2 = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        return data1 + "_" + data2;
    }

    /**
     * 输入起止日期，返回指定日期间所有日期，包含起止日期
     * */
    public static List<String> selectDatesBetween2Date(String startDate,String endDate) throws Exception {
        List<String> retDates=new ArrayList<>();
        retDates.add(startDate);
        Integer days=DateTransform.daysOfTwo(String2Date(startDate),String2Date(endDate));
        Integer index=1;
        while (index<days){
            String date=DateTransform.beforNumDay(startDate,index);
            retDates.add(date);
            index++;
        }
        retDates.add(endDate);
        return retDates;
    }
}
