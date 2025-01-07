package com.utem.harrazshukri.lab;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.utem.harrazshukri.lab.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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
        EditText editTextUsername = binding.editTextText;
        EditText editTextPassword = binding.editTextTextPassword;
        Button btnLogin = binding.btnLogin;

        // Login Button Click Handler
        btnLogin.setOnClickListener(v -> {
            if (validateFields(editTextUsername, editTextPassword)) {
                // Pass data to Register
                Intent intent = new Intent(this, Register.class);
                intent.putExtra("USERNAME", editTextUsername.getText().toString());
                intent.putExtra("PASSWORD", editTextPassword.getText().toString());
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

    // Validation Method
    private boolean validateFields(EditText username, EditText password) {
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Password is required");
            return false;
        }

        return true;
    }
}
