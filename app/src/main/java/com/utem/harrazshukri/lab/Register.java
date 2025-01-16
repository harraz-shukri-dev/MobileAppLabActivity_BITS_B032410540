package com.utem.harrazshukri.lab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.utem.harrazshukri.lab.databinding.ActivityRegisterBinding;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up Drawer Layout and Toggle
        drawerLayout = binding.myDrawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inside onCreate() of any activity
        NavigationUtils.setupNavigation(this, binding.myDrawerLayout, binding.navigation);

        // Fields
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        EditText editTextDOB = findViewById(R.id.editTextDOB);

        RadioButton radioMale = findViewById(R.id.radioMale);
        RadioButton radioFemale = findViewById(R.id.radioFemale);

        Button buttonRegister = findViewById(R.id.buttonRegister);

        // Retrieve Intent Extras
        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("PASSWORD");

        // Populate fields with received values
        editTextName.setText(username);
        editTextPassword.setText(password);

        // Set OnClickListener to open a DatePickerDialog
        editTextDOB.setOnClickListener(v -> showDatePicker(editTextDOB));

        // Ensure only one gender checkbox is selected
        radioMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) radioFemale.setChecked(false);
        });

        radioFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) radioMale.setChecked(false);
        });

        // Register Button Click Handler
        buttonRegister.setOnClickListener(v -> {
            if (validateFields(editTextName, editTextPassword, editTextEmail, editTextPhone, editTextDOB, radioMale, radioFemale)) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Register.this, ActivityDashBoard.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to show the DatePickerDialog
    private void showDatePicker(EditText editTextDOB) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date
                    String dob = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);

                    // Calculate age
                    int age = calculateAge(selectedYear, selectedMonth + 1, selectedDay);

                    // Update EditText with date and age
                    editTextDOB.setText(dob + " (You are " + age + " years old)");
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    // Helper method to calculate age
    private int calculateAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        dob.set(year, month - 1, day);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    // Validation Method
    private boolean validateFields(EditText name, EditText password, EditText email, EditText phone, EditText dob, RadioButton male, RadioButton female) {
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Password is required");
            return false;
        }

        if (TextUtils.isEmpty(email.getText().toString()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Valid email is required");
            return false;
        }

        String phoneText = phone.getText().toString();
        if (TextUtils.isEmpty(phoneText) || !phoneText.matches("^(\\+60|0)[1-9][0-9]{7,9}$")) {
            phone.setError("Valid Malaysian phone number is required (e.g., +60123456789 or 0123456789)");
            return false;
        }

        if (TextUtils.isEmpty(dob.getText().toString())) {
            dob.setError("Date of Birth is required");
            return false;
        }

        if (!male.isChecked() && !female.isChecked()) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
