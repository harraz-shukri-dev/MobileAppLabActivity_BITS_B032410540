package com.utem.harrazshukri.lab;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtExpName, txtExpDate, txtExpValue, txtExpQty, txtExpTotal;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtExpName = itemView.findViewById(R.id.txtExpName);
        this.txtExpDate = itemView.findViewById(R.id.txtExpDate);
        this.txtExpValue = itemView.findViewById(R.id.txtExpValue);
        this.txtExpQty = itemView.findViewById(R.id.txtExpQty);
        this.txtExpTotal = itemView.findViewById(R.id.txtExpTotal);
    }

    public void setExpense(Expense expense) {
        txtExpName.setText(expense.getExpName());
        txtExpDate.setText(expense.getExpDate());
        txtExpValue.setText(String.format("%.2f", expense.getExpValue())); // Format float as a string
        txtExpQty.setText(String.valueOf(expense.getExpQty())); // Convert int to string
        txtExpTotal.setText(String.format("%.2f", expense.getExpTotal())); // Format total as a string
    }
}
