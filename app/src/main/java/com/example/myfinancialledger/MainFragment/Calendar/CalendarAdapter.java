package com.example.myfinancialledger.MainFragment.Calendar;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myfinancialledger.MainActivity;
import com.example.myfinancialledger.MainFragment.DataView.List.DailyRecordAdapter;
import com.example.myfinancialledger.MainFragment.MainFragment;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.MainFragment.Calendar.CalViewModel.Day;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;
    private List<Object> mCalendarList;
    private int[] deposit = new int[32];
    private int[] withdraw = new int[32];
    private int FirstDayOfMonth;
    private itemClickedListener itemClickedListener;
    private String selectDate;

    public CalendarAdapter(List<Object> calendarList) {
        mCalendarList = calendarList;
    }

    public void setCalendarList(List<Object> calendarList, int[] deposit, int[] withdraw) {
        mCalendarList = calendarList;
        this.deposit = deposit;
        this.withdraw = withdraw;
        notifyDataSetChanged();
    }

    public void setItemClickedListener(itemClickedListener listener) {
        itemClickedListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mCalendarList.get(position);
        if (item instanceof String) {
            return EMPTY_TYPE;
        } else {
            return DAY_TYPE;
        }
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, parent, false));

        } else if (viewType == DAY_TYPE) {
            ViewGroup tmp = (ViewGroup) inflater.inflate(R.layout.item_day, parent, false);
            DayViewHolder holder = new DayViewHolder(tmp);
            return holder;
        }
        return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            GregorianCalendar item = (GregorianCalendar) mCalendarList.get(position);
            if (item.get(Calendar.DAY_OF_MONTH) == 1)
                FirstDayOfMonth = item.get(Calendar.DAY_OF_WEEK) - 1;
            item.getFirstDayOfWeek();
            int dateDeposit = deposit[position - FirstDayOfMonth];
            int dateWithdraw = withdraw[position - FirstDayOfMonth];
            Day model = new Day();
            model.setCalendar((Calendar) item);
            model.check(item);
            holder.bind(model, dateDeposit, dateWithdraw);
        }
    }

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder

        RelativeLayout layout;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v) {
            layout = v.findViewById(R.id.item_layout);
        }

    }

    // TODO : item_day와 매칭
    public class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder
        TextView itemDay;
        TextView deposit;
        TextView withdraw;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (itemClickedListener != null) {
                        itemClickedListener.onItemClick(DayViewHolder.this, v, position);
                    }
                }
            });
        }

        public void initView(View v) {
            itemDay = (TextView) v.findViewById(R.id.item_day);
            deposit = (TextView) v.findViewById(R.id.deposit);
            withdraw = (TextView) v.findViewById(R.id.withdraw);
        }

        public void bind(Day model, int deposit, int withdraw) {

            // 일자 값 가져오기
            String day = ((Day) model).getDay();
            // 일자 값 View에 보이게하기
            if (model.getToday()) {
                itemDay.setTextSize(16);
                itemDay.setTextColor(0xFF006600);
            } else {
                itemDay.setTextSize(12);
                itemDay.setTextColor(Color.GRAY);
            }
            itemDay.setText(day);
            this.deposit.setText(Integer.toString(deposit));
            this.withdraw.setText(Integer.toString(withdraw));
        }

    }

    public String getSelectDate() {
        return selectDate;
    }

    public static interface itemClickedListener {
        public void onItemClick(DayViewHolder holder, View view, int position);
    }
}
