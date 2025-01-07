package com.utem.harrazshukri.lab;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    private final TextView lblFullName,lblStudNo,lblGender,lblBirthdate,lblEmail,lblState;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        this.lblFullName = itemView.findViewById(R.id.lblFullName);
        this.lblStudNo = itemView.findViewById(R.id.lblStudNo);
        this.lblGender = itemView.findViewById(R.id.lblGender);
        this.lblBirthdate = itemView.findViewById(R.id.lblBirthdate);
        this.lblEmail = itemView.findViewById(R.id.lblEmail);
        this.lblState = itemView.findViewById(R.id.lblState);
    }


    public void setStudent(Student student)
    {
        lblFullName.setText(student.getStrFullName());
        lblStudNo.setText(student.getStrStudNo());
        lblEmail.setText(student.getStrEmail());
        lblBirthdate.setText(student.getStrBirthdate());
        lblGender.setText(student.getStrGender());
        lblState.setText(student.getStrState());
    }

}
