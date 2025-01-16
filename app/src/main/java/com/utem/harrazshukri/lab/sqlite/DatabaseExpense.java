package com.utem.harrazshukri.lab.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.utem.harrazshukri.lab.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseExpense extends SQLiteOpenHelper {

    public static final String dbName = "dbExpense";
    public static final int dbVersion = 1;
    public static final String tblExpense = "tblExpense";

    public static final String colId = "id";

    public static final String colExpName = "exp_name";

    public static final String colExpDate = "exp_date";

    public static final String colExpValue = "exp_value";

    public static final String colExpQty = "exp_qty";
    public static final String colExpTotal = "exp_total";


    public static final String createTable = "CREATE TABLE " + tblExpense + "("
            + colId + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colExpName + " TEXT, "
            + colExpDate + " TEXT, "
            + colExpValue + " REAL, "
            + colExpQty + " INTEGER, "
            + colExpTotal + " TEXT)";

    public static final String dropTable = "DROP TABLE IF EXISTS " + tblExpense;


    public DatabaseExpense(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTable);
    }


    public int fnInsertExpense(Expense expense) {
        int resCode = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(colExpName, expense.getExpName());
        values.put(colExpDate, expense.getExpDate());
        values.put(colExpValue, expense.getExpValue());
        values.put(colExpQty, expense.getExpQty());
        values.put(colExpTotal, expense.getExpTotal());

        return (int) db.insert(tblExpense, null, values);

    }

    //Change 1
    @SuppressLint("Range")
    public List<Expense> fnGetAllExpense() {
        List<Expense> expenses = new ArrayList<Expense>();
        String strSelect = "SELECT * FROM " + tblExpense;
        Cursor cursor = getReadableDatabase().rawQuery(strSelect, null);
        if (cursor.moveToFirst()) {
            do {
                String expName = cursor.getString(cursor.getColumnIndex(colExpName));
                String expDate = cursor.getString(cursor.getColumnIndex(colExpDate));
                float expValue = cursor.getFloat(cursor.getColumnIndex(colExpValue));
                int expQty = cursor.getInt(cursor.getColumnIndex(colExpQty));
                float expTotal = cursor.getFloat(cursor.getColumnIndex(colExpTotal));
                Expense expense = new Expense(expName, expDate, expValue, expQty, expTotal);
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        return expenses;
    }

}
