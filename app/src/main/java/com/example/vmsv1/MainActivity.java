package com.example.vmsv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.vmsv1.ui.ResetPassword;
import com.example.vmsv1.ui.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    String userId,defaultGateId,sbuId;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin, R.id.nav_user,R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
            defaultGateId = intent.getStringExtra("defaultGateId");
            sbuId = intent.getStringExtra("sbuId");

            Log.d("Act","mainact userid="+userId);
            Log.d("Act","mainact sbuid="+sbuId);
            Log.d("Act","mainact defaultGateId="+defaultGateId);

            sharedViewModel.setUserId(userId);
            sharedViewModel.setSbuId(sbuId);
            sharedViewModel.setDefaultGateId(defaultGateId);
        }
        navController.navigate(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset_password) {
            Intent intent = new Intent(this, ResetPassword.class);
            intent.putExtra("defaultGateId",defaultGateId);
            intent.putExtra("sbuId",sbuId);
            intent.putExtra("userId_admin",userId);
            intent.putExtra("userId_to_be_changed",userId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}