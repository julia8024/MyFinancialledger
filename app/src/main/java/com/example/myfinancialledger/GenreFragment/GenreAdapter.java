package com.example.myfinancialledger.GenreFragment;

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

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> implements ItemTouchHelperListener {
    ArrayList<String> genre_items = new ArrayList<>();
    private Context context;

    public GenreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_set_recyclerview_item, parent, false);

        return new GenreAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        String genre = genre_items.get(position);
        holder.setItem(genre);
    }

    @Override
    public int getItemCount() {
        return genre_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.item_name_genre);
        }

        public void setItem(String genre)   {
            itemText.setText(genre);
        }
    }

    public void addItem(String genre) {
        genre_items.add(genre);
    }

    public void setGenre_items(ArrayList<String> genre_items) {
        this.genre_items = genre_items;
    }
    public String getItem(int position) {
        return genre_items.get(position);
    }
    public void setItem(int position, String genre) {
        genre_items.set(position, genre);
    }

    // 스와이프 시 삭제
    @Override
    public void onItemSwipe(int position) {
        genre_items.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getAllItems() {
        return genre_items;
    }
}
