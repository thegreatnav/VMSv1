package com.example.vmsv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.R;
import com.example.vmsv1.PrintLabel;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.ui.home.HomeFragment;

public class DisplayNDA extends AppCompatActivity {
    Button buttonAccept;
    Button buttonDecline;
    DatabaseHelperSQL dbsql;
    private String mobileNum;
    private int gateId,sbuId;
    private int userId;
    TextView lblNDAUnitAddress,lblNDAPreviewName1,lblNDAUnitDescription,lblNDAUnit2,lblNDAUnit3,lblNDAPreviewName2,lblNDAVersion,lblNDAPreviewDate1,lblNDAPreviewDesignation,lblNDAPreviewCompany,lblNDAPreviewVisitingArea,lblNDAPreviewApproverName,lblNDAPreviewDate2,lblNDAPreviewMailReference,lblNDAPreviewAssets,lblNDAPreviewSecurityName,lblNDAPreviewSecurityId,lblNDAPreviewVisitorId,lblNDAPreviewTime,lblNDAFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nda);
        dbsql=new DatabaseHelperSQL();

        Intent i=getIntent();
        if(i.hasExtra("sbuId"))
        {
            sbuId= Integer.parseInt(i.getStringExtra("sbuId"));
        }

        lblNDAUnitAddress = findViewById(R.id.lblNDAUnitAddress);
        lblNDAPreviewName1= findViewById(R.id.lblNDAPreviewName1);
        lblNDAUnitDescription = findViewById(R.id.lblNDAUnitDescription);
        lblNDAUnit2 = findViewById(R.id.lblNDAUnit2);
        lblNDAUnit3= findViewById(R.id.lblNDAUnit3);
        lblNDAPreviewName2= findViewById(R.id.lblNDAPreviewName2);
        lblNDAVersion= findViewById(R.id.lblNDAVersion);
        lblNDAPreviewDate1= findViewById(R.id.lblNDAPreviewDate1);
        lblNDAPreviewDesignation= findViewById(R.id.lblNDAPreviewDesignation);
        lblNDAPreviewCompany= findViewById(R.id.lblNDAPreviewCompany);
        lblNDAPreviewVisitingArea= findViewById(R.id.lblNDAPreviewVistingArea);
        lblNDAPreviewApproverName= findViewById(R.id.lblNDAPreviewApproverName);
        lblNDAPreviewDate2= findViewById(R.id.lblNDAPreviewDate2);
        lblNDAPreviewMailReference= findViewById(R.id.lblNDAPreviewMailReference);
        lblNDAPreviewAssets= findViewById(R.id.lblNDAPreviewAssets);
        lblNDAPreviewSecurityName= findViewById(R.id.lblNDAPreviewSecurityName);
        lblNDAPreviewSecurityId= findViewById(R.id.lblNDAPreviewSecurityId);
        lblNDAPreviewVisitorId= findViewById(R.id.lblNDAPreviewVisitorId);
        lblNDAPreviewTime= findViewById(R.id.lblNDAPreviewTime);
        lblNDAFileName= findViewById(R.id.lblNDAFileName);

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
                Intent intentHome = new Intent(DisplayNDA.this, MainActivity.class);
                startActivity(intentHome);
            }
        });
    }
}
