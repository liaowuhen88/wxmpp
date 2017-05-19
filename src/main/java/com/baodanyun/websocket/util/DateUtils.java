package com.baodanyun.websocket.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 文件名：DateUtils.java 日期处理相关工具类
 * 版本信息：V1.0
 * 日期：2013-03-11
 */
public class DateUtils {

    /**
     * 定义常量*
     */

    public static final String DATE_JFP_STR    = "yyyyMM";
    public static final String DATE_HOURS_STR  = "yyyyMMdd";
    public static final String DATE_SMALL_STR  = "yyyy-MM-dd";
    public static final String DATE_FULL_TRIM1 = "yyyyMMddHH";
    public static final String DATE_CHINSE_STR = "yyyy年MM月dd";
    public static final String DATE_KEY_STR    = "yyMMddHHmmss";
    public static final String DATE_FULL_TRIM  = "yyyyMMddHHmmss";
    public static final String DATE_FULL_STR   = "yyyy-MM-dd HH:mm:ss";



    public static final String DATE_WEEK[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        Date result = parse(strDate, DATE_FULL_STR);
        if(result == null){
            result = parse(strDate,DATE_SMALL_STR);
        }
        return result;
    }

    public static Date parseFrmChnse(String strDate) {
        return parse(strDate, DATE_CHINSE_STR);
    }


    public static String format(Date date) {
        return format(date, DATE_FULL_STR);
    }

    public static String format(Date date, String format) {
        DateFormat dateFormat = getDateFormat(format);
        if (date != null) {
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两个时间比较
     *
     * @param date1
     * @return
     */
    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    /**
     * 两个时间比较(时间戳比较)
     *
     * @param date1
     * @return
     */
    public static int compareDateWithNow(long date1) {
        long date2 = dateToUnixTimestamp();
        if (date1 > date2) {
            return 1;
        } else if (date1 < date2) {
            return -1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(getDayWeek(new Date()));
    }

    /**
     * 计算年龄
     *
     * @param birthDate
     * @return
     */
    public static int calcAge(Date birthDate) {
        Date nowDate = new Date();
        Calendar flightCal = Calendar.getInstance();
        flightCal.setTime(nowDate);
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);

        int y = flightCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        int m = flightCal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH);
        int d = flightCal.get(Calendar.DATE) - birthCal.get(Calendar.DATE);
        if (y < 0) {
            throw new RuntimeException("您老还没出生");
        }

        if (m < 0) {
            y--;
        }
        if (m >= 0 && d < 0) {
            y--;
        }

        return y;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    /**
     * 获取系统当前计费期
     *
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     *
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }


    /**
     * 将Unix时间戳转换成日期
     *
     * @param timestamp
     * @return
     */
    public static String unixTimestampToDate(long timestamp) {
        return unixTimestampToDate(DATE_FULL_STR, timestamp);
    }

    /**
     * 将Unix时间戳转换成日期
     *
     * @param timestamp
     * @return
     */
    public static String unixTimestampToDate(String dateFormat, long timestamp) {

        SimpleDateFormat sd = new SimpleDateFormat(dateFormat);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }


    public static String formatDateYYYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyDDmmHH");
        return sdf.format(new Date());
    }

    public static String formatDateYYYMMDDSS() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSS");
        return sdf.format(new Date());
    }

    public static String formatDateYYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    private static DateFormat getDateFormat(String dateFormatStr) {
        return new SimpleDateFormat(dateFormatStr);
    }

