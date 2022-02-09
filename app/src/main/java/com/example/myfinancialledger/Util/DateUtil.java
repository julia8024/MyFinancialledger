package com.example.myfinancialledger.Util;

import com.example.myfinancialledger.DataBase.Goal;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public final static String DATE_FORMAT= "yyyy-MM-dd";
    public final static String Cal_FORMAT ="yyyy-MM";
    public final static String YEAR_FORMAT = "yyyy";
    public final static String MONTH_FORMAT = "MM";
    public final static String DAY_FORMAT = "d";
    public final static String HOUR_FORMAT = "HH";
    public final static String MIN_FORMAT = "mm";
    public final static String SEC_FORMAT = "ss";

    public static String getDate(long date, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREAN);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            return " ";
        }
    }

    public static String plusWOrM(String typeSt, boolean type, int temp, boolean adjust){

        LocalDate localDate = LocalDate.parse(typeSt);
        Calendar cal = Calendar.getInstance();
        java.sql.Date typeDate = java.sql.Date.valueOf(typeSt);
        cal.setTime(typeDate);
        if(type){
            cal.add(Calendar.DATE,temp*7);
        }else
            cal.add(Calendar.MONTH,temp);
        if(adjust)
            cal.add(Calendar.DAY_OF_MONTH,-1);

        return DateUtil.getDate(cal.getTimeInMillis(),DateUtil.DATE_FORMAT);
    }

    public static String[] getPeriodArray(String start, String end){
        LocalDate DateStart = LocalDate.parse(start);
        LocalDate DateEnd = LocalDate.parse(end);
        Period p = DateStart.until(DateEnd);
        Calendar cal = Calendar.getInstance();
        String[] result = new String[p.getDays()+1];
        java.sql.Date typeDate= java.sql.Date.valueOf(start);
        cal.setTime(typeDate);
        result[0] = DateStart.toString();
        for(int i=1;i<result.length;i++){
            cal.add(Calendar.DATE,1);
            result[i] = DateUtil.getDate(cal.getTimeInMillis(),DateUtil.DATE_FORMAT);
        }
        return result;
    }
    public static LocalDate ReturnNow(){
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
    public static String getCal_FORMAT(LocalDate d){
        StringBuilder tmp = new StringBuilder();
        tmp.append(d.getYear());
        tmp.append("-");
        if(d.getMonthValue()<10)
            tmp.append(0);
        tmp.append(d.getMonthValue());
        return  tmp.toString();
    }
}
