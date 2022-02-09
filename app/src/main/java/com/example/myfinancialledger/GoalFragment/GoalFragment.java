package com.example.myfinancialledger.GoalFragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfinancialledger.DataBase.CustomPreferenceManager;
import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.Goal;
import com.example.myfinancialledger.DataBase.GoalDao;
import com.example.myfinancialledger.MainActivity;
import com.example.myfinancialledger.R;
import com.example.myfinancialledger.Util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;

public class GoalFragment extends Fragment {



    private Spinner spinner;
    private String type;
    private String[] items = {"한달(28일 이후 설정은 2월 이후 재설정필요) ", "일주일(당일 포함해 7일)"};
    private TextView GoalRange;
    private String now;
    private Goal update;
    private EditText text;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goal, container, false);
        Database db =Database.getInstance(getContext());
        new GetGoalNow(db.mGoalDao()).execute(LocalDate.now(ZoneId.of("Asia/Seoul")).toString());
        spinner = root.findViewById(R.id.spinner_set_goal_period);
        GoalRange = root.findViewById(R.id.Goal_Range);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 items[0] 자동 선택
                type = items[0];
            }
        });

        text = root.findViewById(R.id.editText_set_goal_price);
        root.findViewById(R.id.button_set_goal_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate tmp = LocalDate.now(ZoneId.of("Asia/Seoul"));
                now = tmp.toString();
                // 키보드 닫기
                hideKeyboardFrom(getContext(), root);
                if(text.getText().toString().equals("")){
                    Toast.makeText(getContext(),"금액을 입력하세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String end = DateUtil.plusWOrM(now,type.equals("한주"),1,true);
                if(CustomPreferenceManager.getBoolean(getContext(),CustomPreferenceManager.KEY_FIRST_TIME)) {
                    CustomPreferenceManager.setString(getContext(), CustomPreferenceManager.KEY_APP_START_DATE, now);
                }
                int goal_money = Integer.parseInt(text.getText().toString());
                CustomPreferenceManager.setBoolean(getContext(),CustomPreferenceManager.KEY_FIRST_TIME,false);
                Goal[] data = new Goal[110];
                boolean WorM  = true;
                if(type.equals(items[0])){
                    data =new Goal[24];
                    WorM=false;
                }
                if(update==null){
                    update = new Goal(now,"",goal_money);
                }
                update.setEnd(LocalDate.parse(now).minusDays(1).toString());
                String tmpNow = new String(tmp.toString());
                String tmpEnd;
                for(int i=0;i<data.length;i++){
                    tmpEnd = DateUtil.plusWOrM(tmpNow,WorM,1, true);
                    data[i] = new Goal(tmpNow,tmpEnd,goal_money);
                    tmpNow = DateUtil.plusWOrM(tmpNow,WorM,1,false);
                }
                new InsertGoalTask(db.mGoalDao()).execute(data);
            }
        });
        return root;
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class GetGoalNow extends AsyncTask<String ,Void,Goal>{
        private GoalDao mGoalDao;

        public GetGoalNow(GoalDao mGoalDao){
            this.mGoalDao = mGoalDao;
        }

        @Override
        protected void onPostExecute(Goal goal) {
            super.onPostExecute(goal);
            if(goal==null) {
                GoalRange.setText("");
                return;
            }
            StringBuffer tmp = new StringBuffer();
            tmp.append(goal.getStart());
            tmp.append("~");
            tmp.append(goal.getEnd());
            text.setText(Integer.toString(goal.getGoal_money()));
            GoalRange.setText(tmp.toString());
        }

        @Override
        protected Goal doInBackground(String... strings) {
            update = mGoalDao.get_now_Goal(strings[0]);
            return update;
        }
    }


    private class InsertGoalTask extends AsyncTask<Goal,Void,Void>{
        private GoalDao mGoalDao;

        public InsertGoalTask(GoalDao mGoalDao){
            this.mGoalDao =mGoalDao;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity tmp = (MainActivity)getActivity();
            tmp.onFragmentChanged(0);
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            mGoalDao.changeGoal(now,goals,update);
            return null;
        }
    }
}