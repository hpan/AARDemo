package com.vondear.rxtools.utils.dateUtils;

import com.vondear.rxtools.utils.RxFormatValue;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 项目名称：TextureViewDome
 * 类描述：
 * 创建人：Jack
 * 创建时间：2017/7/18
 */
public class RxFormat {

    public RxFormat() {
        throw new RuntimeException("cannot be instantiated");
    }

    public static final String Date_Date = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_Date2 = "yyyy-MM-dd HH:mm";
    public static final String Date_Time = "HH:mm:ss";
    public static final String Date_Time_MS = "mm:ss";
    public static final String Date_Date_CH = "yyyy年MM月dd日HH时mm分ss秒";
    public static final String Date_Time_CH = "HH时mm分ss秒";
    public static final String Date = "yyyy-MM-dd";
    public static final String Date_CH = "yyyy年MM月dd日";
    public static final String Date_Month_Day = "MM月dd日";


    public static String setFormatDate(long value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Date, Locale.getDefault());
        return dateFormat.format(value);
    }


    public static String setFormatDate(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(value);
    }

    public static String setFormatDateG8(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return dateFormat.format(value);
    }

    public static String setFormatDateG(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(value);
    }

    public static String setFormatDateD8(long value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        return dateFormat.format(value);
    }


    public static String setFormatDateG8(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return dateFormat.format(date);
    }

    public static String setFormatDateG8(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return dateFormat.format(calendar.getTime());
    }

    public static String setFormatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Date, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String setFormatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String setFormatDate(Calendar calendar, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String setFormatNum(long value, String format) {
        return new DecimalFormat(format).format(value);
    }

    public static String setFormatNum(int value, String format) {
        return new DecimalFormat(format).format(value);
    }

    public static String setFormatNum(double value, String format) {
        return new DecimalFormat(format).format(value);
    }

    public static String setFormatNum(float value, String format) {
        return new DecimalFormat(format).format(value);
    }

    /**
     * 秒转换成分钟：未满一小时，只显示分钟，满小时，显示小时分钟
     */
    public static String setSec2HM(int second) {
        double min = second / 60f;
        String s = RxFormatValue.fromatUp(min, 0);
        return min < 60 ? s.length() == 1 ? "0" + s : s + "min" : String.format("%02d", (int) min / 60) + "h" + String.format("%02d", (int) min % 60) + "min";
    }

    /**
     * 秒转换成分钟：未满一小时，只显示分钟，满小时，显示小时分钟
     */
    public static String setSec2MS(int second) {
        return String.format("%02d", (int) (second / 60)) + "'" + String.format("%02d", (int) (second % 60)) + "''";
    }

    /**
     * 秒转换成分钟：未满一小时，只显示分钟，满小时，显示小时分钟
     */
    public static String setS2MS(int second) {
        return String.format("%02d", (int) (second / 60)) + ":" + String.format("%02d", (int) (second % 60));
    }


    public static Number setParseNumber(String value, String format) {
        try {
            return new DecimalFormat(format).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
