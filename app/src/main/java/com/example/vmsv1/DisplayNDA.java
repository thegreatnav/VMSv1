package com.example.vmsv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.R;
import com.example.vmsv1.PrintLabel;

public class DisplayNDA extends AppCompatActivity {
    Button buttonAccept;
    Button buttonDecline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nda);

        buttonAccept = findViewById(R.id.buttonAccept);
        buttonDecline= findViewById(R.id.buttonDecline);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DisplayNDA.this, "NDA accepted", Toast.LENGTH_SHORT).show();
                Intent intentPrint = new Intent(DisplayNDA.this, PrintLabel.class);
                startActivity(intentPrint);
            }
        });

        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DisplayNDA.this, "NDA declined", Toast.LENGTH_SHORT).show();
                //Intent intentPrint = new Intent(DisplayNDA.this,PrintLabel.class);
                //startActivity(intentPrint);
            }
        });
    }
}
