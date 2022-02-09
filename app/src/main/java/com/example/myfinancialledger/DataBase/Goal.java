package com.example.myfinancialledger.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Goal {
    @NonNull
    @PrimaryKey
    private String Start;
    private String End;
    private int Goal_money;

    public Goal(String Start, String End, int Goal_money){
        this.Start = Start;
        this.End =End;
        this.Goal_money = Goal_money;
    }

    public void setStart(String start) {
        Start = start;
    }

    public void setEnd(String end) {
        End = end;
    }

    public void setGoal_money(int goal_money) {
        Goal_money = goal_money;
    }

    public String getStart() {
        return Start;
    }

    public String getEnd() {
        return End;
    }

    public int getGoal_money() {
        return Goal_money;
    }
}
