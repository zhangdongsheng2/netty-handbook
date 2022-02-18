package server.yto.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public final static String PATTERN_YYMM = "yyyyMM";
    public final static String PATTERN_YYMMDD = "yyyyMMdd";
    public final static String PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public final static String PATTERN_YYMMDDHHMMSS = "yyMMddHHmmss";
    public static final Integer HOUR_15 = 15;

    public static String formatTodayYMD() {
        return formatYMD(new Date());
    }

    public static String formatTodayYM() {
        return formatYM(new Date());
    }

    public static String formatYM(Date curDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYMM);
        return sdf.format(curDate);
    }

    public static String formatYMD(Date curDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YYMMDD);
        return sdf.format(curDate);
    }

    public static String formatYMDHMS(Date curDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YMDHMS);
        return sdf.format(curDate);
    }

    public static Date praseDate(String curDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_YMDHMS);
        return sdf.parse(curDate);
    }

    public static Date getDateFromMills(Long secodes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(secodes);
        return cal.getTime();
    }

    public static String addHour(String curDate, int hour) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(praseDate(curDate));

        cal.add(Calendar.HOUR_OF_DAY, hour);

        return formatYMDHMS(cal.getTime());
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return DateUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        if (null == date) return null;
        if (null == format || "".equals(format)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 字符串转日期，格式为："yyyy-MM-dd HH:mm:ss"
     *
     * @param dateStr
     * @return
     */
    public static Date formatDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串转日期，格式为："yyyy-MM-dd HH:mm:ss"
     *
     * @param dateStr
     * @return
     */
    public static Date formatDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Long curTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 日期转时间戳
     *
     * @param date
     * @return
     */
    public static Long transForMillis(Date date) {
        if (date == null) return null;
        return date.getTime();
    }

    /**
     * 日期字符串转时间戳
     *
     * @param dateStr
     * @return
     */
    public static Long transForMillis(String dateStr) {
        Date date = DateUtil.formatDate(dateStr);
        return date == null ? null : DateUtil.transForMillis(date);
    }

    /**
     * 日期字符串转时间戳
     *
     * @param dateStr
     * @return
     */
    public static Long transForMillis(String dateStr, String format) {
        Date date = DateUtil.formatDate(dateStr, format);
        return date == null ? null : DateUtil.transForMillis(date);
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return DateUtil.formatDate(today);
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return DateUtil.formatDate(today, format);
    }
}