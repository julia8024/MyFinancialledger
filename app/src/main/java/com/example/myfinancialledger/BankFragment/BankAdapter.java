package com.example.myfinancialledger.BankFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinancialledger.ItemTouchHelperListener;
import com.example.myfinancialledger.R;
import java.util.ArrayList;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> implements ItemTouchHelperListener {
    ArrayList<String> bank_items = new ArrayList<>();
    private Context context;

    public BankAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_set_recyclerview_item, parent, false);

        return new BankAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, int position) {
        String bank = bank_items.get(position);
        holder.setItem(bank);
    }

    @Override
    public int getItemCount() {
        return bank_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_name);
        }

        public void setItem(String bank)   {
            itemText.setText(bank);
        }
    }

    public void addItem(String bank) {
        bank_items.add(bank);
    }

    public void setBank_items(ArrayList<String> bank_items) {
        this.bank_items = bank_items;
    }
    public String getItem(int position) {
        return bank_items.get(position);
    }
    public void setItem(int position, String bank) {
        bank_items.set(position, bank);
    }

    // 스와이프 시 삭제
    @Override
    public void onItemSwipe(int position) {
        bank_items.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getAllItems() {
        return bank_items;
    }
}

