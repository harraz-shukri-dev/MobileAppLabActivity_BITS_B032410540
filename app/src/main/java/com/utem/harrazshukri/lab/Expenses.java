package com.utem.harrazshukri.lab;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.utem.harrazshukri.lab.databinding.ActivityExpensesBinding;
import com.utem.harrazshukri.lab.sqlite.DatabaseExpense;

import java.util.Calendar;

public class Expenses extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100; // Request code for camera intent
    private static final int CAMERA_PERMISSION_CODE = 200; // Request code for camera permission

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ActivityExpensesBinding binding;

    private ExpensesAdapter expensesAdapter;
    private DatabaseExpense databaseExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpensesBinding.inflate(getLayoutInflater());
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

        // Populate Spinner with quantity options
        Integer[] numbers = new Integer[15];
        for (int i = 0; i < 15; i++) {
            numbers[i] = i + 1;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnQty.setAdapter(adapter);

        // Set button click listeners
        binding.btnSaveExpenses.setOnClickListener(this::fnSaveExp);

        // Set image click listener to open the camera
        binding.imgExp.setOnClickListener(this::fnRequestCameraPermission);

        // Date picker for date field
        binding.editExpDate.setOnClickListener(view -> fnInvokeDatePicker());

        // Set a placeholder image initially (optional)
        binding.imgExp.setImageResource(R.drawable.baseline_camera_alt_24);

        databaseExpense = new DatabaseExpense(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fnSaveExp(View view) {
        try {
            int qtyItem = Integer.parseInt(binding.spnQty.getSelectedItem().toString());
            float expValue = Float.parseFloat(binding.editExpValue.getText().toString());
            float totalPrice = qtyItem * expValue;
            binding.txtVwTotalPrice.setText(String.format("Total Price RM: %.2f", totalPrice));
        } catch (NumberFormatException e) {
            binding.txtVwTotalPrice.setText("Invalid input for quantity or expense value.");
        }

        binding.txtVwTotalPrice.setText("" + qtyItem * Float.parseFloat(binding.editExpValue.getText().toString()));
        try {
            int resCode = databaseExpense.fnInsertExpense(expense);
            if (resCode > 0) {
                Toast.makeText(this, "Information saved locally", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Information not saved locally", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception eeish) {
            eeish.printStackTrace();
        }
        expenses.add(expense);
        expensesAdapter.notifyDataSetChanged();
    }

    // Check and request camera permission
    private void fnRequestCameraPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted; request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            // Permission already granted; open the camera
            fnTakePic();
        }
    }

    // Open camera to capture picture
    private void fnTakePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.imgExp.setImageBitmap(bitmap); // Display the captured image
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; open the camera
                fnTakePic();
            } else {
                // Permission denied; show a message
                Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fnInvokeDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog pickerDialog = new DatePickerDialog(this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            binding.editExpDate.setText(selectedDate);
        }, year, month, day);
        pickerDialog.show();
    }
}