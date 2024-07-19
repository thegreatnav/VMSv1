package com.example.vmsv1.ui;

// ResetPassword.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.vmsv1.db.DatabaseHelperSQL;

import com.example.vmsv1.R;

public class ResetPassword extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    private Button saveButton;
    private DatabaseHelperSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        db = new DatabaseHelperSQL();

        oldPassword = findViewById(R.id.old_ps);
        newPassword = findViewById(R.id.new_ps);
        confirmPassword = findViewById((R.id.confirm_ps));
        saveButton = findViewById(R.id.save_new_password);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPassword == newPassword) {
                    //Toast.makeText(this, "Enter a new password", Toast.LENGTH_SHORT).show();
                }
                if (newPassword != confirmPassword) {
                   // Toast.makeText(this, "Confirm password doesn't match", Toast.LENGTH_SHORT).show();
                }
                else {

                }
            }
        }
        );

        //db.resetUserPassword()

    }
}
