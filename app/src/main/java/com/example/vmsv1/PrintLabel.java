package com.example.vmsv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ImageView imageViewRight;
    private ProgressBar progressBar;

    private long uniqueId;
    private int userId;
    private int gateId;
    private int sbuId;

    private ExecutorService executorService;
    private Handler mainHandler;

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
        imageViewRight = findViewById(R.id.imageViewRight);
        progressBar = findViewById(R.id.progressBar3);

        db = new DatabaseHelperSQL();

        Intent i = getIntent();
        if (i.hasExtra("uniqueId")) {
            uniqueId = Long.parseLong(i.getStringExtra("uniqueId"));
            sbuId = Integer.parseInt(i.getStringExtra("sbuId"));
            userId = Integer.parseInt(i.getStringExtra("userId"));
            gateId = Integer.parseInt(i.getStringExtra("gateId"));
            Log.d("Intent", "onCreate: Received uniqueId: " + uniqueId);
        } else {
            Log.d("MissingIntent", "onCreate: Missing intent extras 'uniqueId'");
        }

        // Initialize ExecutorService and Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Show ProgressBar and fetch visitor details in background
        progressBar.setVisibility(View.VISIBLE);
        fetchVisitorDetailsInBackground();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to MainActivity
                Intent intentBack = new Intent(PrintLabel.this, MainActivity.class);
                intentBack.putExtra("userId",String.valueOf(userId));
                intentBack.putExtra("defaultGateId",String.valueOf(gateId));
                intentBack.putExtra("sbuId",String.valueOf(sbuId));
                startActivity(intentBack);
                finish(); // Optional, if you want to close the current activity
            }
        });

    }


    private void fetchVisitorDetailsInBackground() {
        executorService.execute(() -> {
            //long uniqueId = 8;  // Replace with actual uniqueId retrieval logic
            VisitorSearchResult v = db.getVisitorDetails(uniqueId);

            Bitmap visitorPhoto = null;
            if (v != null && v.getPhotoFilePath() != null) {
                visitorPhoto = loadImageFromFile(v.getPhotoFilePath(), v.getPhotoFileName());
            }
            final Bitmap finalVisitorPhoto = visitorPhoto;

            // Update UI on the main thread
            mainHandler.post(() -> {
                if (v != null) {
                    if (v.getVisitorId() != null) {
                        visitorId.setText(v.getVisitorId());
                    }
                    if (v.getVisitorName() != null) {
                        visitorName.setText(v.getVisitorName());
                    }
                    if (v.getVisitorCompany() != null) {
                        visitorCompany.setText(v.getVisitorCompany());
                    }
                    if (v.getVisitorPlace() != null) {
                        visitorPlace.setText(v.getVisitorPlace());
                    }
                    if (v.getMobileNo() != null) {
                        visitorContactNum.setText(v.getMobileNo());
                    }
                    if (v.getVisitingFaculty() != null) {
                        visitingStaff.setText(v.getVisitingFaculty());
                    }
                    if (v.getEntryDatetime() != null) {
                        entryDateTime.setText(v.getEntryDatetime());
                    }
                    if (v.getExitDatetime() != null) {
                        exitDateTime.setText(v.getExitDatetime());
                    } else {
                        exitDateTime.setText("_");
                    }
                    if (finalVisitorPhoto != null) {
                        imageViewRight.setImageBitmap(finalVisitorPhoto);
                    }
                }
                // Hide ProgressBar after data is loaded
                progressBar.setVisibility(View.GONE);
                printDocument(v);
            });
        });
    }

    private Bitmap loadImageFromFile(String filePath, String fileName) {
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Log.e("PrintLabel", "Image file does not exist: " + imageFile.getAbsolutePath());
            return null;
        }
    }

    private void printDocument(VisitorSearchResult v) {
        // Get the PrintManager instance
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        // Create a print adapter instance
        PrintDocumentAdapter printAdapter = new MyPrintDocumentAdapter(this,v);

        // Create a print job with name and adapter
        PrintJob printJob = printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());

        // Check the status of the print job (optional)
        new Handler().postDelayed(() -> {
            if (printJob.isCompleted()) {
                Toast.makeText(PrintLabel.this, "Print job completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isFailed()) {
                Toast.makeText(PrintLabel.this, "Print job failed", Toast.LENGTH_SHORT).show();
            }
        }, 2000);  // Delay to check the print job status, adjust as needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up ExecutorService
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
