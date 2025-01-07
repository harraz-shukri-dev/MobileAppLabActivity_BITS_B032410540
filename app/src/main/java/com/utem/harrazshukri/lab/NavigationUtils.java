package com.utem.harrazshukri.lab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class NavigationUtils {

    public static void setupNavigation(Context context, DrawerLayout drawerLayout, NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent = null;

            if (item.getItemId() == R.id.nav_login) {
                intent = new Intent(context, Login.class);
            } else if (item.getItemId() == R.id.nav_register) {
                intent = new Intent(context, Register.class);
            } else if (item.getItemId() == R.id.nav_expenses) {
                intent = new Intent(context, Expenses.class);
            } else if (item.getItemId() == R.id.nav_student) {
                intent = new Intent(context, StudentForm.class);
            } else if (item.getItemId() == R.id.nav_getImage) {
                intent = new Intent(context, GetImage.class);
            }
            else if (item.getItemId() == R.id.nav_searchStudent) {
                intent = new Intent(context, SearchStudentActivity.class);
            }

            if (intent != null) {
                context.startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