    /**
     * 上周开始时间
     *
     * @return
     */
    public static Date getLastWeekStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return calendar.getTime();
    }

    /**
     * 上周结束时间
     *
     * @return
     */
    public static Date getLastWeekEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获得前days天当天的开始时间
     *
     * @param days
     * @return
     */
    public static Date getBeforeStartTime(int days) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获得前days天当天的结束时间
     *
     * @param days
     * @return
     */
    public static Date getBeforeEndTime(int days) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -days + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date getBeforeEndTime(Date beforeTime, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beforeTime);
        calendar.add(Calendar.DAY_OF_YEAR, days + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 当天的开始时间
     * @param day
     * @return
     */
    public static Date getDayStartTime(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 当天的结束时间
     * @param day
     * @return
     */
    public static Date getDayEndTime(Date day) {
        return getBeforeEndTime(day,0);
    }

    /**
     * 距今几天后的日期
     * @param days
     * @return
     */
    public static Date getAfterTime(int days) {
        return getAfterTime(new Date(),days);
    }

    /**
     * 距startTime几天后的日期
     * @param days
     * @return
     */
    public static Date getAfterTime(Date startTime, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date getAfterStartTime(Date startTime, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date getAfterEndTime(Date startTime, int days) {
        return getAfterStartTime(getAfterStartTime(startTime,days),1);
    }



    /**
     * 获得明天的开始时间
     *
     * @return
     */
    public static Date getTomorrowStartTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获得明天的结束时间
     *
     * @return
     */
    public static Date getTomorrowEndTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获得上月的开始时间
     *
     * @return
     */
    public static Date getLastMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得上月的结束时间
     *
     * @return
     */
    public static Date getLastMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得昨天的开始时间
     *
     * @return
     */
    public static Date getYesterdayStartTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获得昨天的结束时间
     *
     * @return
     */
    public static Date getYestadyEndTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    //星期日是7
    public static int getDayWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index == 0) {
            week_index = 7;
    }
        return week_index;
    }

    /**
     * 根据当前日期得到星期几
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return DATE_WEEK[week_index];
    }

    /**
     * 获取前多少天日期.......day前多少天
     * 当前日期-7天的时间
     *
     * @return
     */
    public static String getSevenDay(Integer day) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_SMALL_STR);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -day);
        Date monday = c.getTime();
        return sdf.format(monday);
    }

    /**
     * 获取最近的已经过去的周几 （用1-7表示）
     */
    public static Date getLastDayOfWeek(int dayOfWeek) {
        if (dayOfWeek == 7) {
            dayOfWeek = 1;
        } else {
            dayOfWeek += 1;
        }
        int i = -1;
        Calendar calendar = Calendar.getInstance();
        while (true) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                return calendar.getTime();
            }
            calendar.add(Calendar.DAY_OF_WEEK, i);
        }
    }

    /**
     * 距离某个日期还有多少天
     * @param deadline
     * @return
     */
    public static int getLeftDays(Date deadline){
        int result = 0;

        Date now = new Date();
        if(deadline.after(now)){
            Calendar calendar = Calendar.getInstance();
            int day1 = calendar.get(Calendar.DAY_OF_YEAR);

            calendar.setTime(deadline);
            int day2 = calendar.get(Calendar.DAY_OF_YEAR);

            result = day2 -day1;
}
        return result;
    }
    /**
     * 制定日期和当前时间天数差
     */
    public static Long getDayDiff(Date d) {
        //当前时间
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //给定时间处理
        Calendar setCal = Calendar.getInstance();
        setCal.setTime(d);
        setCal.set(Calendar.HOUR_OF_DAY, 0);
        setCal.set(Calendar.MINUTE, 0);
        setCal.set(Calendar.SECOND, 0);
        setCal.set(Calendar.MILLISECOND, 0);
        return (setCal.getTimeInMillis() - cal.getTimeInMillis()) / (1000 * 60 * 60 * 24);
    }
    //计算2个指定时间的天数差
    public static Long getDayDiff(Date d1,Date d2) {
        Calendar setCal1 = Calendar.getInstance();
        Calendar setCal2 = Calendar.getInstance();
        setCal1.setTime(d1);
        setCal2.setTime(d2);
        setCal1.set(Calendar.HOUR_OF_DAY, 0);
        setCal1.set(Calendar.MINUTE, 0);
        setCal1.set(Calendar.SECOND, 0);
        setCal1.set(Calendar.MILLISECOND, 0);
        setCal2.set(Calendar.HOUR_OF_DAY, 0);
        setCal2.set(Calendar.MINUTE, 0);
        setCal2.set(Calendar.SECOND, 0);
        setCal2.set(Calendar.MILLISECOND, 0);
        return (setCal1.getTimeInMillis() - setCal2.getTimeInMillis()) / (1000 * 60 * 60 * 24);
    }
    public static Date getHourDate(Date day,int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }
}
