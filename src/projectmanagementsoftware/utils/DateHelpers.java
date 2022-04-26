/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author david
 */
public class DateHelpers {
    public static final Calendar calendar = GregorianCalendar.getInstance();
    public static final int MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
    
    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + (long) days * MILLISECONDS_PER_DAY);
    }
    
    public static String getMonthAsString(Date date) {
        return new SimpleDateFormat("MMM").format(date);
    }
    
    public static int getDaysInMonth(Date date) {
        calendar.setTime(date);
        
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    public static Date getStartOfNextMonth(Date date) {
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToStartOfDay();
        
        return calendar.getTime();
    }
    
    public static Tuple<Date, Date> getMonthDateRange(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToStartOfDay();
        
        Date start = calendar.getTime();
        
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToStartOfDay();
        
        Date end = calendar.getTime();
        
        return new Tuple<>(start, end);
    }
    
    public static int dayDifference(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / DateHelpers.MILLISECONDS_PER_DAY);
    }
    private static void setTimeToStartOfDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
