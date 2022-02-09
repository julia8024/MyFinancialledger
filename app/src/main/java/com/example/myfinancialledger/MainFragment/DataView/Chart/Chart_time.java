package com.example.myfinancialledger.MainFragment.DataView.Chart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.Util.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Chart_time extends Fragment  {
    private final ArrayList<BarEntry> data = new ArrayList<BarEntry>();
    private final ArrayList<String> xAxisLabel = new ArrayList<String>();
    private final int[] month_data = new int[12];
    private final int[] Day_data = new int[31];
    private int[] color_form;
    private final int MAX_ITEM_TO_VIEW = 7;
    private BarChart chart;
    private final Calendar cal = Calendar.getInstance();
    private SeekBar bar;
    private change chg;

    public Chart_time(){ }

    public static Chart_time newInstance(String date){
        Chart_time fragment = new Chart_time();
        Bundle args = new Bundle();
        args.putString(PagerAdapter.KEY_DATE,date);
        args.putBoolean(PagerAdapter.KEY_CREATE,false);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup RootView = (ViewGroup)inflater.inflate(R.layout.fragment_chart_time,container,false);
        chart = RootView.findViewById(R.id.chart_time);
        bar = RootView.findViewById(R.id.seekBar);
        getArguments().putBoolean(PagerAdapter.KEY_CREATE,true);
        Button D = RootView.findViewById(R.id.D);
        Button M = RootView.findViewById(R.id.M);
        Date d = Date.valueOf(getArguments().getString(PagerAdapter.KEY_DATE));
        cal.setTime(d);
        chg = new change();
        D.setOnClickListener(chg);
        M.setOnClickListener(chg);
        label_set(Calendar.DAY_OF_MONTH);
        chart_set(Calendar.MONTH,true);
        chart_set(Calendar.DAY_OF_MONTH,true);

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        x.setGranularity(1f);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMinimum(0f);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chart.centerViewTo(progress,0, YAxis.AxisDependency.RIGHT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return RootView;
    }

    private void label_set(int type){
        xAxisLabel.clear();
        int select = cal.get(type);
        int max =  type==Calendar.DAY_OF_MONTH?cal.getActualMaximum(type):12;
        int i,count=0;
        String basic;
        if (type == Calendar.DAY_OF_MONTH) {
            basic = "D";
        }
        else {
            basic ="M";
            select++;
        }
        color_form = new int[max];
        for(i=1;i<select;i++){
            String tmp = "-"+(select-i);
            xAxisLabel.add(basic+tmp);
            color_form[count++] = 0x4500FF00;
        }
        xAxisLabel.add(basic);
        color_form[count++] = 0xF0FFFF00;
        for(i=1;i<=max-select;i++){
            String tmp = "+"+i;
            xAxisLabel.add(basic+tmp);
            color_form[count++] = 0x4500FF00;
        }
    }

    private void chart_set(int type, boolean clear){
        int max;
        int todate = cal.get(type);
        int [] tmp= month_data;
        if (type==Calendar.DAY_OF_MONTH) {
            max = cal.getActualMaximum(type);
            tmp = Day_data;
        }
        else{
            max=12;
            todate++;
        }
        data.clear();
        if(clear) {
            DataSet(type,max);
        }
        for (int i = 0; i < max; i++) {
            data.add(new BarEntry(i, tmp[i]));
        }
        BarDataSet set = new BarDataSet(data, "sdsds");
        set.setColors(color_form);
        set.setValueTextSize(10);
        BarData item = new BarData(set);
        item.setBarWidth(0.5f);
        item.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                StringBuffer tmp = new StringBuffer(super.getFormattedValue(value));
                tmp.delete(tmp.length()-2,tmp.length());
                return tmp.append('￦').toString();
            }
        });
        chart.setData(item);
        chart.setVisibleXRangeMaximum(MAX_ITEM_TO_VIEW);
        chart.setVisibleXRangeMinimum(MAX_ITEM_TO_VIEW);
        chart.centerViewTo(todate-1,0, YAxis.AxisDependency.RIGHT);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        chart.setVisibleXRangeMaximum(MAX_ITEM_TO_VIEW);
        bar.setMax(max-4);
        bar.setMin(3);
        bar.setProgress(todate>4?todate-1:bar.getMin());
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private class change implements View.OnClickListener {
        private Boolean which = true;
        @Override
        public void onClick(View v) {
            int type=0;
            if(v.getId()== R.id.D){
                if(which)
                    return;
                which=true;
                type=Calendar.DAY_OF_MONTH;
            }
            else if(v.getId()== R.id.M){
                if(!which)
                    return;
                which=false;
                type=Calendar.MONTH;
            }
            label_set(type);
            chart_set(type, false);
        }

        public void set_which(boolean which){
            this.which= which;
        }
    }

    @Override
    public void onResume() {
        label_set(Calendar.DAY_OF_MONTH);
        chart_set(Calendar.DAY_OF_MONTH,false);
        chg.set_which(true);
        super.onResume();
    }

    public void update(String date,boolean clear){
        Date d = Date.valueOf(date);
        cal.setTime(d);
        label_set(Calendar.DAY_OF_MONTH);
        chart_set(Calendar.MONTH, clear);
        chart_set(Calendar.DAY_OF_MONTH, clear);
        chg.set_which(true);
    }

    private void DataSet(int type, int max){
        Database db = Database.getInstance(getContext());
        java.util.Date date = cal.getTime();
        String Value_Format = DateUtil.getDate(cal.getTimeInMillis(),DateUtil.DATE_FORMAT);
        StringBuffer value = new StringBuffer(Value_Format);
        String[] param = new String[max];
        int [] save = Day_data;
        int chg_index = 8;
        if(type==Calendar.MONTH){
            chg_index =5;
            value.replace(7,10,"%");
            save = month_data;
        }
        for(int i=1;i<=max;i++){
            String tmp =Integer.toString(i);
            if(i<10)
                tmp = "0"+tmp;
            param[i-1]=value.replace(chg_index,chg_index+2,tmp).toString();
        }
        new totalTask(db.mRecordDao()).execute(param);
    }

    private class totalTask extends AsyncTask<String,Void,Integer>{
        private RecordDao mRecordDao;
        public totalTask(RecordDao mRecordDao){
            this.mRecordDao=mRecordDao;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            int type = Calendar.DAY_OF_MONTH;
            if(integer<15)
                type=Calendar.MONTH;
            chart_set(type,false);
            super.onPostExecute(integer);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int[] tmp = strings.length<15? month_data:Day_data;
            for(int i=0;i<strings.length;i++) {
                tmp[i] = mRecordDao.get_total_money(strings[i],"출금");
            }
            return tmp.length;
        }
    }
}