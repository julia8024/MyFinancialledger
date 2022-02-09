package com.example.myfinancialledger.MainFragment.DataView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myfinancialledger.MainFragment.DataView.Chart.Chart_cost;
import com.example.myfinancialledger.MainFragment.DataView.Chart.Chart_time;
import com.example.myfinancialledger.MainFragment.DataView.List.DailyRecordFragment;
import com.example.myfinancialledger.MainFragment.MainFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private Fragment[] data = new Fragment[3];
    public static final String KEY_CREATE = "Create?";
    public static final String KEY_DATE ="Date";
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, String date, MainFragment main) {
        super(fragmentActivity);
        data[0] = Chart_time.newInstance(date);
        data[1] = DailyRecordFragment.getInstance(date);
        data[2] = Chart_cost.newInstance(date,main);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return data[position];
    }

    @Override
    public int getItemCount() {
        return 3;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void update(String date){
        Chart_time chart_time = (Chart_time)data[0];
        DailyRecordFragment dailyRecordFragment = (DailyRecordFragment)data[1];
        Chart_cost chart_cost = (Chart_cost)data[2];
        chart_cost.update(date);
        if(!chart_time.getArguments().getBoolean("Create?"))
            return;
        chart_time.update(date,true);
        if(!dailyRecordFragment.getArguments().getBoolean("Create?")) {
            dailyRecordFragment.getArguments().putString("Date", date);
            chart_cost.getArguments().putString("Date",date);
            return;
        }
        dailyRecordFragment.update(date);
        dailyRecordFragment.reset();
    }

    public void setInsertTrue(){
        Chart_cost chart_cost = (Chart_cost)data[2];
        chart_cost.setInsertTrue();
    }
}
