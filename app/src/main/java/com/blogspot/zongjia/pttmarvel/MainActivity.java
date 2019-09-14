package com.blogspot.zongjia.pttmarvel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.blogspot.zongjia.pttmarvel.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = binding.drawerLayout;

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


                switch (menuItem.getItemId()) {
                    case R.id.mySettingsFragment:
                        navController.navigate(NavGrpahDirections.actionGlobalMySettingsFragment());
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.kanbanManageFagment:
                        navController.navigate(NavGrpahDirections.actionGlobalKanbanManageFagment());
                        drawerLayout.closeDrawers();

                    case R.id.kanban_gossiping:
                        pref.edit().putString("kanban", "Gossiping").apply();
                        navController.navigate(NavGrpahDirections.actionGlobalArticleListFragment());
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.kanban_beauty:
                        pref.edit().putString("kanban", "beauty").apply();
                        navController.navigate(NavGrpahDirections.actionGlobalArticleListFragment());
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.kanban_marvel:
                        pref.edit().putString("kanban", "marvel").apply();
                        navController.navigate(NavGrpahDirections.actionGlobalArticleListFragment());
                        drawerLayout.closeDrawers();
                        break;


                }
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }


}
