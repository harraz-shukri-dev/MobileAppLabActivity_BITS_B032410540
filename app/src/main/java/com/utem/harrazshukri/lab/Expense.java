package com.utem.harrazshukri.lab;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_expense")
public class Expense {
@PrimaryKey(autoGenerate = true)
    public int id;
     String expName, expDate;
     float expValue, expTotal;
     int expQty;

    public Expense(String expName, String expDate, float expValue, int expQty, float expTotal) {
        this.expName = expName;
        this.expDate = expDate;
        this.expValue = expValue;
        this.expQty = expQty;
        this.expTotal = expValue * expQty; // Calculate total price dynamically
    }

    public String getExpName() {
        return expName;
    }

    public String getExpDate() {
        return expDate;
    }

    public float getExpValue() {
        return expValue;
    }

    public int getExpQty() {
        return expQty;
    }

    public float getExpTotal() {
        return expTotal;
    }
}
