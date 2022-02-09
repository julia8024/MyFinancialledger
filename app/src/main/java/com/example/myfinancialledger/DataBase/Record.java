package com.example.myfinancialledger.DataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String DATE;
    private int Amount;
    private String Type;
    private String KindOfI_O;
    private String BankOrGenre;
    private String Memo;

    public Record(String DATE, int Amount, String Type, String KindOfI_O, String BankOrGenre, String Memo) {
        this.DATE = DATE;
        this.Amount = Amount;
        this.Type = Type;
        this.KindOfI_O = KindOfI_O;
        this.BankOrGenre = BankOrGenre;
        this.Memo = Memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getKindOfI_O() {
        return KindOfI_O;
    }

    public void setKindOfI_O(String kindOfI_O) {
        KindOfI_O = kindOfI_O;
    }

    public String getBankOrGenre() {
        return BankOrGenre;
    }

    public void setBankOrGenre(String bankOrGenre) {
        BankOrGenre = bankOrGenre;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
