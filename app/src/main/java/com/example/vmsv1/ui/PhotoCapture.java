package com.example.vmsv1.ui;

import android.Manifest;
import android.content.Intent;
import android.content.Context;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.example.vmsv1.DisplayNDA;
import com.example.vmsv1.PrintLabel;
import com.example.vmsv1.Visitor;
import com.example.vmsv1.ui.SharedViewModel;
import com.example.vmsv1.R;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.dataitems.VisitorSearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class PhotoCapture extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private String mobileNum;
    private int gateId;
    private int userId;
    private ImageView imageViewPhoto;
    private Button buttonCapture;
    private Button buttonSaveImage;
    private EditText editTextFilename;

    private String savedImageFilename;
    private String savedImageFilepath;

    private int idprooftype;
    private String numidproof;
    private String fileidproof;
    private String idproofname;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    private DatabaseHelperSQL dbsql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Oncreate","Hello");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);

        dbsql = new DatabaseHelperSQL();
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonSaveImage = findViewById(R.id.buttonSaveImage);
        editTextFilename = findViewById(R.id.editTextFileName);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("IDProofType")) {
            mobileNum = intent.getStringExtra("VisitorEntry.MobileNumber");
            Log.d("On create mobileNum",""+mobileNum);
            gateId = Integer.parseInt(intent.getStringExtra("VisitorEntry.gateId"));
            Log.d("gateId",""+gateId);
            userId = Integer.parseInt(intent.getStringExtra("VisitorEntry.userId"));
            Log.d("userId",""+userId);
            idprooftype = Integer.parseInt(intent.getStringExtra("IDProofType"));
            Log.d("idprooftype",""+idprooftype);
            numidproof = intent.getStringExtra("IDProofNum");
            Log.d("numidproof",""+numidproof);
           /* fileidproof = intent.getStringExtra("FileIDProof");
            Log.d("fileidproof",""+fileidproof);
            if (numidproof == null)
            {
                idproofname = fileidproof;
                Log.d("idproofname","null case");
                Log.d("idproofname",idproofname);

            }
            else {
                idproofname = numidproof;
                Log.d("idproofname","non null case");
            }*/

            //Log.d("On create mobileNum",""+mobileNum);
        }
        else
        if (intent != null && !intent.hasExtra("IDProofType")) {
            mobileNum = intent.getStringExtra("VisitorEntry.MobileNumber");
            gateId = Integer.parseInt(intent.getStringExtra("VisitorEntry.gateId"));
            userId = Integer.parseInt(intent.getStringExtra("VisitorEntry.userId"));

            Log.d("On create mobileNum",""+mobileNum);
            Log.d("gateId",""+gateId);
            Log.d("userId",""+userId);

        }


        // Register activity result launchers
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
                        saveButton(imageBitmap);
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
    public void saveButton(Bitmap imageBitMap)
    {
        buttonSaveImage.setOnClickListener(v -> {
            saveImageToFile(imageBitMap);
        });
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
        // Get filename from EditText
        String customFilename = editTextFilename.getText().toString().trim();

        // Validate if filename is provided
        if (TextUtils.isEmpty(customFilename)) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!photoDirectory.exists()) {
            if (!photoDirectory.mkdirs()) {
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File photoFile = new File(photoDirectory, customFilename + ".jpg");

        try (FileOutputStream fos = new FileOutputStream(photoFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            savedImageFilename = photoFile.getName(); // Save the filename
            savedImageFilepath = photoFile.getAbsolutePath();

            // After saving the image, you can perform further actions if needed
            long uniqueId = getUniqueId();
            Log.d("unique id", "" + uniqueId);

            if (idprooftype> 0 ){

                List<String> update = dbsql.updateVisitorIDProofDetails(uniqueId,idprooftype,numidproof,savedImageFilepath,savedImageFilename);
                Toast.makeText(this, "Image saved with unique Id " + String.valueOf(update.get(0)) + " to " + savedImageFilepath, Toast.LENGTH_SHORT).show();

                Intent intentBack = new Intent(PhotoCapture.this, VisitorEntry.class);
                startActivity(intentBack);
            } else
            {
                List<String> message = dbsql.updateVisitorPhoto(uniqueId, savedImageFilepath, savedImageFilename);
                Toast.makeText(this, "Image saved with unique Id " + String.valueOf(message.get(0)) + " to " + savedImageFilepath, Toast.LENGTH_SHORT).show();

                //check if nda status is 'Y'
                Intent intentNDA = new Intent(PhotoCapture.this,DisplayNDA.class);
                startActivity(intentNDA);
            }


        } catch (IOException e) {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private long getUniqueId()
    {
        Log.d("mobileNum",""+mobileNum);
        Log.d("gateId",""+gateId);
        Log.d("userId",""+userId);
        List<VisitorSearchResult> visitorSearchResults = dbsql.getVisitorSearchByMobile(mobileNum,gateId,userId);
        Log.d("visitor details list",""+visitorSearchResults);
        VisitorSearchResult visitor_details = visitorSearchResults.get(0);
        Log.d("visitor details",""+visitor_details);

        return visitor_details.getUniqueId();

    }
}
