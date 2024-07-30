package com.example.vmsv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.dataitems.NDADetails;
import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.db.NDADatabaseTask;
import com.example.vmsv1.ui.home.HomeFragment;

import java.util.List;

public class DisplayNDA extends AppCompatActivity {
    Button buttonAccept;
    Button buttonDecline;
    DatabaseHelperSQL dbsql;
    private String mobileNum;
    private int gateId, sbuId;
    private int userId;
    private long uniqueId;
    TextView lblNDAUnitAddress, lblNDAPreviewName1, lblNDAUnitDescription, lblNDAUnit2, lblNDAUnit3, lblNDAPreviewName2, lblNDAVersion, lblNDAPreviewDate1, lblNDAPreviewDesignation, lblNDAPreviewCompany, lblNDAPreviewVisitingArea, lblNDAPreviewApproverName, lblNDAPreviewDate2, lblNDAPreviewMailReference, lblNDAPreviewAssets, lblNDAPreviewSecurityName, lblNDAPreviewSecurityId, lblNDAPreviewVisitorId, lblNDAPreviewTime, lblNDAFileName;

    private static final String TAG = "DisplayNDA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nda);
        dbsql = new DatabaseHelperSQL();

        Log.d(TAG, "onCreate: Activity started");

        Intent i = getIntent();
        if (i.hasExtra("sbuId") && i.hasExtra("uniqueId")) {
            sbuId = Integer.parseInt(i.getStringExtra("sbuId"));
            uniqueId = Long.parseLong(i.getStringExtra("uniqueId"));
            userId = Integer.parseInt(i.getStringExtra("userId"));
            gateId = Integer.parseInt(i.getStringExtra("gateId"));
            Log.d(TAG, "onCreate: Received sbuId: " + sbuId + ", uniqueId: " + uniqueId);
        } else {
            Log.w(TAG, "onCreate: Missing intent extras 'sbuId' or 'uniqueId'");
        }

        lblNDAUnitAddress = findViewById(R.id.lblNDAUnitAddress);
        lblNDAPreviewName1 = findViewById(R.id.lblNDAPreviewName1);
        lblNDAUnitDescription = findViewById(R.id.lblNDAUnitDescription);
        lblNDAUnit2 = findViewById(R.id.lblNDAUnit2);
        lblNDAUnit3 = findViewById(R.id.lblNDAUnit3);
        lblNDAPreviewName2 = findViewById(R.id.lblNDAPreviewName2);
        lblNDAVersion = findViewById(R.id.lblNDAVersion);
        lblNDAPreviewDate1 = findViewById(R.id.lblNDAPreviewDate1);
        lblNDAPreviewDesignation = findViewById(R.id.lblNDAPreviewDesignation);
        lblNDAPreviewCompany = findViewById(R.id.lblNDAPreviewCompany);
        lblNDAPreviewVisitingArea = findViewById(R.id.lblNDAPreviewVistingArea);
        lblNDAPreviewApproverName = findViewById(R.id.lblNDAPreviewApproverName);
        lblNDAPreviewDate2 = findViewById(R.id.lblNDAPreviewDate2);
        lblNDAPreviewMailReference = findViewById(R.id.lblNDAPreviewMailReference);
        lblNDAPreviewAssets = findViewById(R.id.lblNDAPreviewAssets);
        lblNDAPreviewSecurityName = findViewById(R.id.lblNDAPreviewSecurityName);
        lblNDAPreviewSecurityId = findViewById(R.id.lblNDAPreviewSecurityId);
        lblNDAPreviewVisitorId = findViewById(R.id.lblNDAPreviewVisitorId);
        lblNDAPreviewTime = findViewById(R.id.lblNDAPreviewTime);
        lblNDAFileName = findViewById(R.id.lblNDAFileName);

        buttonAccept = findViewById(R.id.buttonAccept);
        buttonDecline = findViewById(R.id.buttonDecline);

        NDADatabaseTask dbTask = new NDADatabaseTask(dbsql);
        //uniqueId=5;
        //sbuId=1;

