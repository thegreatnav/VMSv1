package com.example.vmsv1.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vmsv1.MainActivity;
import com.example.vmsv1.R;

import com.example.vmsv1.dataitems.UserProfile;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    ProgressBar progressBar;

    DatabaseHelperSQL dbsql;
    String userId, defaultGateId, sbuId;

    private Button login_btn;
    private EditText password;
    private EditText username;
    private String androidId;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login_btn = findViewById(R.id.login);
        password = findViewById(R.id.ps);
        username = findViewById(R.id.un);
        progressBar=findViewById(R.id.progressBar2);

        dbsql = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        // Get the Android ID
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("AndroidID", "Android ID: " + androidId);

        String ans=dbsql.getUserIdByAndroidId(androidId);
        if(ans.equals(""))
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Android Id not registered in database",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"User Id : "+ans+" detected",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            handler.postDelayed(() -> {
            UserProfile user = dbsql.getUserDetails(Integer.parseInt(ans));

            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            intent.putExtra("userId", ans);
            intent.putExtra("sbuId", user.getSbuId());
            intent.putExtra("FullName", user.getFullName());
            intent.putExtra("defaultGateId", user.getDefaultGateId());
            intent.putExtra("androidId", androidId);
            startActivity(intent);
            }, 1000);
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us = username.getText().toString().trim();
                String p = password.getText().toString().trim();

                if (us.isEmpty() || p.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<String> values = dbsql.loginValidation(us, p);

                if (values.get(0).equals("0")) {
                    Toast.makeText(LoginPage.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                } else if (!values.get(3).equals("Active")) {
                    Toast.makeText(LoginPage.this, "User is Inactive", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    String UserID = values.get(0);
                    String FullName = values.get(4);
                    String SbuID = values.get(5);
                    String DefaultGateId = values.get(6);

                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    intent.putExtra("userId", UserID);
                    intent.putExtra("sbuId", SbuID);
                    intent.putExtra("FullName", FullName);
                    intent.putExtra("defaultGateId", DefaultGateId);
                    intent.putExtra("androidId", androidId); // Passing Android ID
                    startActivity(intent);
                }
            }
        });
    }
}
