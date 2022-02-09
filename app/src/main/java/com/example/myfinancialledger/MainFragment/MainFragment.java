package com.example.myfinancialledger.MainFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.Record;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.MainActivity;
import com.example.myfinancialledger.MainFragment.Calendar.CalFragment;
import com.example.myfinancialledger.MainFragment.Calendar.CalendarAdapter;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.MainFragment.DataView.List.DailyRecordFragment;
import com.example.myfinancialledger.MainFragment.I_OFragment.I_OFragment;
import com.example.myfinancialledger.Util.DateUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.TimeZone;

public class MainFragment extends Fragment {

    private I_OFragment FragmentI_O;
    private I_OBtListener listener = new I_OBtListener();
    private CalFragment mCalFragment;
    private PagerAdapter pagerAdapter;
    private String now;
    private ViewPager2 pager2;
    private TextView Total;
    private TextView selectDate;

    public static MainFragment newInstance(String date) {
        MainFragment m = new MainFragment();
        Bundle arg = new Bundle();
        arg.putString("Date", date);
        m.setArguments(arg);
        return m;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        now = DateUtil.ReturnNow().toString();
        mCalFragment = (CalFragment)getChildFragmentManager().findFragmentById(R.id.calendar);
        getChildFragmentManager().beginTransaction().replace(R.id.calendar,mCalFragment);

        pager2 = rootView.findViewById(R.id.pager);
        pager2.setSaveEnabled(false);
        pagerAdapter = new PagerAdapter(getActivity(),now,this);
        FragmentI_O = I_OFragment.newInstance(pagerAdapter, mCalFragment);
        pager2.setAdapter(pagerAdapter);
        selectDate = rootView.findViewById(R.id.Date);
        selectDate.setText(now);
        pagerAdapter.update(now);
        Button dButton = (Button) rootView.findViewById(R.id.depositButton);
        dButton.setOnClickListener(listener);
        Button wButton = (Button) rootView.findViewById(R.id.withdrawalButton);
        wButton.setOnClickListener(listener);
        Total = rootView.findViewById(R.id.total);
        return rootView;
    }

    public void update_cal(String D) {
        mCalFragment.setCalendarList(D);
        mCalFragment.notify_change();
    }

    private class I_OBtListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.depositButton)
                FragmentI_O.change_arg(true, selectDate.getText().toString());
            else
                FragmentI_O.change_arg(false, selectDate.getText().toString());
            FragmentI_O.show(getFragmentManager(), I_OFragment.TAG_EVENT_DIALOG);
        }
    }


    public void setSelectDate(String date) {
        MainActivity ds = (MainActivity) getContext();
        selectDate.setText(ds.getSpinnerDate() + date);
        pagerAdapter.update(selectDate.getText().toString());
    }

    public PagerAdapter getPagerAdapter(){
        return pagerAdapter;
    }

    public CalFragment getCalFragment(){
        return mCalFragment;
    }


    public String getSelectDate(){
        return selectDate.getText().toString();
    }
    public interface SpinnerDate {
        public String getSpinnerDate();
    }

    public void setTotal(int value){
        StringBuilder text = new StringBuilder();
        String tmp = "";
        if(value<0)
            Total.setTextColor(0x90FF0000);
        else if(value>0){
            Total.setTextColor(0x9000FF00);
            tmp = "+";
        }else {
            Total.setTextColor(Color.BLACK);
        }
        text.append(tmp);
        text.append(value);
        text.append(getResources().getString(R.string.won));
        Total.setText(text.toString());
    }
}
