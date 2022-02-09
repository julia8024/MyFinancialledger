package com.example.myfinancialledger.DataBase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Record.class,Goal.class}, version = 2, exportSchema = true)
public abstract class Database extends RoomDatabase {
    private static Database INSTANCE;
    public abstract RecordDao mRecordDao();
    public abstract GoalDao mGoalDao();
    public static synchronized Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Record-db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
