package com.jkkniugmail.rubel.tourfriend.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by islan on 1/3/2017.
 */

public class MyDate {
    String count;
    Date startDate = null;

    public String getTodayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayString;
        Date today = new Date();
        dayString = dateFormat.format(today);
        return dayString;

    }

    public String getCountDown(String date) {
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        long diff = startDate.getTime() - today.getTime();
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        if (numOfDays > 0) {
            count = String.valueOf(numOfDays) + " days left";
        } else if (numOfDays < 0) {
            count = String.valueOf(numOfDays * (-1)) + " days ago";
        } else if (numOfDays == 0) {
            count = "has started today";
        } else {
            count = "";
        }
        return count;
    }
}
