package com.example.myfinancialledger;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.myfinancialledger.BankFragment.BankFragment;
import com.example.myfinancialledger.ChartFragment.Chart_Genre;
import com.example.myfinancialledger.DataBase.CustomPreferenceManager;
import com.example.myfinancialledger.DataBase.Database;
import com.example.myfinancialledger.DataBase.GoalDao;
import com.example.myfinancialledger.DataBase.RecordDao;
import com.example.myfinancialledger.GenreFragment.GenreFragment;
import com.example.myfinancialledger.GoalFragment.GoalFragment;
import com.example.myfinancialledger.MainFragment.MainFragment;
import com.example.myfinancialledger.Util.DateUtil;
import com.google.android.material.navigation.NavigationView;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements MainFragment.SpinnerDate, NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    private ArrayList<String> items;
    private MaterialSpinner spinner;
    private MainFragment mainFragment;
    private GoalFragment fragment_goal = new GoalFragment();
    private GenreFragment fragment_genre = new GenreFragment();
    private BankFragment  fragment_bank = new BankFragment();
    private Chart_Genre fragment_chart = new Chart_Genre();
    private Boolean Main = true;
    private LocalDate AppStartDay;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database db = Database.getInstance(this);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppStartDay = LocalDate.parse(CustomPreferenceManager.getString(this,CustomPreferenceManager.KEY_APP_START_DATE));
        LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        spinner = (MaterialSpinner)toolbar.findViewById(R.id.spinner);
        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                spinner.getPopupWindow().setWidth(spinner.getWidth());
            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (Main)
                    mainFragment.update_cal(items.get(position));
                else
                    fragment_chart.changeRange(getSpinnerDate());
            }
        });
        setSupportActionBar(toolbar) ;
        setItems(localDate);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        mainFragment = MainFragment.newInstance(localDate.toString());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(CustomPreferenceManager.getBoolean(this,CustomPreferenceManager.KEY_FIRST_TIME)) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment_goal).commit();
            toolbar.setVisibility(View.INVISIBLE);
            return;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container,mainFragment).commit();
    }

    @Override
    public String getSpinnerDate() {
        return items.get(spinner.getSelectedIndex());
    }

    public void setItems(LocalDate cal){
        StringBuffer tmp = new StringBuffer();
        String first =CustomPreferenceManager.getString(this,CustomPreferenceManager.KEY_APP_START_DATE);
        LocalDate firstDate = LocalDate.parse(first);
        LocalDate firstStandard = LocalDate.parse(DateUtil.getCal_FORMAT(LocalDate.parse(first))+"-01");
        LocalDate nowStandard = LocalDate.parse(DateUtil.getCal_FORMAT(cal)+"-01");
        Period period = firstStandard.until(nowStandard);
        items = new ArrayList<String>();
        for (int i=0;i<period.getMonths()+24;i++){
            GregorianCalendar d = new GregorianCalendar(firstDate.getYear(),firstDate.getMonthValue()+i-1,1,0,0,0);
            tmp.delete(0,tmp.length());
            tmp.append(Integer.toString(d.get(Calendar.MONTH)+1));
            if(d.get(Calendar.MONTH)+1<10)
                tmp.insert(0,'0');
                tmp.insert(0,Integer.toString(d.get(Calendar.YEAR))+"-");
            items.add(tmp.toString());
        }
        spinner.setItems(items);
        spinner.setSelectedIndex(items.indexOf(DateUtil.getCal_FORMAT(cal)));
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            onFragmentSelected(0, null);
        } else if (id == R.id.menu_goal) {
            onFragmentSelected(1, null);
        } else if (id == R.id.menu_genre) {
            onFragmentSelected(2, null);
        } else if (id == R.id.menu_bank) {
            onFragmentSelected(3, null);
        } else if (id == R.id.menu_chart){
            onFragmentSelected(4,null);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment curFragment = null;
        spinner.setVisibility(View.INVISIBLE);
        if (position == 0) {
            curFragment = mainFragment;
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelectedIndex(items.indexOf(DateUtil.getCal_FORMAT(LocalDate.now(ZoneId.of("Asia/Seoul")))));
            Main = true;
            toolbar.setTitle("");
        } else if (position == 1) {
            curFragment = fragment_goal;
            toolbar.setTitle("목표 금액 설정");
        } else if (position == 2) {
            curFragment = fragment_genre;
            toolbar.setTitle("지출 유형 설정");
        } else if (position == 3) {
            curFragment = fragment_bank;
            toolbar.setTitle("은행 설정");
        } else if (position == 4) {
            curFragment = fragment_chart;
            toolbar.setTitle("");
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelectedIndex(items.indexOf(DateUtil.getCal_FORMAT(LocalDate.now(ZoneId.of("Asia/Seoul")))));
            Main = false;
        }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();
    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelectedIndex(items.indexOf(DateUtil.getCal_FORMAT(LocalDate.now(ZoneId.of("Asia/Seoul")))));
            Main= true;
        }
    }

    private class DeleteAll extends AsyncTask<Void,Void,Void>{
        private GoalDao mGoalDao;
        private RecordDao mRecordDao;
        public DeleteAll(GoalDao mGoalDao,RecordDao mRecordDao){
            this.mGoalDao = mGoalDao;
            this.mRecordDao =mRecordDao;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mGoalDao.deleteAll();
            mRecordDao.deleteAll();
            return null;
        }
    }


}