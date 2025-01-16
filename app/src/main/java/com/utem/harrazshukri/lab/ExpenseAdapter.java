package com.utem.harrazshukri.lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Expense> expenses;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.layoutInflater = LayoutInflater.from(context);
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseViewHolder(layoutInflater.inflate(R.layout.item_expenses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.setExpense(expenses.get(position));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
