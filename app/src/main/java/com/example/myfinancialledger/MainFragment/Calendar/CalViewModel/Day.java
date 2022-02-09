package com.example.myfinancialledger.MainFragment.Calendar.CalViewModel;

import androidx.lifecycle.ViewModel;

import com.example.myfinancialledger.MainFragment.Calendar.CalFragment;
import com.example.myfinancialledger.Util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public class Day extends ViewModel {
    private String day;
    private Boolean today =false;
    public Day() {
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public void setCalendar(Calendar calendar){
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }
    public void check(Calendar calendar){
        String tmp = DateUtil.getDate(calendar.getTimeInMillis(),DateUtil.DATE_FORMAT);
        today =LocalDate.now(ZoneId.of("Asia/Seoul")).toString().equals(tmp);
    }
    public Boolean getToday(){
        return today;
    }

    
}
