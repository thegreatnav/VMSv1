package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.dataitems.UserProfile;
import com.example.vmsv1.db.DatabaseHelperSQL;

import com.example.vmsv1.R;

import java.util.List;
import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    private EditText oldPassword_textview;
    private EditText newPassword_textview;
    private EditText confirmPassword_textview;

    private Button saveButton,verifyButton;
    private DatabaseHelperSQL db;
    private Handler handler;
    String userId_admin,userId_to_be_changed,defaultGateId,sbuId,oldPassword,newPassword,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        if (intent != null) {
            userId_admin = intent.getStringExtra("userId_admin");
            Log.d("Tag5",String.valueOf(userId_admin));
            userId_to_be_changed=intent.getStringExtra("userId_to_be_changed");
            defaultGateId = intent.getStringExtra("defaultGateId");
            sbuId = intent.getStringExtra("sbuId");

            Log.d("ResetPassword", "userId_admin: " + userId_admin);
            Log.d("ResetPassword", "userId_to_be_changed: " + userId_to_be_changed);
            Log.d("ResetPassword", "DefaultGateId: " + defaultGateId);
            Log.d("ResetPassword", "SbuId: " + sbuId);
        }

        db = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        oldPassword_textview = findViewById(R.id.old_ps);
        newPassword_textview = findViewById(R.id.new_ps);
        confirmPassword_textview = findViewById((R.id.confirm_ps));
        saveButton = findViewById(R.id.save_new_password);
        verifyButton=findViewById(R.id.verify_button);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword=oldPassword_textview.getText().toString();
                Log.d("Tag4","oldpassword="+oldPassword);
                List<String> password_dbs=db.getUserPassword(Integer.parseInt(userId_to_be_changed));
                Log.d("Tag4",password_dbs.get(0));
                if(oldPassword.equals(String.valueOf(password_dbs.get(0))))
                {
                    verifyButton.setText("Verified");
                    verifyButton.setEnabled(false);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password incorrect",Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verifyButton.isEnabled())
                    Toast.makeText(getApplicationContext(), "Verify old password first.", Toast.LENGTH_SHORT).show();
                else {
                    oldPassword = oldPassword_textview.getText().toString();
                    newPassword = newPassword_textview.getText().toString();
                    confirmPassword = confirmPassword_textview.getText().toString();

                    if (Objects.equals(oldPassword, newPassword)) {
                        Toast.makeText(getApplicationContext(), "You've entered the same password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                            if (!Objects.equals(newPassword, confirmPassword)) {
                                Toast.makeText(getApplicationContext(), "Confirm password doesn't match", Toast.LENGTH_SHORT).show();
                            } else {
                                List<String> message = db.resetUserPassword(userId_to_be_changed, confirmPassword, Integer.parseInt(userId_admin));
                                Log.d("Tag1", "message=" + String.valueOf(message.get(1)));
                                if (message.get(1).equals("Count: 0"))
                                    Toast.makeText(getApplicationContext(), "Update not successful", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();

                                handler.postDelayed(() -> {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }, 1000);
                            }

                    }
                }
            }
        }
        );

        //db.resetUserPassword()

    }
}
