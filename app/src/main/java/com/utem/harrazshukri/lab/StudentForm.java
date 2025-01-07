package com.utem.harrazshukri.lab;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import com.utem.harrazshukri.lab.databinding.ActivityStudentFormBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StudentForm extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActivityStudentFormBinding binding;
    private Vector<Student> students;
    private StudentAdapter adapter;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityStudentFormBinding.inflate(getLayoutInflater());
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

        // Date Picker for Birthdate
        binding.edtStuBirthdate.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            datePicker = new DatePickerDialog(StudentForm.this, (view1, year1, monthOfYear, dayOfMonth) -> {
                String formattedDate = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                binding.edtStuBirthdate.setText(formattedDate);
            }, year, month, day);
            datePicker.show();
        });

        // Initialize RecyclerView and Adapter
        students = new Vector<>();
        adapter = new StudentAdapter(getLayoutInflater(), students);
        binding.rcvStud.setAdapter(adapter);
        binding.rcvStud.setLayoutManager(new LinearLayoutManager(this));

        // Add Student to REST API
        binding.fabAdd.setOnClickListener(this::fnAddToRest);
    }

    private void fnAddToRest(View view) {
        String fullname = binding.edtStuFullName.getText().toString().trim();
        String studNo = binding.edtStudNum.getText().toString().trim();
        String email = binding.edtStuEmail.getText().toString().trim();
        String birth = binding.edtStuBirthdate.getText().toString().trim();
        String gender = binding.rbMale.isChecked() ? "Male" : binding.rbFemale.isChecked() ? "Female" : "";
        String state = binding.spnState.getSelectedItem().toString();

        if (fullname.isEmpty() || studNo.isEmpty() || email.isEmpty() || birth.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        String strUrl = "http://10.200.93.95/labrestapi/rest_api.php"; // Replace with actual server IP
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, strUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String serverResponse = jsonObject.getString("respond");
                        Toast.makeText(getApplicationContext(), "Server: " + serverResponse, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "JSON Parsing Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "fnSaveData");
                params.put("studName", fullname);
                params.put("studNo", studNo);
                params.put("studGender", gender);
                params.put("studDob", birth);
                params.put("studState", state);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
