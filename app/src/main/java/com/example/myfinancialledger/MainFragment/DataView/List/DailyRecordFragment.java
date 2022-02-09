package com.example.myfinancialledger.MainFragment.DataView.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinancialledger.DataBase.ChangeAsyncTask;
import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.Record;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;
import com.example.myfinancialledger.MainFragment.MainFragment;
import com.example.myfinancialledger.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyRecordFragment extends Fragment  {
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    private Database db;
    private DailyRecordAdapter adapter;
    private RecyclerView recyclerView;
    private MainFragment tmp;
    private TextView InfoAmount;
    private TextView InfoKindOfI_O;
    private TextView InfoBankOrGenre;
    private TextView InfoMemo;
    private Button bt_delete;
    private Record SelectedItem;
    private  List<Record> data  = new ArrayList<Record>();
    public DailyRecordFragment() { }

    public static DailyRecordFragment getInstance(String date) {
        DailyRecordFragment d = new DailyRecordFragment();
        Bundle arg = new Bundle();
        arg.putString(PagerAdapter.KEY_DATE,date);
        arg.putBoolean(PagerAdapter.KEY_CREATE, false);
        d.setArguments(arg);
        return d;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_daily_record, container, false);
        db = Database.getInstance(getActivity());
        InfoAmount = v.findViewById(R.id.AmountInfo);
        InfoKindOfI_O = v.findViewById(R.id.InfoCCA);
        InfoBankOrGenre = v.findViewById(R.id.InfoGB);
        tmp= null;
        for(Fragment fragment : getFragmentManager().getFragments()){
            if(fragment instanceof MainFragment){
                tmp = (MainFragment) fragment;
                break;
            }
        }
        InfoMemo = v.findViewById(R.id.InfoMemo);
        getArguments().putBoolean(PagerAdapter.KEY_CREATE,true);
        recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        adapter = new DailyRecordAdapter();
        update(getArguments().getString(PagerAdapter.KEY_DATE));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DailyRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DailyRecordAdapter.ViewHolder holder, View view, int position) {
                Record item = adapter.getItem(position);
                SelectedItem = item;
                InfoBankOrGenre.setVisibility(View.VISIBLE);
                InfoAmount.setText(Integer.toString(item.getAmount()));
                InfoKindOfI_O.setText(item.getKindOfI_O());
                InfoMemo.setText(item.getMemo());
                if(InfoKindOfI_O.getText().toString().equals("현금")&&item.getType().equals("입금")) {
                    InfoBankOrGenre.setVisibility(View.INVISIBLE);
                    return;
                }
                InfoBankOrGenre.setText(item.getBankOrGenre());
            }
        });
        bt_delete = v.findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectedItem==null)
                    return;

                new ChangeAsyncTask(db.mRecordDao(),
                        tmp.getPagerAdapter(),tmp.getCalFragment(),tmp.getSelectDate(),ChangeAsyncTask.DELETE).execute(SelectedItem);
            }
        });

        return v;
    }

    private class getDateRecord extends AsyncTask<String,Void,Void>{
        private RecordDao mRecordDao;
        private List<Record> tmp = new ArrayList<Record>();
        public getDateRecord(RecordDao mRecordDao){
            this.mRecordDao = mRecordDao;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setItems(tmp);
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {
            tmp = mRecordDao.getDateRecord(strings[0]);
            return null;
        }
    }

    public void update(String date){
        Database database = Database.getInstance(getContext());
        new getDateRecord(database.mRecordDao()).execute(date);
    }

    public void reset(){
        InfoAmount.setText("");
        InfoKindOfI_O.setText("");
        InfoBankOrGenre.setText("");
        InfoMemo.setText("");
        SelectedItem = null;
        InfoBankOrGenre.setVisibility(View.VISIBLE);
    }

    private class bt_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}