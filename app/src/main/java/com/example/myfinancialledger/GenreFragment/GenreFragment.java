package com.example.myfinancialledger.GenreFragment;

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
import java.util.Set;

public class GenreFragment extends Fragment {

    public GenreFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    ArrayList<String> genre_items;
    Set<String> genre_set;
    GenreAdapter genreAdapter;
    ItemTouchHelper helper;

    String[] string_genre = {"식비", "쇼핑", "여행"};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_genre, container, false);

        // 지출 유형 리사이클러뷰 기본 설정
        recyclerView = root.findViewById(R.id.RecyclerView_genre);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        genreAdapter = new GenreAdapter(getContext());
        recyclerView.setAdapter(genreAdapter);

        // 스와이프 이벤트를 위해 ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(genreAdapter));
        // 리사이클러뷰에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

        // shared preferences 에서 genre_data 값 가져오기
        genre_items = CustomPreferenceManager.getStringSet(getContext(), "genre_data");

        if (genre_items.size() == 0) {
            // 기본값 string_genre 을 리사이클러뷰에 넣기
            for (int i = 0; i < string_genre.length; i++) {
                genreAdapter.addItem(string_genre[i]);
            }
        } else {
            // 가져온 genre_items 를 리사이클러뷰에 넣기
            for (int i = 0; i < genre_items.size(); i++) {
                genreAdapter.addItem(genre_items.get(i));
            }
        }

        // 버튼 이벤트
        // 지출 유형 항목 추가
        EditText editText = root.findViewById(R.id.editText_add_genre);
        root.findViewById(R.id.button_add_genre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // 사용자가 입력한 내용 가져오기
                String add_genre = editText.getText().toString();

                if(genreAdapter.getAllItems().contains(add_genre)) {
                    Toast.makeText(getContext(), "이미 존재하는 항목입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // ArrayList 에 text 값 추가
                genreAdapter.addItem(add_genre);

                // 어댑터에서 리사이클러뷰에 반영하도록 함
                genreAdapter.notifyDataSetChanged();

                // editText 에 입력된 값 없애기
                editText.setText("");

            }
        });

        // 확인 버튼 클릭 시 이벤트
        root.findViewById(R.id.button_set_genre_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // shared preferences 에 지출 유형 데이터 저장
//                PreferenceManager.removeKey(getContext(), "genre_data");
                CustomPreferenceManager.setStringSet(getContext(), CustomPreferenceManager.KEY_GENRE, genreAdapter.getAllItems());

                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // editText 에 입력된 값 없애기
                editText.setText("");

                // 홈화면 (home fragment) 으로 이동
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChanged(0);
            }
        });

        // 취소 버튼 클릭 시 이벤트
        root.findViewById(R.id.button_set_genre_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);

                // editText 에 입력된 값 없애기
                editText.setText("");
                if(CustomPreferenceManager.getBoolean(getContext(),CustomPreferenceManager.KEY_FIRST_TIME)){
                    CustomPreferenceManager.setBoolean(getContext(),CustomPreferenceManager.KEY_FIRST_TIME,true);
                }
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