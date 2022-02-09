package com.example.myfinancialledger.DataBase;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.appcompat.app.AlertDialog;

import com.example.myfinancialledger.MainFragment.Calendar.CalFragment;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;

import java.util.Calendar;

public class ChangeAsyncTask extends AsyncTask<Record, Void, Void> {
    private RecordDao mRecordDao;
    private PagerAdapter mPagerAdapter;
    public static final int INSERT = 0;
    public static final int DELETE = 1;
    private String Date;
    private CalFragment calFragment;
    private int type;
    public ChangeAsyncTask(RecordDao mRecordDao , PagerAdapter pg, CalFragment calFragment, String Date, int type) {
        this.mRecordDao = mRecordDao;
        this.mPagerAdapter = pg;
        this.calFragment =calFragment;
        this.Date =Date;
        this.type =type;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        calFragment.update();
        mPagerAdapter.setInsertTrue();
        mPagerAdapter.update(Date);
    }

    @Override
    protected Void doInBackground(Record... records) {
        switch (type){
            case INSERT:
                mRecordDao.insert(records[0]);
                break;
            case DELETE:
                mRecordDao.delete(records[0]);
                break;
        }
        return null;
    }


}