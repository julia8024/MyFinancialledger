package com.example.myfinancialledger.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myfinancialledger.Util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public abstract class RecordDao {
    @Query("SELECT * FROM Record")
    public abstract List<Record> getAll();

    @Insert
    public abstract void insert(Record record);

    @Update
    public abstract void update(Record record);

    @Delete
    public abstract void delete(Record record);

    @Query("SELECT * from Record WHERE date == :d")
    public abstract List<Record> getDateRecord(String d);

    @Query("SELECT TOTAL(Amount) from Record WHERE type =:type AND date like :d")
    public abstract int get_total_money(String d,String type);

    @Query("SELECT BankOrGenre from Record WHERE type == '출금' AND date like :d")
    public abstract String[] get_ALL_Genre(String d);

    @Query("DELETE FROM Record")
    public abstract void deleteAll();

    @Transaction
    public Map<String,Integer> GetCountGenre(String d,String[] set){
        String[] tmp =get_ALL_Genre(d);
        Map<String,Integer> map = new HashMap<String,Integer>();
        for(String key :set){
            map.put(key,0);
        }
        for(String key :tmp){
            if(!map.containsKey(key))
                key="기타";
            map.put(key,map.getOrDefault(key,0)+1);
        }
        return map;
    }

    @Transaction
    public int[] GetTotalPeriod(Goal goal){
       String[] range = DateUtil.getPeriodArray(goal.getStart(),goal.getEnd());
       int[] result = {0,0};
        for (String s : range) {
            result[0] += get_total_money(s, "출금");
            result[1] += get_total_money(s, "입금");
        }
       return result;
    }
}
