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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.utem.harrazshukri.lab.databinding.ActivityExpensesBinding;
import com.utem.harrazshukri.lab.sqlite.DatabaseExpense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityExpense extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 200;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActivityExpensesBinding binding;
    private DatabaseExpense databaseExpense;

    private List<Expense> expenses;
    private ExpenseAdapter expenseAdapter;

    private AppDatabase appDatabase;
    private ExpensesDAO expensesDAO;

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

        // Navigation setup
        NavigationUtils.setupNavigation(this, binding.myDrawerLayout, binding.navigation);

        // Populate Spinner
        Integer[] numbers = new Integer[15];
        for (int i = 0; i < 15; i++) {
            numbers[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnQty.setAdapter(adapter);

        // Initialize databases and adapter
        databaseExpense = new DatabaseExpense(this);
        appDatabase = AppDatabase.getDatabase(this);
        expensesDAO = appDatabase.expensesDAO();
        expenses = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, expenses);
        binding.rcvExp.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvExp.setAdapter(expenseAdapter);

        // Load all expenses from Room database
        loadExpensesFromDatabase();

        // Set listeners
        binding.btnSaveExpenses.setOnClickListener(this::fnSaveExp);
        binding.imgExp.setOnClickListener(this::fnRequestCameraPermission);
        binding.editExpDate.setOnClickListener(view -> fnInvokeDatePicker());
        binding.imgExp.setImageResource(R.drawable.baseline_camera_alt_24);
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
            String name = binding.editExpName.getText().toString().trim();
            String date = binding.editExpDate.getText().toString().trim();
            String valueText = binding.editExpValue.getText().toString().trim();
            String qtyText = binding.spnQty.getSelectedItem().toString();


            if (name.isEmpty() || date.isEmpty() || valueText.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            float value = Float.parseFloat(valueText);
            int qty = Integer.parseInt(qtyText);
            float total = value * qty;
            // Display the total in the TextView
            binding.txtVwTotalPrice.setText(String.format("Total: %.2f", total));

            Expense expense = new Expense(name, date, value, qty, total);

            // Save to SQLite
            long sqliteResCode = databaseExpense.fnInsertExpense(expense);
            if (sqliteResCode > 0) {
                Toast.makeText(this, "Saved locally (SQLite)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save locally (SQLite)", Toast.LENGTH_SHORT).show();
            }

            // Save to Room database (Async)
            new Thread(() -> {
                expensesDAO.insertExpense(expense);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Saved locally (Room)", Toast.LENGTH_SHORT).show();
                    loadExpensesFromDatabase(); // Refresh the RecyclerView
                });
            }).start();

            // Save to remote database (mocked)
            saveToRemoteDatabase(expense);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input for quantity or expense value.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpensesFromDatabase() {
        new Thread(() -> {
            List<Expense> expenseList = expensesDAO.getAllExpenses(); // Fetch all expenses from Room
            runOnUiThread(() -> {
                expenses.clear();
                expenses.addAll(expenseList); // Update the adapter's data
                expenseAdapter.notifyDataSetChanged(); // Notify adapter about data change
            });
        }).start();
    }

    private void saveToRemoteDatabase(Expense expense) {
        // Simulate remote database save
        Toast.makeText(this, "Saved remotely (mocked)", Toast.LENGTH_SHORT).show();
    }

    private void fnRequestCameraPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            fnTakePic();
        }
    }

    private void fnTakePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.imgExp.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fnTakePic();
            } else {
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
