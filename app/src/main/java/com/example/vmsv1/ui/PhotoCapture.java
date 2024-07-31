package com.example.vmsv1.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.vmsv1.DisplayNDA;
import com.example.vmsv1.ui.VisitorEntry;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.dataitems.VisitorSearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoCapture extends AppCompatActivity {

    private String mobileNum;
    private int gateId,sbuId;
    private int userId;
    private int idprooftype,uniqueId;
    private String numidproof;
    private String savedImageFilename;
    private String savedImageFilepath;

    private ImageView imageViewPhoto;
    private Button buttonCapture;
    private Button buttonSaveImage;
    private EditText editTextFilename;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    private DatabaseHelperSQL dbsql;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);

        dbsql = new DatabaseHelperSQL();
        executorService = Executors.newFixedThreadPool(4);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonSaveImage = findViewById(R.id.buttonSaveImage);
        editTextFilename = findViewById(R.id.editTextFileName);

        handleIntent(getIntent());

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                    }
                });

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageViewPhoto.setImageBitmap(imageBitmap);
                        editTextFilename.setVisibility(View.VISIBLE);
                        buttonSaveImage.setVisibility(View.VISIBLE);
                        buttonSaveImage.setOnClickListener(v -> saveImageToFile(imageBitmap));
                    }
                });

        buttonCapture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            } else {
                openCamera();
            }
        });
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            mobileNum = intent.getStringExtra("VisitorEntry.MobileNumber");
            gateId = Integer.parseInt(intent.getStringExtra("VisitorEntry.gateId"));
            userId = Integer.parseInt(intent.getStringExtra("VisitorEntry.userId"));
            idprooftype = Integer.parseInt(intent.getStringExtra("VisitorEntry.ID"));
            sbuId=Integer.parseInt(intent.getStringExtra("VisitorEntry.sbuId"));
            uniqueId=Integer.parseInt(intent.getStringExtra("unique_Id"));
            if(intent.hasExtra("IDProofNum"))
                numidproof = intent.getStringExtra("IDProofNum");
            Log.d("Intent Data", "mobileNum: " + mobileNum + ", gateId: " + gateId + ", userId: " + userId + ", idprooftype: " + idprooftype + ", numidproof: " + numidproof + ", sbuId: " + sbuId);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageToFile(Bitmap bitmap) {
        String customFilename = editTextFilename.getText().toString().trim();

        if (TextUtils.isEmpty(customFilename)) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!photoDirectory.exists() && !photoDirectory.mkdirs()) {
            Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile = new File(photoDirectory, customFilename + ".jpg");

        executorService.execute(() -> {
            try (FileOutputStream fos = new FileOutputStream(photoFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                savedImageFilename = photoFile.getName();
                savedImageFilepath = photoFile.getAbsolutePath();

                long uniqueId = getUniqueId();

                if (idprooftype > 0) {
                    if (idprooftype != 1 && idprooftype != 6) {
                        List<String> update = dbsql.updateVisitorIDProofDetails(uniqueId, idprooftype, numidproof, savedImageFilepath, savedImageFilename);
                        runOnUiThread(() -> {
                            if (!update.get(0).equals("Id : null")) {
                                Toast.makeText(this, "ID Proof saved to " + savedImageFilepath + " and database", Toast.LENGTH_SHORT).show();
                                Log.d("Idproof", "ID Proof saved to " + savedImageFilepath + " and database");
                            } else {
                                Toast.makeText(this, "ID Proof not saved to database", Toast.LENGTH_SHORT).show();
                                Log.d("Idproof", "ID Proof not saved to database");
                            }
                            navigateToVisitorEntry();
                        });
                    } else {
                        List<String> message = dbsql.updateVisitorPhoto(uniqueId, savedImageFilepath, savedImageFilename);
                        runOnUiThread(() -> {
                            if (!message.get(0).equals("Id : null")) {
                                Toast.makeText(this, "Visitor photo saved with unique Id " + message.get(0) + " to " + savedImageFilepath, Toast.LENGTH_SHORT).show();
                                Log.d("Idproof", "Visitor photo saved to " + savedImageFilepath + " and database");
                            } else {
                                Toast.makeText(this, "Visitor photo not saved to database", Toast.LENGTH_SHORT).show();
                                Log.d("Idproof", "Visitor photo not saved to database");
                            }
                            navigateToDisplayNDA();
                        });
                    }
                }
            } catch (IOException | SQLException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
            }
        });
    }

    private long getUniqueId() {
        List<VisitorSearchResult> visitorSearchResults = dbsql.getVisitorSearchByMobile(mobileNum, gateId, userId);
        Log.d("gateId",""+gateId);
        Log.d("userId",""+userId);
        Log.d("visitor details list",""+visitorSearchResults);
        //VisitorSearchResult visitor_details = visitorSearchResults.get(0);
        if (!visitorSearchResults.isEmpty())
        {
            Log.d("ans", "inside if");
            VisitorSearchResult visitor_details = visitorSearchResults.get(0);
            Log.d("visitor details", "" + visitor_details);
            return visitor_details.getUniqueId();
        } else {
            Log.d("ans", "inside else");
            long ans=dbsql.getHighestUniqueId();
            Log.d("ans", String.valueOf(ans));
            return dbsql.getHighestUniqueId();
        }
    }

    private void navigateToVisitorEntry() {
        Intent intentBack = new Intent(PhotoCapture.this, VisitorEntry.class);
        intentBack.putExtra("sbuId",String.valueOf(sbuId));
        intentBack.putExtra("ImagePath", savedImageFilepath);
        intentBack.putExtra("ImageName", savedImageFilename);
        intentBack.putExtra("uniqueId",uniqueId);
        startActivity(intentBack);
    }

    private void navigateToDisplayNDA() {
        Intent intentNDA = new Intent(PhotoCapture.this, DisplayNDA.class);
        intentNDA.putExtra("userId",String.valueOf(userId));
        intentNDA.putExtra("sbuId",String.valueOf(sbuId));
        intentNDA.putExtra("gateId",String.valueOf(gateId));
        intentNDA.putExtra("uniqueId",String.valueOf(uniqueId));
        startActivity(intentNDA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
