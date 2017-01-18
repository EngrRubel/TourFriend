package com.jkkniugmail.rubel.tourfriend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by islan on 12/20/2016.
 */

public class TimeStamp {
    public static String getDateString(long unixSeconds){
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String getTimeString(long unixSeconds){
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String timeString = sdf.format(date);
        return timeString;
    }
}
