package com.utem.harrazshukri.lab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.utem.harrazshukri.lab.databinding.ActivityGetImageBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GetImage extends AppCompatActivity {

    private ImageView imgVwSelfie;
    private static final int SELECT_PHOTO = 100;
    private static final int REQUEST_CAMERA = 101;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ActivityGetImageBinding binding;
    private Executor executor;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Executor for background tasks
        executor = Executors.newSingleThreadExecutor();
        // Handler for posting tasks to the main thread
        mainHandler = new Handler(Looper.getMainLooper());

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

        imgVwSelfie = binding.imgVwSelfie;
        MaterialButton btnUploadImage = binding.btnUploadImage;

        btnUploadImage.setOnClickListener(view -> {
            showUploadOptionsDialog();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Show dialog for image upload options
    private void showUploadOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source")
                .setItems(new String[]{"Upload by URL", "Take a Picture", "Choose from Gallery"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Upload by URL
                            showUrlInputDialog();
                            break;
                        case 1:
                            // Take a picture
                            openCamera();
                            break;
                        case 2:
                            // Choose from gallery
                            openGallery();
                            break;
                    }
                })
                .show();
    }

    // Show input dialog for URL upload
    private void showUrlInputDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter Image URL");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image URL")
                .setView(input)
                .setPositiveButton("Upload", (dialog, which) -> {
                    String imageUrl = input.getText().toString().trim();
                    if (!imageUrl.isEmpty()) {
                        // Run image loading in background using Executor
                        loadImageFromUrl(imageUrl);
                    } else {
                        Toast.makeText(this, "URL cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Load image from URL (running in background thread)
    private void loadImageFromUrl(String url) {
        executor.execute(() -> {
            try {
                // Simulating some background task, e.g., network operation
                Thread.sleep(2000); // Simulate network delay

                // Post result to main thread
                mainHandler.post(() -> {
                    Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.sharp_add_a_photo_24)  // Add a placeholder image
                            .error(R.drawable.baseline_cancel_24)  // Add an error image
                            .into(imgVwSelfie);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    // Open camera to take a picture
    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    // Open gallery to choose an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO && data != null) {
                Uri selectedImage = data.getData();
                imgVwSelfie.setImageURI(selectedImage);
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgVwSelfie.setImageBitmap(imageBitmap);
            }
        }
    }
}
