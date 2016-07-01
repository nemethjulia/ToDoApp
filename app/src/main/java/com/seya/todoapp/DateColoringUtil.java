package com.seya.todoapp;


import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DateColoringUtil {

    public static void setDueDateColor(TextView etDueDate, Date dueDate) {
        etDueDate.setTextColor(ContextCompat.getColor(etDueDate.getContext(),
                isPast(dueDate) ?
                        R.color.colorRed :
                        R.color.colorPrimary));
    }

    public static boolean isPast(Date dueDate) {
        Calendar toDay = Calendar.getInstance();
        toDay.set(Calendar.HOUR_OF_DAY, 0);
        toDay.set(Calendar.MINUTE, 0);
        toDay.set(Calendar.SECOND, 0);
        toDay.set(Calendar.MILLISECOND, 0);

        return toDay.getTime().getTime() > dueDate.getTime();
    }
}
