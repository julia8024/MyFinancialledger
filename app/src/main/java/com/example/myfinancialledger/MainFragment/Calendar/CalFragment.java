package com.example.myfinancialledger.MainFragment.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.MainActivity;
import com.example.myfinancialledger.MainFragment.MainFragment;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.MainFragment.Calendar.Keys.Keys;
import com.example.myfinancialledger.Util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalFragment extends Fragment {
    public int mCenterPosition;
    public ArrayList<Object> mCalendarList = new ArrayList<>();
    private int[] deposit = new int[32];
    private int [] withdraw = new int[32];
    private String[] tmp_date;
    private Database db;
    private int dayOfWeek;
    public TextView textView;
    public RecyclerView recyclerView;
    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;


    @Override
    public void onAttach(@NonNull Context context) {
        db =Database.getInstance(context);
        super.onAttach(context);
    }

    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_cal, container, false);

        initView(rootView);
        initSet();

        setRecycler();

        return rootView;
    }


    public void initView(View v){

        textView = (TextView)v.findViewById(R.id.title);
        recyclerView = (RecyclerView)v.findViewById(R.id.calendar);
        recyclerView.setSaveEnabled(false);
        recyclerView.setSaveFromParentEnabled(false);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), GridLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration2 =
                new DividerItemDecoration(recyclerView.getContext(), GridLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(dividerItemDecoration2);
    }

    public void initSet(){

        initCalendarList();

    }

    public void initCalendarList() {
        Calendar cal = Calendar.getInstance();
        String tmp = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1);
        setCalendarList(tmp);
    }

    private void setRecycler() {

        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new CalendarAdapter(mCalendarList);
        mAdapter.setCalendarList(mCalendarList,deposit,withdraw);
        mAdapter.setItemClickedListener(new CalendarAdapter.itemClickedListener(){
            TextView last =null;
            @Override
            public void onItemClick(CalendarAdapter.DayViewHolder holder, View view, int position) {
                if(mAdapter.getItemViewType(position)==1)
                    return;
                if(last!=null&&last.getCurrentTextColor()!=-16751104) {
                    last.setTextSize(12);
                    last.setTextColor(Color.GRAY);
                }
                last = view.findViewById(R.id.item_day);
                if(last.getCurrentTextColor()!=-16751104) {
                    last.setTextColor(0xFFFF8000);
                    last.setTextSize(16);
                }
                String selectDate = last.getText().toString();
                if(Integer.parseInt(selectDate)<10)
                    selectDate = "0"+selectDate;
                MainFragment tmp = (MainFragment)getParentFragment();
                tmp.setSelectDate("-"+selectDate);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

    }

    public void setCalendarList(String Y_M) {
        ArrayList<Object> calendarList = new ArrayList<>();
        String[] tmp = Y_M.split("-");
        int max;
        GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])-1 , 1, 0, 0, 0);
        mCenterPosition = calendarList.size();

        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        tmp_date = new String[max];
        StringBuffer param = new StringBuffer(DateUtil.getDate(calendar.getTimeInMillis(),DateUtil.DATE_FORMAT));

        for(int i=1;i<=max;i++){
            StringBuilder tmpStr = new StringBuilder();
            if(i<10)
                tmpStr.append("0");
            tmpStr.append(i);
            tmp_date[i-1] = param.replace(param.length()-2,param.length(),tmpStr.toString()).toString();
        }
        for (int j = 0; j < dayOfWeek; j++) {
            calendarList.add(Keys.EMPTY);
        }
        for (int j = 1; j <= max; j++) {
            calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
        }

        mCalendarList = calendarList;
        new totalTask(db.mRecordDao()).execute(tmp_date);
    }

    public void update(){
        new totalTask(db.mRecordDao()).execute(tmp_date);
        notify_change();
    }
    public void notify_change(){
       mAdapter.setCalendarList(mCalendarList,deposit,withdraw);

    }

    public String getSelectDate(){
        return "-"+mAdapter.getSelectDate();
    }

    private class totalTask extends AsyncTask<String,Void,Void> {
        private RecordDao mRecordDao;
        public totalTask(RecordDao mRecordDao){
            this.mRecordDao=mRecordDao;
        }

        @Override
        protected void onPostExecute(Void d) {
            notify_change();
            super.onPostExecute(d);
        }

        @Override
        protected Void doInBackground(String... strings) {
            for(int i=0;i<strings.length;i++) {
             withdraw[i] = mRecordDao.get_total_money(strings[i], "출금");
             deposit[i] = mRecordDao.get_total_money(strings[i], "입금");
            }
            return null;
        }
    }
}
