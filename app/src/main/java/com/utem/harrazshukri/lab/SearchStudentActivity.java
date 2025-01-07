package com.utem.harrazshukri.lab;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.utem.harrazshukri.lab.databinding.ActivitySearchStudentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchStudentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActivitySearchStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivitySearchStudentBinding.inflate(getLayoutInflater());
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

        // Set up Search Button Click Listener
        binding.btnSearch.setOnClickListener(this::fnSearch);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fnSearch(View view) {
        String studentID = binding.edtStudID.getText().toString().trim();

        if (studentID.isEmpty()) {
            Toast.makeText(this, "Please enter a student ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String strUrl = "http://10.200.93.95/labrestapi/rest_api.php"; // Replace with your server IP or URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(getApplicationContext(), "No student found!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject jsonObject = jsonArray.getJSONObject(0); // Assuming one student result

                    binding.txtVwStudName2.setText(jsonObject.getString("stud_name"));
                    binding.txtVwStudGender.setText(jsonObject.getString("stud_gender"));
                    binding.txtVwStudNo.setText(jsonObject.getString("stud_no"));
                    binding.txtVwStudState.setText(jsonObject.getString("stud_state"));

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Unable to fetch student info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "fnSearchStud"); // Match the PHP function name
                params.put("studNo", studentID);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
