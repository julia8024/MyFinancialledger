package com.example.myfinancialledger.ChartFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myfinancialledger.DataBase.CustomPreferenceManager;
import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.Util.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Map;

public class Chart_Genre extends Fragment {
    private final ArrayList<BarEntry> data = new ArrayList<BarEntry>();
    private final ArrayList<String> xAxisLabel = new ArrayList<String>();
    private int[] color_form;
    private BarChart chart;
    private String[] GenreType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup RootView = (ViewGroup)inflater.inflate(R.layout.fragment_chart_genre,container,false);
        chart = RootView.findViewById(R.id.chart_genre);
        xAxisLabel.clear();
        xAxisLabel.addAll(CustomPreferenceManager.getStringSet(getContext(),CustomPreferenceManager.KEY_GENRE));
        xAxisLabel.add("기타");
        GenreType = new String[xAxisLabel.size()];
        for(int i=0; i<GenreType.length;i++){
            GenreType[i] = xAxisLabel.get(i);
        }
        color_form = new int[xAxisLabel.size()];
        for(int i=0; i<color_form.length;i++){
            color_form[i] = (int)(Math.random()*0x01000000)|0x45000000;
        }
        StringBuffer tmp = new StringBuffer(DateUtil.ReturnNow().toString());
        tmp.replace(tmp.length()-2,tmp.length(),"");
        changeRange(tmp.toString());
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

        return RootView;
    }

    public void ViewChart(Map<String,Integer> count){
        data.clear();
        for(int i=0; i<count.size();i++){
            data.add(new BarEntry(i*1f,count.get(xAxisLabel.get(i))));
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
                return tmp.toString();
            }
        });
        chart.setData(item);
        chart.setVisibleXRangeMaximum(xAxisLabel.size());
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        chart.setVisibleXRangeMinimum(xAxisLabel.size());
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    public void changeRange(String date){
        Database db = Database.getInstance(getContext());
        new GenreTask(db.mRecordDao()).execute(date+"%");
    }

    private class GenreTask extends AsyncTask<String, Void, Map<String,Integer>>{
        private RecordDao mRecordDao;

        public GenreTask(RecordDao mRecordDao){
            this.mRecordDao =mRecordDao;
        }

        @Override
        protected void onPostExecute(Map<String, Integer> stringIntegerMap) {
            super.onPostExecute(stringIntegerMap);
            if(stringIntegerMap!=null)
                ViewChart(stringIntegerMap);

        }

        @Override
        protected Map<String,Integer> doInBackground(String... strings) {
            return mRecordDao.GetCountGenre(strings[0],GenreType);
        }
    }

}
