package com.example.myfinancialledger.MainFragment.I_OFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.myfinancialledger.DataBase.CustomPreferenceManager;
import com.example.myfinancialledger.DataBase.ChangeAsyncTask;
import com.example.myfinancialledger.DataBase.Record;
import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.MainFragment.Calendar.CalFragment;
import com.example.myfinancialledger.MainFragment.DataView.PagerAdapter;
import com.example.myfinancialledger.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class I_OFragment extends DialogFragment{
    public static final String TAG_EVENT_DIALOG = "dialog_event";
    private String Key;
    private  String I_O = "출금";
    private String[] Spending;
    private String chg_set = "계좌이체";
    private String CD = "";
    private CalFragment calFragment;
    private String otherCD = "카드";
    private String spending = "";
    private Integer Amount;
    private EditText wEditCash;
    private EditText wMemo;
    private String Memos;
    private MaterialSpinner wGenreSelect;

    private Button wCardButton;
    private Button wCashButton;

    public Date date = new Date();
    public String getTime;
    private PagerAdapter pagerAdapter;
    private final bt_listener listener = new bt_listener();
    public I_OFragment() { }

    public static I_OFragment newInstance(PagerAdapter pg, CalFragment calFragment) {
        I_OFragment w = new I_OFragment();
        Bundle arg = new Bundle();
        w.pagerAdapter = pg;
        w.calFragment =calFragment;
        arg.putBoolean("Type",true);
        arg.putString("Date", "");
        w.setArguments(arg);
        return w;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inoutput, container);
        Database db = Database.getInstance(getActivity());

        wCardButton = (Button) v.findViewById(R.id.wCardButton);
        if(getArguments().getBoolean("Type")) {
            I_O = "입금";
            Key = CustomPreferenceManager.KEY_BANK;
            otherCD = chg_set;
            wCardButton.setText(chg_set);
        }
        else {
            Key = CustomPreferenceManager.KEY_GENRE;
            I_O = "출금";
        }
        ArrayList<String> tmp = CustomPreferenceManager.getStringSet(getContext(),Key);
        Spending = new String[tmp.size()];
        Spending = tmp.toArray(Spending);
        spending = Spending[0];
        getTime = getArguments().getString("Date");

        ImageButton wDelete = (ImageButton) v.findViewById(R.id.wDelete);
        wDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        wEditCash = (EditText) v.findViewById(R.id.wEditTextCash);
        wEditCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String cash = wEditCash.getText().toString().trim();
                    Amount = Integer.valueOf(cash);
                } catch (Exception e) {
                    Amount = 0;
                }
            }
        });

        wCardButton.setOnClickListener(listener);

        wCashButton = (Button) v.findViewById(R.id.wCashButton);
        wCashButton.setOnClickListener(listener);
        wGenreSelect = (MaterialSpinner) v.findViewById(R.id.wGenreSelect);
        wGenreSelect.setItems(Arrays.asList(Spending));
        wGenreSelect.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spending = item;
            }
        });

        wMemo = (EditText) v.findViewById(R.id.wMemo);
        wMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Memos = wMemo.getText().toString().trim();
            }
        });

        Button wOkButton = (Button) v.findViewById(R.id.wOkButton);
        wOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CD.equals("")||Amount==null||Amount==0) {
                    Toast.makeText(getContext(), "금액/타입을 입력/선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(CD.equals("현금")&&getArguments().getBoolean("Type"))
                    spending = "";

                new ChangeAsyncTask(db.mRecordDao(),pagerAdapter,calFragment,getTime,ChangeAsyncTask.INSERT).execute(new Record(getTime, Amount, I_O, CD, spending, Memos));
                dismiss();
            }
        });

        Button wNoButton = (Button) v.findViewById(R.id.wNoButton);
        wNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        wEditCash.setText("");
        wMemo.setText("");
        ArrayList<String> tmp = CustomPreferenceManager.getStringSet(getContext(),Key);
        Spending = new String[tmp.size()];
        Spending = tmp.toArray(Spending);
        spending = Spending[0];
        wGenreSelect.setItems(Spending);
        wGenreSelect.setSelectedIndex(0);
        getTime = getArguments().getString("Date");
        super.onResume();
    }

    private class bt_listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d("dsd", "onClick: on");
            if(v.isSelected())
                return;
            if(v.getId()==wCardButton.getId()) {
                wCardButton.setSelected(true);
                wCashButton.setSelected(false);
                wGenreSelect.setVisibility(View.VISIBLE);
                CD = otherCD;
            }
            else {
                wCardButton.setSelected(false);
                wCashButton.setSelected(true);
                if(getArguments().getBoolean("Type"))
                    wGenreSelect.setVisibility(View.INVISIBLE);
                CD = "현금";
            }
        }
    }
    public void change_arg(boolean type,String date){

        getArguments().putBoolean("Type",type);
        getArguments().putString("Date",date);
    }


}