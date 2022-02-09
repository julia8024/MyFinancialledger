package com.example.myfinancialledger.MainFragment.DataView.Chart;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.Goal;
import com.example.myfinancialledger.DataBase.GoalDao;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;
import com.example.myfinancialledger.MainFragment.MainFragment;
import com.example.myfinancialledger.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class Chart_cost extends Fragment {

    private final ArrayList<BarEntry> data = new ArrayList<BarEntry>();
    private final String[] Default_SET = {"목표금액","사용금액","남은금액","입금금액"};
    private final ArrayList<String> xAxisLabel = new ArrayList<>();
    private final int[] color_form= {0x450000FF,0x45800080,0x4500FF00,0x45FFFF00};
    private final int color_red = 0x45FF0000;
    private final int color_green = 0x4500FF00;
    private final int[] result = new int[4];
    private final int ITEM_TO_VIEW = 4;
    private int save;
    private boolean insert = false;
    private MainFragment fragment_main;
    private TextView GoalRange;
    private BarChart chart;
    private Database db;
    private final Calendar cal = Calendar.getInstance();

    public Chart_cost() {
        // Required empty public constructor
    }

    public static Chart_cost newInstance(String date, MainFragment mainFragment) {
        Chart_cost fragment = new Chart_cost();
        Bundle args = new Bundle();
        fragment.fragment_main = mainFragment;
        args.putString(PagerAdapter.KEY_DATE, date);
        args.putBoolean(PagerAdapter.KEY_CREATE,false);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_chart_cost, container, false);
        chart = rootView.findViewById(R.id.chart_cost);
        db = Database.getInstance(getContext());
        GoalRange = rootView.findViewById(R.id.Goal_Range_in_chart);
        getArguments().putBoolean(PagerAdapter.KEY_CREATE,true);
        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        x.setGranularity(1f);
        update(getArguments().getString(PagerAdapter.KEY_DATE));
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
        return rootView;

    }


    public void update(String date){
        Bundle tmp =getArguments();
        tmp.putString(PagerAdapter.KEY_DATE,date);
        new getPeriodTask(Database.getInstance(getContext()).mGoalDao()).execute(getArguments().getString(PagerAdapter.KEY_DATE));
    }

    public void ViewChart(){
        data.clear();
        Default_SET[2] = "남은금액";
        color_form[2] = color_green;
        if(result[2]<0) {
            Default_SET[2] = "초과금액";
            color_form[2] = color_red;
            result[2] = -result[2];
        }
        for(int i=0;i<result.length;i++){
            data.add(new BarEntry(i*1f,result[i]));
        }
        xAxisLabel.clear();
        xAxisLabel.addAll(Arrays.asList(Default_SET));
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
        chart.setVisibleXRangeMaximum(xAxisLabel.size());
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        chart.setVisibleXRangeMinimum(xAxisLabel.size());
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
    private class getPeriodTask extends AsyncTask<String, Void, Goal >{
        private GoalDao mGoalDao;
        public getPeriodTask(GoalDao mGoalDao){
            this.mGoalDao =mGoalDao;
        }

        @Override
        protected void onPostExecute(Goal goal) {
            super.onPostExecute(goal);
            if(goal==null)
                return;
            result[0] =goal.getGoal_money();
            new getPeriodTotalTask(Database.getInstance(getContext()).mRecordDao()).execute(goal);

            if(getArguments().getBoolean(PagerAdapter.KEY_CREATE)){
                StringBuilder tmp = new StringBuilder();
                tmp.append(goal.getStart());
                tmp.append("~");
                tmp.append(goal.getEnd());
                GoalRange.setText(tmp.toString());
            }
        }

        @Override
        protected Goal doInBackground(String... strings) {
            return mGoalDao.get_now_Goal(strings[0]);
        }
    }

    private class getPeriodTotalTask extends AsyncTask<Goal,Void,int[]>{
        private RecordDao mRecordDao;

        public getPeriodTotalTask(RecordDao mRecordDao){
            this.mRecordDao =mRecordDao;
        }

        @Override
        protected void onPostExecute(int[] ints) {
            super.onPostExecute(ints);
            save = result[1];
            result[1] =ints[0];
            if(result[1]>save&&result[1]>result[0]&&insert)
                showMessage();
            insert =false;
            result[3] =ints[1];
            result[2] = result[0]-result[1];

            fragment_main.setTotal(result[2]);
            if(getArguments().getBoolean(PagerAdapter.KEY_CREATE))
                ViewChart();
        }

        @Override
        protected int[] doInBackground(Goal... goals) {
            return mRecordDao.GetTotalPeriod(goals[0]);
        }
    }

    public void setInsertTrue(){
        insert =true;
    }

    public void showMessage() {
        AlertDialog.Builder warning = new AlertDialog.Builder(fragment_main.getContext());
        warning.setTitle("경고")
                .setMessage("목표 금액치 이상 사용했습니다!!");
        warning.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = warning.create();
        dialog.show();

        //경고 팝업창 시 진동울리기
        Vibrator vibrator = (Vibrator)fragment_main.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT>=26){
            vibrator.vibrate(VibrationEffect.createOneShot(1000,10));
        }else{
            vibrator.vibrate(1000);
        }
    }
}