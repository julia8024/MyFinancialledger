package com.example.myfinancialledger.BankFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfinancialledger.DataBase.CustomPreferenceManager;
import com.example.myfinancialledger.ItemTouchHelperCallback;
import com.example.myfinancialledger.MainActivity;
import com.example.myfinancialledger.R;

import java.util.ArrayList;

public class BankFragment extends Fragment {

    public BankFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    ArrayList<String> bank_items;
    BankAdapter bankAdapter;
    ItemTouchHelper helper;

    String[] string_bank = {"농협", "국민", "신한"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bank, container, false);

        // 은행 리사이클러뷰 기본 설정
        recyclerView = root.findViewById(R.id.RecyclerView_bank);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        bankAdapter = new BankAdapter(getContext());
        recyclerView.setAdapter(bankAdapter);

        // 스와이프 이벤트를 위해 ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(bankAdapter));
        // 리사이클러뷰에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

        // shared preferences 에서 bank_data 값 가져오기
        bank_items = CustomPreferenceManager.getStringSet(getContext(), "bank_data");

        if (bank_items.size() == 0) {
            // 기본값 string_bank 을 리사이클러뷰에 넣기
            for (int i = 0; i < string_bank.length; i++) {
                bankAdapter.addItem(string_bank[i]);
            }
        } else {
            // 가져온 bank_items 를 리사이클러뷰에 넣기
            for (int i = 0; i < bank_items.size(); i++) {
                bankAdapter.addItem(bank_items.get(i));
            }
        }

        // 버튼 이벤트
        // 은행 항목 추가
        EditText editText = root.findViewById(R.id.editText_add_bank);
        root.findViewById(R.id.button_add_bank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // 사용자가 입력한 내용 가져오기
                String add_bank = editText.getText().toString();

                if(bankAdapter.getAllItems().contains(add_bank)) {
                    Toast.makeText(getContext(), "이미 존재하는 항목입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ArrayList 에 text 값 추가
                bankAdapter.addItem(add_bank);

                // 어댑터에서 리사이클러뷰에 반영하도록 함
                bankAdapter.notifyDataSetChanged();

                // editText 에 입력된 값 없애기
                editText.setText("");
            }
        });

        // 확인 버튼 클릭 시 이벤트
        root.findViewById(R.id.button_set_bank_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // shared preferences 에 은행 데이터 저장
                CustomPreferenceManager.setStringSet(getContext(), "bank_data", bankAdapter.getAllItems());

                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // editText 에 입력된 값 없애기ItemTouchHelperCallback
                editText.setText("");

                // 홈화면 (home fragment) 으로 이동
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        // 취소 버튼 클릭 시 이벤트
        root.findViewById(R.id.button_set_bank_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // editText 에 입력된 값 없애기
                editText.setText("");

                // 홈화면 (home fragment) 으로 이동
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        return root;
    }

    // 키보드 닫기 메소드
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}