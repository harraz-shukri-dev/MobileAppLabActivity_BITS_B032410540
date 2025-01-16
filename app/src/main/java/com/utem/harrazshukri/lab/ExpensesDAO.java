package com.utem.harrazshukri.lab;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpensesDAO {
    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    @Query("SELECT * FROM tbl_expense")
    List<Expense> getAllExpenses();


    @Query("SELECT * FROM tbl_expense WHERE id = :id")
    Expense getExpenseById(int id);
}
