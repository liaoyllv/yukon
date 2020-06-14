package com.lyl.yukon.common.util;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * date工具类
 *
 * @author xc
 */
public class DateUtils {

    /**
     * 获取某段时间内的周一（二等等）的日期
     *
     * @param startData 开始日期
     * @param endData   结束日期
     * @param weekDays  获取周几，1－6代表周一到周六。0代表周日,多个用逗号隔开
     * @param holiday   节假日，多天用逗号隔开
     * @return 返回日期List
     */
    public static List<Date> getDayOfWeekWithinDateInterval(Date startData, Date endData, String weekDays, String holiday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataBegin = sdf.format(startData);
        String dataEnd = sdf.format(endData);
        List<Date> dateResult = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String[] dateInterval = {dataBegin, dataEnd};
        Date[] dates = new Date[dateInterval.length];
        for (int i = 0; i < dateInterval.length; i++) {
            String[] ymd = dateInterval[i].split("[^\\d]+");
            cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));
            dates[i] = cal.getTime();
        }
        for (Date date = dates[0]; date.compareTo(dates[1]) <= 0; ) {
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
            StringBuffer weekNum = new StringBuffer(Integer.toString(cal.get(Calendar.DAY_OF_WEEK) - 1));
            if (weekDays.contentEquals(weekNum)) {
                if (StringUtils.isBlank(holiday) || (!holiday.contains(sdf.format(date)))) {
                    dateResult.add(date);
                }
            }
        }
        return dateResult;
    }

    /**
     * 获取时间段内所有日期，并组合成字符串
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getBetweenTime(Date startDate, Date endDate) {
        StringBuffer betweenTime = new StringBuffer();
        try {
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar sCalendar = Calendar.getInstance();
            sCalendar.setTime(startDate);
            int year = sCalendar.get(Calendar.YEAR);
            int month = sCalendar.get(Calendar.MONTH);
            int day = sCalendar.get(Calendar.DATE);
            sCalendar.set(year, month, day, 0, 0, 0);
            Calendar eCalendar = Calendar.getInstance();
            eCalendar.setTime(endDate);
            year = eCalendar.get(Calendar.YEAR);
            month = eCalendar.get(Calendar.MONTH);
            day = eCalendar.get(Calendar.DATE);
            eCalendar.set(year, month, day, 0, 0, 0);
            while (sCalendar.before(eCalendar)) {
                betweenTime.append(outFormat.format(sCalendar.getTime()));
                sCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            betweenTime.append(outFormat.format(eCalendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return betweenTime.toString();
    }

    public static Date addDays(Date day, int addDays) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            c.add(Calendar.DAY_OF_MONTH, addDays);// 今天+1天
            Date tomorrow = c.getTime();
            String endDayStr = f.format(tomorrow);
            return f.parse(endDayStr);
        } catch (Exception e) {
            return null;
        }
    }
}
