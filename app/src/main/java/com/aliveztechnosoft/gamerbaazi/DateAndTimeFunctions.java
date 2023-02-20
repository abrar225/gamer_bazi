package com.aliveztechnosoft.gamerbaazi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTimeFunctions {

    public static Long convertToTimestamps(String dateTime, String format) {
        String newDate = dateTime;

        if (format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        if (newDate.contains("-")) {
            newDate = getCurrentDate("") + " " + newDate;
        }

        if (newDate.contains(":")) {
            newDate = newDate + " 00:00:00";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date newFormatDate = dateFormat.parse(newDate);
            if (newFormatDate != null) {
                return newFormatDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static String convertToDate(long timestamps, String format) {
        final SimpleDateFormat date = new SimpleDateFormat(format, Locale.getDefault());
        return date.format(new Date(timestamps));
    }

    public static String getCurrentDate(String format) {
        if (format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    public static String getCurrentTime(String format) {
        if (format.isEmpty()) {
            format = "HH:mm:ss";
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDateTime(String format) {
        if (format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date());
    }

    public static Long getDaysBetweenDates(String start, String end) {
        final long difference = convertToTimestamps(end, "") - convertToTimestamps(start, "");
        return Math.abs((difference / (24 * 60 * 60 * 1000)));
    }

    public static Long getMinutesBetweenTimes(String start, String end) {
        final long difference = convertToTimestamps(end, "") - convertToTimestamps(start, "");
        return Math.abs((difference / (60 * 1000)));
    }

    public static String addDaysInDate(String dateString, int days) {
        final Calendar cal = Calendar.getInstance();
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final Date dateObj = dateFormat.parse(dateString);

            if (dateObj != null) {
                cal.setTime(dateObj);

                // use add() method to add the days to the given date
                cal.add(Calendar.DAY_OF_MONTH, days);
                return dateFormat.format(cal.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String subtractDaysFromDate(String dateString, int days) {
        final Calendar cal = Calendar.getInstance();
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final Date dateObj = dateFormat.parse(dateString);

            if (dateObj != null) {
                cal.setTime(dateObj);

                // use add() method to add the days to the given date
                cal.add(Calendar.DATE, -days);
                return dateFormat.format(cal.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String changeFormat(String dateTime, String oldFormat, String newFormat) {

        if(oldFormat.isEmpty()){
            oldFormat = "yyyy-MM-dd HH:mm:ss";
        }

        final SimpleDateFormat oldSDF = new SimpleDateFormat(oldFormat, Locale.getDefault());

        try {
            final Date oldDate = oldSDF.parse(dateTime);

            if (oldDate != null) {
                final SimpleDateFormat newSDF = new SimpleDateFormat(newFormat, Locale.getDefault());
                return newSDF.format(oldDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String convert24HoursTo12Hours(String timeIn24Format) {
        return changeFormat(timeIn24Format, "HH:mm:ss", "hh:mm:ss a").toUpperCase();
    }
}
