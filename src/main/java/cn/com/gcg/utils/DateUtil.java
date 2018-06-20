package cn.com.gcg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jack on 2018-6-19.
 */
public class DateUtil {
    private int             month;
    private int             day;
    final static String     chineseNumber[]   =
            {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static SimpleDateFormat formator = new SimpleDateFormat();
    public DateUtil()
    {
    }
    public static String convertToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }
    public static String format(String pattern, Date date) {
        SimpleDateFormat formator = new SimpleDateFormat();
        formator.applyPattern(pattern);
        return formator.format(date);
    }
    public static String getMMCh(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case 0:
                return "一";
            case 1:
                return "二";
            case 2:
                return "三";
            case 3:
                return "四";
            case 4:
                return "五";
            case 5:
                return "六";
            case 6:
                return "七";
            case 7:
                return "八";
            case 8:
                return "九";
            case 9:
                return "十";
            case 10:
                return "十一";
            case 11:
                return "十二";
        }
        return "";
    }

    /**
     * 获取旬期
     * @author 谢智生
     * @return md
     */
    public static String tenDayStr() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String str = "上旬";
        if(day <= 10) str = "上旬";
        if (day > 10 && day <= 20) str = "中旬";
        if (day > 20) str = "下旬";
        return str;
    }
    /**
     * 返回月(农历)
     */
    public static String monthN() {
        Calendar cal = Calendar.getInstance();
        DateUtil d=new DateUtil();
        String lunar = d.month();
        return lunar;
    }

    /**
     * 返回日(农历)
     */
    public static String dayN() {
        Calendar cal = Calendar.getInstance();
        DateUtil d=new DateUtil();
        String lunar = d.day();
        return lunar;
    }
    /**
     * 通过定义日期,获取星期几
     * @param days
     * @return
     */
    public static String dayOfWeek(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (w)
        {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            default:
                week = "六";
                break;
        }
        return week;
    }
    /**
     * 通过定义日期,获取星期几
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (w)
        {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            default:
                week = "六";
                break;
        }
        return week;
    }

    public static String monthAndDay(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return sdf.format(cal.getTime());
    }

    public static String yearAndMonthAndDay(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return sdf.format(cal.getTime());
    }




    /**
     * 获得当前时间的前多少天
     * @param num
     * @return
     */
    public static List<String> getDateBefore(int num) {
        List<String> list = new ArrayList<String>();
        for (int k = 0; k < num; k++) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            GregorianCalendar newCal = new GregorianCalendar(year, month, day);
            long milSec = newCal.getTimeInMillis() - k * 24 * 3600 * 1000;
            GregorianCalendar other = new GregorianCalendar();
            other.setTimeInMillis(milSec);
            String newYear = String.valueOf(other.get(GregorianCalendar.YEAR));
            String newMonth = String.valueOf(other.get(GregorianCalendar.MONTH) + 1);
            newMonth = newMonth.length() == 1 ? "0" + newMonth : newMonth;
            String newDay = String.valueOf(other.get(GregorianCalendar.DAY_OF_MONTH));
            newDay = newDay.length() == 1 ? "0" + newDay : newDay;
            String date = newYear + "-" + newMonth + "-" + newDay;
            list.add(date);
        }
        return list;
    }
    /***
     * 日期和时间操作工具类
     * @author 黄小勇 2011-5-11
     */
    /**
     * 取得某天相加(减)后的那一天
     * @param date
     * @param num(可正可负)
     * @return
     */
    public static Date getAnotherDate(Date date, int num) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, num);
        return c.getTime();
    }
    /**
     * 获取今天的前几天的日期，days>0 为后几天，返回几号
     * @param days
     * @return md
     */
    public static String previousDayWithMonth(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return formatDate(cal.getTime(), "M月d");
    }
    /**
     * 获取本月的前几月，days>0 为后几月，返回月份
     * @param months
     * @return
     */
    public static int previousMonth(int months) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, months);
        return cal.get(Calendar.MONTH) + 1;
    }
    public static String getDateStr(Date date, String str)
    {
        SimpleDateFormat format = new SimpleDateFormat(str);
        String returnStr = "";
        try
        {
            returnStr = format.format(date);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnStr;
    }
    /**
     *按days天数前推后推多少天
     * @param days
     * @return
     */
    public static String getDateByArg(int days){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());   //设置当前日期
        c.add(Calendar.DATE, days); //日期加天数
        Date date = c.getTime(); //结果
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static String formatDateForFileName(String patten) {
        Date d = new Date();
        return formatDate(d, patten);
    }

    public static java.sql.Timestamp newTime() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }
    public static String getAfterTime(String datatime, String str, int i)
    {
        SimpleDateFormat format = new SimpleDateFormat(str);
        String returnStr = "";
        Calendar cal = Calendar.getInstance();
        try
        {
            cal.setTime(format.parse(datatime));
            cal.add(11, i);
            Date returndate = cal.getTime();
            returnStr = format.format(returndate);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnStr;
    }

    public static Date[] getDates(Date beginDate, int days) {
        if (beginDate == null)
            beginDate = new Date();
        Date[] dates = new Date[days];
        Calendar now = Calendar.getInstance();
        now.setTime(beginDate);
        for (int i = 0; i < days; i++) {
            int day = now.get(Calendar.DAY_OF_YEAR);
            if (i == 0) {
                now.set(Calendar.DAY_OF_YEAR, day);
            }else {
                now.set(Calendar.DAY_OF_YEAR, day + 1);
            }
            dates[i] = now.getTime();
        }
        return dates;
    }
    /**
     *
     * formatDate:时间数据转换为字符串
     *
     * @author
     * @param date
     * @return
     * @since  ssd　Ver 1.1
     */
    public static String formatDate(Date date){
        return formatDate(date,"yyyy-MM-dd HH:mm:ss");
    }
    /**
     *
     * formatDate:将时间按指定格式转换为字符串
     *
     * @author
     * @param date
     * @param patten
     * @return
     * @since  ssd　Ver 1.1
     */
    public static String formatDate(Date date, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(date);
    }
    public static Date getAnotherDateByDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }
    /**
     * 获取12个月起始结束日期
     * getMonths:
     *
     * @author 关超
     * @param year
     * @return
     * @throws ParseException
     * @since  ssd　Ver 1.1
     */
    public static List<Map<String, List<String>>> getMonths(String year) throws ParseException{
        List<Map<String,List<String>>> resultList = new ArrayList<Map<String,List<String>>>();
        SimpleDateFormat fds = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < 13; i++) {
            Map<String,List<String>> monthMap = new LinkedHashMap<String, List<String>>();
            List<String> list = new ArrayList<String>();
            Map map = getFirstday_Lastday_Month(fds.parse(year+"-"+i+"-"+"01"));
            list.add(String.valueOf(map.get("first")));
            list.add(String.valueOf(map.get("last")));
            monthMap.put(i+"月份", list);
            resultList.add(monthMap);
        }
        return resultList;
    }

    public static String getAnotherDateByDayStr(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }
    /**
     * 某一个月第一天和最后一天
     * @param date
     * @return
     */
    public static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 0);
        Date theDate = calendar.getTime();

        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }
    public static Date parseDate(String pattern, String date) throws ParseException {
        DateUtil.formator.applyPattern(pattern);
        return DateUtil.formator.parse(date);
    }
    /**
     * 取得某一年共有多少周
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }
    /**
     * 取得某天是一年中的多少周
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 获取任一年所有星期的起始结束时间
     * getWeeks:
     *
     * @author 关超
     * @param year
     * @param weeks
     * @return
     * @since  ssd　Ver 1.1
     */
    public static List<Map<String, List<String>>> getWeeks(int year,int weeks){
        SimpleDateFormat fds = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String,List<String>>> resultList = new ArrayList<Map<String,List<String>>>();
        for (int i = 1; i <= weeks; i++) {
            List<String> list = new ArrayList<String>();
            Map<String,List<String>> weekMap = new LinkedHashMap<String, List<String>>();
            list.add(fds.format(getFirstDayOfWeek(year,i)));
            list.add(fds.format(getLastDayOfWeek(year,i)));
            weekMap.put("第"+i+"周", list);
            resultList.add(weekMap);
        }
        return resultList;
    }
    /**
     * 取得某年某周的最后一天
     * 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周,2009-01-05为2009年第一周的第一天
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(year, 0, 7);
        Date LastDate = getFirstDayOfWeek(calFirst.getTime());
        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(LastDate);
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
        LastDate = getFirstDayOfWeek(cal.getTime());
        Calendar lastDateCal = Calendar.getInstance();
        lastDateCal.setTime(LastDate);
        lastDateCal.add(Calendar.DATE, -1);
        return lastDateCal.getTime();
    }
    /**
     * 获取当年所有旬
     * getXuns:
     *
     * @author 关超
     * @param year
     * @return 旬起始结束时间数组
     * @since  ssd　Ver 1.1
     */
    public static List<Map<String, List<String>>> getXuns(String year) {
        List<Map<String,List<String>>> resultList = new ArrayList<Map<String,List<String>>>();
        for(int i = 1 ; i <= 12 ; i++){
            SimpleDateFormat fds = new SimpleDateFormat("yyyy-MM-dd");
            try {
                List<String> list = new ArrayList<String>();
                Map<String,List<String>> monthMap = new LinkedHashMap<String, List<String>>();
                String date0 = year+"-"+i+"-"+"01";
                String date1 = year+"-"+i+"-"+"10";
                String date2 = year+"-"+i+"-"+"11";
                String date3 = year+"-"+i+"-"+"20";
                String date4 = year+"-"+i+"-"+"21";
                Map map = getFirstday_Lastday_Month(fds.parse(year+"-"+i+"-"+"10"));
                String date5 = (String) map.get("last");
                list.add(date0);
                list.add(date1);
                monthMap.put("第"+i+"月上旬", list);
                resultList.add(monthMap);
                monthMap = new LinkedHashMap<String, List<String>>();
                list = new ArrayList<String>();
                list.add(date2);
                list.add(date3);
                monthMap.put("第"+i+"月中旬", list);
                resultList.add(monthMap);
                list = new ArrayList<String>();
                monthMap = new LinkedHashMap<String, List<String>>();
                list.add(date4);
                list.add(date5);
                monthMap.put("第"+i+"月下旬", list);
                resultList.add(monthMap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
    /**
     * 获取当前年份的季度
     * getQuarter:
     *
     * @author 关超
     * @param year
     * @return
     * @since  ssd　Ver 1.1
     */
    public static List<Map<String, List<String>>> getQuarter(String year){
        List<String> list = new ArrayList<String>();
        Map<String,List<String>> quarterMap = new LinkedHashMap<String, List<String>>();
        List<Map<String,List<String>>> resultList = new ArrayList<Map<String,List<String>>>();
        list.add(year+"-01-01");
        list.add(year+"-03-31");
        quarterMap.put("第1季度", list);
        resultList.add(quarterMap);
        list = new ArrayList<String>();
        quarterMap = new LinkedHashMap<String, List<String>>();
        list.add(year+"-04-01");
        list.add(year+"-06-30");
        quarterMap.put("第2季度", list);
        resultList.add(quarterMap);

        list = new ArrayList<String>();
        quarterMap = new LinkedHashMap<String, List<String>>();
        list.add(year+"-07-01");
        list.add(year+"-09-30");
        quarterMap.put("第3季度", list);
        resultList.add(quarterMap);
        list = new ArrayList<String>();
        quarterMap = new LinkedHashMap<String, List<String>>();
        list.add(year+"-10-01");
        list.add(year+"-12-31");
        quarterMap.put("第4季度", list);
        resultList.add(quarterMap);
        return resultList;
    }
    /**
     * 获取当前年的前5年后5年
     * getYears:
     *
     * @author 关超
     * @return
     * @since  ssd　Ver 1.1
     */
    public static List<Map<String,Object>> getYears() {
        List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String, Object>();
        Integer year = Integer.valueOf(DateUtil.getStringDateShort().substring(0, 4));
        map.put("name", String.valueOf(year-5));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year-4));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year-3));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year-2));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year-1));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year-0));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year+1));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year+2));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year+3));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year+4));
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("name", String.valueOf(year+5));
        list.add(map);
        return list;
    }
    /**
     * 获取当前日期
     * @return 格式：yyyy年MM月dd日
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 取得某年某周的第一天
     * 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周,2009-01-05为2009年第一周的第一天
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(year, 0, 7);
        Date firstDate = getFirstDayOfWeek(calFirst.getTime());
        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(firstDate);
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (week - 1) * 7);
        firstDate = getFirstDayOfWeek(cal.getTime());
        return firstDate;
    }
    /**
     * 取得某天所在周的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }
    public static String dateToString(Date date, String fromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);
        return sdf.format(date);
    }

    public static Date getAnotherDateByHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public static String dateFormate(String partten,Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(partten);
        return sdf .format(date);
    }


    public String month(){
        return chineseNumber[month - 1];
    }
    public String day(){
        return getChinaDayString(day);
    }
    public static String getChinaDayString(int day) {
        String chineseTen[] =
                {"初", "十", "廿", "卅"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + chineseNumber[n];
    }

    /**
     * 获取今天的前几天的日期，days>0 为后几天，返回几号
     * @param days
     * @return
     */
    public static int previousDay(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
