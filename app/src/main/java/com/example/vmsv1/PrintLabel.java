package com.example.vmsv1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;

import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.db.DatabaseHelperSQL;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.MyPrintDocumentAdapter;
import com.example.vmsv1.R;

public class PrintLabel extends AppCompatActivity {

    DatabaseHelperSQL db;
    private TextView visitorId;
    private TextView visitorName;
    private TextView visitorCompany;
    private TextView visitorPlace;
    private TextView visitorContactNum;
    private TextView visitingStaff;
    private TextView entryDateTime;
    private TextView exitDateTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_label);

        visitorId = findViewById(R.id.visitorIdLabel);
        visitorName = findViewById(R.id.visitorNameLabel);
        visitorCompany = findViewById(R.id.visitorCompanyLabel);
        visitorPlace = findViewById(R.id.visitorPlaceLabel);
        visitorContactNum = findViewById(R.id.visitorContactNumLabel);
        visitingStaff = findViewById(R.id.visitingStaffLabel);
        entryDateTime = findViewById(R.id.entryDateTimeLabel);
        exitDateTime = findViewById(R.id.exitDateTimeLabel);

        db = new DatabaseHelperSQL();

        setVisitorDetails();
        printDocument();
    }

    private void setVisitorDetails()
    {
     long uniqueId = 6;
     VisitorSearchResult v =db.getVisitorDetails(uniqueId);
     if(v.getVisitorId()!=null)
     {
         visitorId.setText(v.getVisitorId());
     }
     if (v.getVisitorName()!=null)
     {
         visitorName.setText(v.getVisitorName());
     }
        if (v.getVisitorCompany()!=null)
        {
            visitorCompany.setText(v.getVisitorCompany());
        }
        if (v.getVisitorPlace()!=null)
        {
            visitorPlace.setText(v.getVisitorPlace());
        }
        if (v.getMobileNo()!=null)
        {
            visitorContactNum.setText(v.getMobileNo());
        }
        if (v.getVisitingFaculty()!=null)
        {
            visitingStaff.setText(v.getVisitingFaculty());
        }
        if (v.getEntryDatetime()!=null)
        {
            entryDateTime.setText(v.getEntryDatetime());
        }
            exitDateTime.setText("_");
    }

    private void printDocument() {
        // Get the PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Create a print adapter instance
        PrintDocumentAdapter printAdapter = new MyPrintDocumentAdapter(this);

        // Create a print job with name and adapter

                // Code to run after the delay
                PrintJob printJob = printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());



        // Check the status of the print job (optional)
        if (printJob.isCompleted()) {
            Toast.makeText(PrintLabel.this, "Print job completed", Toast.LENGTH_SHORT).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(PrintLabel.this, "Print job failed", Toast.LENGTH_SHORT).show();
        }
    }
}
