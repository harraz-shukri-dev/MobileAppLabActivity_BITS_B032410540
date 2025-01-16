package com.utem.harrazshukri.lab;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.utem.harrazshukri.lab.databinding.ActivityDashBoardBinding;

import com.google.android.material.navigation.NavigationView;


public class ActivityDashBoard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ActivityDashBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
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

        // Set OnClickListener for Student Card
        findViewById(R.id.cardStudent).setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDashBoard.this, StudentForm.class);
            startActivity(intent);
        });

        // Set OnClickListener for Search Student Card
        findViewById(R.id.cardSearchStudent).setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDashBoard.this, SearchStudentActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for Expenses Card
        findViewById(R.id.cardExpenses).setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDashBoard.this, ActivityExpense.class);
            startActivity(intent);
        });

        // Set OnClickListener for Get Image Card
        findViewById(R.id.cardGetImage).setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDashBoard.this, GetImage.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
