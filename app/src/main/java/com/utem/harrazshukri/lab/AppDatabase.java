package com.utem.harrazshukri.lab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {


    private static volatile AppDatabase INSTANCE;

    public abstract ExpensesDAO expensesDAO();

    public static AppDatabase getDatabase(final Context context) {

        if (INSTANCE == null)
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db_expense").setJournalMode(JournalMode.TRUNCATE).fallbackToDestructiveMigration().build();

                }
                return INSTANCE;

            }
        return INSTANCE;

    }


}