        dbTask.retrieveVisitorDetails(uniqueId, new NDADatabaseTask.OnNDADetailsRetrievedListener() {
            @Override
            public void onVisitorDetailsRetrieved(VisitorSearchResult vs) {
                if (vs != null) {
                    Log.d(TAG, "onVisitorDetailsRetrieved: Retrieved visitor details for uniqueId: " + uniqueId);
                    lblNDAPreviewName1.setText(vs.getVisitorName());
                    lblNDAPreviewName2.setText(vs.getVisitorName());
                    lblNDAPreviewDate1.setText(vs.getCreatedDate());
                    lblNDAPreviewDesignation.setText(vs.getVisitorDesignation());
                    lblNDAPreviewCompany.setText(vs.getVisitorCompany());
                    lblNDAPreviewVisitingArea.setText(vs.getVisitingAreaName());
                    lblNDAPreviewApproverName.setText(vs.getApproverName());
                    lblNDAPreviewDate2.setText(vs.getUpdatedDate());
                    lblNDAPreviewMailReference.setText(vs.getRefMail());
                    lblNDAPreviewSecurityName.setText(vs.getSecurityName());
                    lblNDAPreviewSecurityId.setText(String.valueOf(vs.getSecurityId()));
                    lblNDAPreviewVisitorId.setText(vs.getVisitorId());
                    lblNDAPreviewTime.setText(vs.getEntryDatetime());
                } else {
                    Log.w(TAG, "onVisitorDetailsRetrieved: Visitor details not found for uniqueId: " + uniqueId);
                }
            }

            @Override
            public void onNDADetailsRetrieved(List<NDADetails> ndaDetailsList) {
                if (!ndaDetailsList.isEmpty()) {
                    NDADetails obj = ndaDetailsList.get(0);
                    Log.d(TAG, "onNDADetailsRetrieved: Retrieved NDA details for sbuId: " + sbuId);
                    lblNDAUnitAddress.setText(obj.getUnitAddress());
                    lblNDAUnitDescription.setText(obj.getUnitDescription());
                    lblNDAUnit2.setText(obj.getVar2());
                    lblNDAUnit3.setText(obj.getVar3());
                    lblNDAFileName.setText(obj.getVar4());
                } else {
                    Log.w(TAG, "onNDADetailsRetrieved: NDA details not found for sbuId: " + sbuId);
                }
            }
        });

        dbTask.retrieveNDADetails(sbuId, new NDADatabaseTask.OnNDADetailsRetrievedListener() {
            @Override
            public void onNDADetailsRetrieved(List<NDADetails> ndaDetailsList) {
                if (!ndaDetailsList.isEmpty()) {
                    NDADetails obj = ndaDetailsList.get(0);
                    Log.d(TAG, "onNDADetailsRetrieved: Retrieved NDA details for sbuId: " + sbuId);
                    lblNDAUnitAddress.setText(obj.getUnitAddress());
                    lblNDAUnitDescription.setText(obj.getUnitDescription());
                    lblNDAUnit2.setText(obj.getVar2());
                    lblNDAUnit3.setText(obj.getVar3());
                } else {
                    Log.w(TAG, "onNDADetailsRetrieved: NDA details not found for sbuId: " + sbuId);
                }
            }

            @Override
            public void onVisitorDetailsRetrieved(VisitorSearchResult visitorDetails) {
                // No action needed here for NDA details
            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Accept button clicked");
                //set NDA status as 'Y' i.e., accepted
                Toast.makeText(DisplayNDA.this, "NDA accepted", Toast.LENGTH_SHORT).show();
                Intent intentPrint = new Intent(DisplayNDA.this, PrintLabel.class);
                intentPrint.putExtra("uniqueId",String.valueOf(uniqueId));
                intentPrint.putExtra("userId",String.valueOf(userId));
                intentPrint.putExtra("gateId",String.valueOf(gateId));
                intentPrint.putExtra("sbuId",String.valueOf(sbuId));
                startActivity(intentPrint);
            }
        });

        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Decline button clicked");
                //set NDA status as N ie declined
                Toast.makeText(DisplayNDA.this, "NDA declined", Toast.LENGTH_SHORT).show();
                Intent intentHome = new Intent(DisplayNDA.this, MainActivity.class);
                startActivity(intentHome);
            }
        });
    }
}
