package com.example.myfinancialledger.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class GoalDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    public abstract void insertGoal(Goal[] goals);

    @Update
    public abstract void update(Goal goal);

    @Query("SELECT * FROM Goal WHERE CAST(strftime('%s', `End`) AS integer ) >= CAST(strftime('%s',:target) AS integer) LIMIT 1")
    public abstract Goal get_now_Goal(String target);

    @Query("DELETE FROM Goal WHERE `Start` > date(:target) ")
    public abstract void delete(String target);

    @Transaction
    public void changeGoal(String target, Goal[] goals,Goal goal) {
        update(goal);
        delete(target);
        insertGoal(goals);
    }

    @Query("DELETE FROM Goal")
    public abstract void deleteAll();
}
