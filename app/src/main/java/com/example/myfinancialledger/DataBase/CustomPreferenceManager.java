package com.example.myfinancialledger.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomPreferenceManager {

    public static final String PREFERENCES_NAME = "goal_preference";
    public static final String KEY_GENRE = "genre_data";
    public static final String KEY_BANK = "bank_data";
    public static final String KEY_GOAL_PRICE = "goal_price";
    public static final String KEY_APP_START_DATE = "START_DATE";
    public static final String KEY_FIRST_TIME = "first_time";
    private static final String DEFAULT_VALUE_STRING_DATE = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
    private static final String DEFAULT_VALUE_STRING = "";
    private static final int DEFAULT_VALUE_INT = 0;
    private static final boolean DEFAULT_VALUE_BOOLEAN = true;
    private static final String [] defaultBankSet = { "NH농협", "기업은행", "신협", "새마을금고", "KB국민은행", "우리은행", "국민은행", "하나은행", "카카오뱅크" };
    private static final String [] defaultGenreSet = { "음식", "쇼핑", "여가", "공과금", "세금" };
    private static final Set<String> DEFAULT_VALUE_STRING_SET_BANK = new HashSet<>(Arrays.asList(defaultBankSet));
    private static final Set<String> DEFAULT_VALUE_STRING_SET_GENRE = new HashSet<>(Arrays.asList(defaultGenreSet));

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // String 값 저장
    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    // int 값 저장
    public static void setInt(Context context, String key, int value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // String Set 값 저장
    public static void setStringSet(Context context, String key, ArrayList<String> value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(value);
        editor.putStringSet(key, set);
        editor.commit();
    }

    // String 값 로드
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String DEFAULT = CustomPreferenceManager.DEFAULT_VALUE_STRING;
        if(key.equals(CustomPreferenceManager.KEY_APP_START_DATE))
            DEFAULT = CustomPreferenceManager.DEFAULT_VALUE_STRING_DATE;
        String value = prefs.getString(key, DEFAULT);
        return value;
    }

    // int 값 로드
    public static int getInt(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        int value = prefs.getInt(key, DEFAULT_VALUE_INT);
        return value;
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;
    }

    // String Set 값 로드
    public static ArrayList<String> getStringSet(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        Set<String> value;
        if(key.equals(KEY_BANK))
            value= new HashSet<>(prefs.getStringSet(key, DEFAULT_VALUE_STRING_SET_BANK));
        else
            value = new HashSet<>(prefs.getStringSet(key,DEFAULT_VALUE_STRING_SET_GENRE));
        return new ArrayList<String>(value);
    }

    // key 값 삭제
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }

    // 모든 저장 데이터 삭제
    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
