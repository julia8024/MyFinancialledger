package com.example.myfinancialledger.MainFragment.DataView.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.Record;
import com.example.myfinancialledger.R;

import java.util.ArrayList;
import java.util.List;

public class DailyRecordAdapter extends RecyclerView.Adapter<DailyRecordAdapter.ViewHolder> {
    public List<Record> items = new ArrayList<>();
    //private Context context;
    public Database db;
    OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    public DailyRecordAdapter() {

    }

    @Override //아이템 개수
    public int getItemCount() {
        return items.size();
    }

    public List<Record> getItems() {
        return items;
    }

    public Record getItem(int position) {
        return items.get(position);
    }

    //아이템 한꺼번에 추가
    public void setItems(List<Record> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    //아이템 추가
    public void addItem(Record item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override //뷰홀더 생성
    public DailyRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.amount_recyclerview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override //뷰 셋팅
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Record model = items.get(position);
        viewHolder.setItem(model);

        viewHolder.setOnItemClickListener(listener);
    }

    //뷰 바인딩
    public class ViewHolder extends RecyclerView.ViewHolder {
        int index;
        OnItemClickListener listener;
        TextView AmountText;
        TextView won;

        public ViewHolder(View itemView) {
            super(itemView);

            AmountText = itemView.findViewById(R.id.AmountText);
            won = itemView.findViewById(R.id.won);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(Record item) {
            AmountText.setText(Integer.toString(item.getAmount()));
            int Color =0x808BC34A;
            if (item.getType().equals("출금"))
                Color =0x70E30E49;
            AmountText.setTextColor(Color);
            won.setTextColor(Color);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}

