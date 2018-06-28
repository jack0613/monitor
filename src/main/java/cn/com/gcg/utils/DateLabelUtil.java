package cn.com.gcg.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jack on 2018-6-19.
 */
public class DateLabelUtil {


    /**
     * @author: Jack
     * @data: 2018-6-19 11:31
     * @param: content
     * @description: 根据传入字符串转换日期标签
     * @return：String
     */
    public static String formatdate(String content){
        if (content != null) {


            //ssdwwq 正则表达 判断是否是整数（正整数、负整数）
            Pattern patt = Pattern.compile("(\\-|)[0-9]*");

            /*Pattern p2 = Pattern.compile("【#d{1,}(\\+|-)[0-9]+#】|【#M{1,}(\\+|-)[0-9]+#】");
            Matcher m2 = p2.matcher(content);
            while (m2.find()) {
                String targetstr = m2.group();
                Calendar cal =  Calendar.getInstance();

                //ssdwwq --start 日期标签替换
                //获取是加还是减
                int jiaIndexOF = targetstr.indexOf("+")+1;
                int jianIndexOf = targetstr.indexOf("-");
                String formatstr = "";
                String day = "";
                //判断标签内容是+还是-
                if(jiaIndexOF > 1){
                    //获取是dd 还是 d or MM 还是 M
                    formatstr = targetstr.substring(2, jiaIndexOF-1);
                    day = targetstr.substring(jiaIndexOF,targetstr.length()-2);
                }else if(jianIndexOf > 1){
                    formatstr = targetstr.substring(2, jianIndexOf);
                    day = targetstr.substring(jianIndexOf,targetstr.length()-2);
                }
                Matcher isNum = patt.matcher(day);
                //判断获取的内容是不是数字
                if(isNum.matches() ){
                    if(formatstr.indexOf("M")!=-1){
                        cal.add(Calendar.MONTH, Integer.valueOf(day));
                    }else{
                        cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
                    }
                }
                //处理replace 和repalceAll 两种方法的不同
                content = content.replace(targetstr, new SimpleDateFormat(formatstr).format(cal.getTime()));
                //ssdwwq --end

            }

            }*/


            /*//ssdwwq 关联月日
            Pattern p5 = Pattern.compile("【#M{1,}d{1,}(\\+|-|)[0-9]*#】");
            Matcher m5 = p5.matcher(content);
            while (m5.find()) {
                String targetstr = m5.group();

                int jiaIndexOf = targetstr.indexOf("+")+1;
                int jianIndexOf = targetstr.indexOf("-");
                String day = "";
                //判断标签内容是+还是-
                if(jiaIndexOf > 1){
                    day = targetstr.substring(jiaIndexOf,targetstr.length()-2);
                }else if(jianIndexOf > 1){
                    day = targetstr.substring(jianIndexOf,targetstr.length()-2);
                }else {
                    day = "0";
                }
                Calendar cal =  Calendar.getInstance();
                //判断获取的内容是不是数字
                Matcher isNum = patt.matcher(day);
                if(isNum.matches() ){
                    cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
                }
                //content = content.replaceAll(targetstr, new SimpleDateFormat("M月d日").format(cal.getTime()));
                content = content.replace(targetstr, new SimpleDateFormat("M月d日").format(cal.getTime()));
            }

            //ssdwwq 关联年月日
            Pattern p15 = Pattern.compile("【#YyyyM{1,}(\\+|-|)[0-9]*#】");
            Matcher m15 = p15.matcher(content);
            while (m15.find()) {
                String targetstr = m15.group();

                int jiaIndexOf = targetstr.indexOf("+")+1;
                int jianIndexOf = targetstr.indexOf("-");
                String day = "";
                //判断标签内容是+还是-
                if(jiaIndexOf > 1){
                    day = targetstr.substring(jiaIndexOf,targetstr.length()-2);
                }else if(jianIndexOf > 1){
                    day = targetstr.substring(jianIndexOf,targetstr.length()-2);
                }else {
                    day = "0";
                }
                Calendar cal =  Calendar.getInstance();
                //判断获取的内容是不是数字
                Matcher isNum = patt.matcher(day);
                if(isNum.matches() ){
                    cal.add(Calendar.MONTH, Integer.valueOf(day));
                }
                //content = content.replaceAll(targetstr, new SimpleDateFormat("M月d日").format(cal.getTime()));
                content = content.replace(targetstr, new SimpleDateFormat("yyyy年M月").format(cal.getTime()));
            }
*/

            /*//ssdwwq 关联年月日
            Pattern p9 = Pattern.compile("【#YyyyMm{1,}Dd{1,}(\\+|-|)[0-9]*#】");
            Matcher m9 = p9.matcher(content);
            while (m9.find()) {
                String targetstr = m9.group();

                int jiaIndexOf = targetstr.indexOf("+")+1;
                int jianIndexOf = targetstr.indexOf("-");
                String day = "";
                //判断标签内容是+还是-
                if(jiaIndexOf > 1){
                    day = targetstr.substring(jiaIndexOf,targetstr.length()-2);
                }else if(jianIndexOf > 1){
                    day = targetstr.substring(jianIndexOf,targetstr.length()-2);
                }else {
                    day = "0";
                }
                Calendar cal =  Calendar.getInstance();
                //判断获取的内容是不是数字
                Matcher isNum = patt.matcher(day);
                if(isNum.matches() ){
                    cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
                }
                //content = content.replaceAll(targetstr, new SimpleDateFormat("M月d日").format(cal.getTime()));
                content = content.replace(targetstr, new SimpleDateFormat("yyyyMMdd").format(cal.getTime()));
            }


            //ssdwwq 添加 周
            Pattern p6 = Pattern.compile("【#week(\\+|\\-|)[0-9]*#】");
            Matcher m6 = p6.matcher(content);
            SimpleDateFormat sdf = new SimpleDateFormat("E");
            while (m6.find()) {
                String targetstr = m6.group();
                String day = "";
                if(targetstr != null && targetstr.indexOf("-") > 0){
                    day = targetstr.substring(targetstr.indexOf("-"),targetstr.length()-2);
                }else if (targetstr != null && targetstr.indexOf("+") > 0) {
                    day = targetstr.substring(targetstr.indexOf("+")+1,targetstr.length()-2);
                }else {
                    day = "0";
                }
                String eleValue = "";
                if(day != null && !day.equals("")){
                    Calendar cal = Calendar.getInstance();
                    Matcher isNum = patt.matcher(day);//判断获取的内容是不是数字
                    if(isNum.matches() ){
                        cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
                    }
                    eleValue = sdf.format(cal.getTime()).substring(2, 3);
                }else {
                    eleValue = sdf.format(new Date()).substring(2, 3);
                }
                content = content.replace(targetstr, eleValue);

            }


            //ssdwwq 添加 旬+ or 旬- or 旬
            Pattern p7 = Pattern.compile("【#ten(\\+|\\-|)[0-9]*#】|【#ten#】");
            Matcher m7 = p7.matcher(content);
            SimpleDateFormat sdfd = new SimpleDateFormat("d");
            while (m7.find()) {
                String targetstr = m7.group();
                String day = "";
                if(targetstr != null && targetstr.indexOf("-") > 0){
                    day = targetstr.substring(targetstr.indexOf("-"),targetstr.length()-2);
                }else if (targetstr != null && targetstr.indexOf("+") > 0) {
                    day = targetstr.substring(targetstr.indexOf("+")+1,targetstr.length()-2);
                }else {
                    day = "0";
                }
                String eleValue = "";
                if(day != null && !day.equals("")){
                    Calendar cal = Calendar.getInstance();
                    Matcher isNum = patt.matcher(day);//判断获取的内容是不是数字
                    if(isNum.matches() ){
                        cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day) * 10 );
                    }
                    eleValue = sdfd.format(cal.getTime());
                }else {
                    eleValue = sdfd.format(new Date());
                }
                Matcher isNum = patt.matcher(eleValue);//判断获取的内容是不是数字
                String ten = "";
                if(isNum.matches() ){
                    int dayNumber = Integer.valueOf(eleValue);
                    if(dayNumber >= 1 && dayNumber <= 10){
                        ten = "上";
                    }else if(dayNumber >=11 && dayNumber <= 20){
                        ten = "中";
                    }else{
                        ten = "下";
                    }
                }
                content = content.replace(targetstr, ten);
            }

            //ssdwwq 添加 月旬+ or 月旬- or 月旬
            Pattern p8 = Pattern.compile("【#Mten(\\+|\\-|)[0-9]*#】|【#Mten#】");
            Matcher m8 = p8.matcher(content);
            while (m8.find()) {
                String targetstr = m8.group();
                String day = "";
                if(targetstr != null && targetstr.indexOf("-") > 0){
                    day = targetstr.substring(targetstr.indexOf("-"),targetstr.length()-2);
                }else if (targetstr != null && targetstr.indexOf("+") > 0) {
                    day = targetstr.substring(targetstr.indexOf("+")+1,targetstr.length()-2);
                }else {
                    day = "0";
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if(day != null && !day.equals("")){
                    Matcher isNum = patt.matcher(day);//判断获取的内容是不是数字
                    if(isNum.matches() ){
                        cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(day) * 10 );
                    }
                }
                String Mten = "";
                String mtenday = DateUtil.format("d",cal.getTime());
                int dayNumber = Integer.valueOf(mtenday);
                if(dayNumber >= 1 && dayNumber <= 10){
                    Mten = DateUtil.format("M",cal.getTime()) + "月上旬";
                }else if(dayNumber >=11 && dayNumber <= 20){
                    Mten = DateUtil.format("M",cal.getTime()) + "月中旬";
                }else{
                    Mten = DateUtil.format("M",cal.getTime()) + "月下旬";
                }
                content = content.replace(targetstr, Mten);
            }

            //ssdwwq 季节 和 季节月份
            Pattern seasonPatt = Pattern.compile("【#season#】|【#seasonMonth#】");
            Matcher seasonMat = seasonPatt.matcher(content);
            while (seasonMat.find()) {
                String targetstr = seasonMat.group();

                int month = Integer.valueOf(DateUtil.format("M",new Date()));
                String season = "";
                String seasonMonth = "";
                if(month >= 3 && month <= 5){
                    season = "春";
                    seasonMonth = "3~5";
                }else if (month >= 6 && month <= 8){
                    season = "夏";
                    seasonMonth = "6~8";
                }else if (month >= 9 && month <= 11){
                    season = "秋";
                    seasonMonth = "9~11";
                }else if (month == 12 || month <= 2){
                    season = "冬";
                    seasonMonth = "12~2";
                }
                content = content.replace("【#season#】", season);
                content = content.replace("【#seasonMonth#】", seasonMonth);

            }*/


            Date nowdate = new Date();
            Pattern p = Pattern.compile("【#y{1,}#】|【#M{1,}#】|【#d{1,}#】|【#H{1,}#】|【#m{1,}#】|【#s{1,}#】");
            Matcher m = p.matcher(content);
            while (m.find()) {
                String targetstr = m.group();
                String formatstr = targetstr.substring(2, targetstr.length()-2);
                content = content.replaceAll(targetstr, new SimpleDateFormat(formatstr).format(nowdate));
            }


            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR_OF_DAY,-8);
            Pattern p2 = Pattern.compile("【#UTy{1,}#】|【#UTM{1,}#】|【#UTd{1,}#】|【#UTH{1,}#】|【#UTm{1,}#】|【#UTs{1,}#】");
            Matcher m2 = p2.matcher(content);
            while (m2.find()) {
                String targetstr = m2.group();
                String formatstr = targetstr.substring(2, targetstr.length()-2);
                content = content.replaceAll(targetstr, new SimpleDateFormat(formatstr.replace("UT","")).format(cal.getTime()));
            }

        }
        return content;
    }

}
