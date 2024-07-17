package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.vmsv1.R;
import com.example.vmsv1.dataitems.IDProof;
import com.example.vmsv1.dataitems.VisitingArea;
import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.dataitems.VisitorType;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VisitorEntry extends AppCompatActivity {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private DatabaseHelperSQL dbsql;

    private EditText editTextMobileNumber;
    private EditText editTextVisitorName;
    private EditText editTextSecurityPersonnel;
    private EditText editTextSecurityId;
    private EditText editTextPlace;
    private EditText editTextDesignation;
    private EditText editTextCompanyName;
    private EditText editTextVisitingStaff;
    private EditText editTextApproverName;
    private EditText editTextReferenceMail;
    private EditText editTextIDProofNumber;
    private EditText editTextPurpose;
    private Button buttonSave;
    private Spinner spinnerIDProof;
    private Spinner spinnerVisitorType;
    private Spinner spinnerVisitingArea;
    private Button openCameraButton;
    private Button buttonSearch;

    protected String userId;
    protected String defaultGateId;
    protected String sbuId;

    private VisitorSearchResult visitor;
    private EditText editTextRefMail;
    private EditText editTextAsset1;
    private EditText editTextAsset2;
    private EditText editTextAsset3;
    private EditText editTextAsset4;
    private EditText editTextAsset5;

    private TextView textViewInfo;
    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_enrty);

        dbsql = new DatabaseHelperSQL();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
            defaultGateId = intent.getStringExtra("defaultGateId");
            sbuId = intent.getStringExtra("sbuId");
            Log.d("visitor entry","userid="+userId);
            Log.d("visitor entry","sbuid="+sbuId);
            Log.d("visitor entry","defaultid="+defaultGateId);
            sharedViewModel.setUserId(userId);
            sharedViewModel.setSbuId(sbuId);
            sharedViewModel.setDefaultGateId(defaultGateId);
        }

        // Initialize UI elements
        initializeUI();

        Future<List<IDProof>> futureID = executorService.submit(() -> dbsql.getIdProofTypeList("", "", "Active"));
        retrieveDropDownList(futureID, spinnerIDProof);

        Future<List<VisitorType>> futureVisitorType = executorService.submit(() -> dbsql.getVisitorTypeList("", "Active"));
        retrieveDropDownList(futureVisitorType, spinnerVisitorType);

        Future<List<VisitingArea>> futureVisitingAreas = executorService.submit(() -> dbsql.getVisitingAreaList("", Integer.parseInt(sbuId), "", "Active"));
        retrieveDropDownList(futureVisitingAreas, spinnerVisitingArea);


        // Set listeners for buttons
        buttonSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveData();
                Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
                startActivity(intentPhoto);
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = editTextMobileNumber.getText().toString();
                String gateId = defaultGateId;

                Log.d("mobile num visitor entry ",""+mobileNo);
                Log.d("gate id visitor entry",""+gateId); //is null
                Log.d("User id visitor entry",""+userId);

                if (mobileNo.isEmpty() || !isNumeric(mobileNo) || mobileNo.length() != 10) {
                    Toast.makeText(VisitorEntry.this, "Enter valid number", Toast.LENGTH_SHORT).show();
                    return;
                }



                List<VisitorSearchResult> visitorDetails = dbsql.getVisitorSearchByMobile(mobileNo, Integer.parseInt(gateId), Integer.parseInt(userId));
                // Process the visitor details as needed
                if (visitorDetails.isEmpty()) {
                    Toast.makeText(VisitorEntry.this, "No visitor found", Toast.LENGTH_SHORT).show();
                } else {
                    // Do something with visitorDetails
                    // For example, display the details
                    visitor = visitorDetails.get(0);
                    prefillFields(visitor);
                }
            }
        });

        openCameraButton.setOnClickListener(v -> {
            Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
            String mobileNum = editTextMobileNumber.getText().toString();
            intentPhoto.putExtra("VisitorEntry.MobileNumber",mobileNum);
            intentPhoto.putExtra("VisitorEntry.gateId",defaultGateId);
            intentPhoto.putExtra("VisitorEntry.userId",userId);
            startActivity(intentPhoto);
        });

        // Set listener for ID proof spinner
        spinnerIDProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                boolean isNumberIDProof = selectedItem.equals("Aadhaar No.") || selectedItem.equals("Employee ID");
                boolean isFileIDProof = selectedItem.equals("Passport") || selectedItem.equals("Driving License")
                        || selectedItem.equals("Voter ID") || selectedItem.equals("PAN Card");
                editTextIDProofNumber.setVisibility(isNumberIDProof ? View.VISIBLE : View.GONE);
                textViewInfo.setVisibility(isNumberIDProof ? View.VISIBLE : View.GONE);
                openCameraButton.setVisibility(isFileIDProof ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("VisitorEntry", "Nothing selected");
            }
        });

    }

    private <T> void retrieveDropDownList(@NonNull Future<List<T>> future, Spinner spinner) {
        executorService.submit(() -> {
            try {
                List<T> items = future.get(); // This blocks until the result is available

                runOnUiThread(() -> {
                    // Update UI with the fetched data
                    if (items != null && !items.isEmpty()) {
                        populateSpinner(spinner, (ArrayList<T>) items);
                        Toast.makeText(VisitorEntry.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VisitorEntry.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions if needed
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the ExecutorService when no longer needed
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // Method to initialize UI elements
    private void initializeUI() {
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);
        editTextVisitorName = findViewById(R.id.editTextVisitorName);
        editTextPlace = findViewById(R.id.editTextPlace);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextDesignation = findViewById(R.id.editTextDesignation);
        editTextVisitingStaff = findViewById(R.id.editTextVisitingStaff);
        editTextApproverName = findViewById(R.id.editTextApproverName);
        editTextReferenceMail = findViewById(R.id.editTextReferenceMail);
        editTextSecurityPersonnel = findViewById(R.id.editTextSecurityPersonnel);
        editTextSecurityId = findViewById(R.id.editTextSecurityId);
        textViewInfo = findViewById(R.id.textViewIDProofNumber);
        editTextIDProofNumber = findViewById(R.id.editTextIdProofNumber);
        buttonSave = findViewById(R.id.buttonSave);
        openCameraButton = findViewById(R.id.openCameraButton);
        spinnerIDProof = findViewById(R.id.spinnerIdProof);
        spinnerVisitingArea = findViewById(R.id.spinnerVisitingArea);
        spinnerVisitorType = findViewById(R.id.spinnerVisitorType);
        //editTextPurpose = findViewById(R.id.editTextPurpose);
        buttonSearch = findViewById(R.id.buttonSearch);
    }

    protected static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // Method to validate inputs
    private boolean validateInputs() {
        EditText[] fields = {
                editTextVisitorName, editTextPlace,
                editTextCompanyName, editTextDesignation, editTextVisitingStaff,
                editTextApproverName, editTextReferenceMail
        };
        if ( !isNumeric(editTextMobileNumber.getText().toString()) || editTextMobileNumber.getText().toString()  .length() != 10) {
            editTextMobileNumber.setError("Please enter a valid mobile number"+editTextMobileNumber.getText());
            return false;
        }

        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                field.setError(field.getHint() + " cannot be blank");
                return false;
            }
        }
        return true;
    }

    private <T> void populateSpinner(Spinner spinner, ArrayList<T> dropDown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());

        for (T item : dropDown) {
            if (item instanceof IDProof) {
                adapter.add(((IDProof) item).getIdProofTypeName());
            } else if (item instanceof VisitorType) {
                adapter.add(((VisitorType) item).getVisitorTypeName());
            } else if (item instanceof VisitingArea) {
                Log.d("Visiting area dropdown before","");
                adapter.add(((VisitingArea) item).getVisitingAreaName());
                Log.d("Visiting area dropdown after",""+adapter);
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runOnUiThread(() -> {
            spinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }


    // Method to save data (dummy implementation)
    private void saveData() {
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }
    private void prefillFields(VisitorSearchResult v)
    {
        editTextVisitorName.setText(v.getVisitorName());
        Log.d("visitor name",""+editTextVisitorName.getText().toString());
        editTextPlace.setText(v.getLocationName());
        Log.d("location",""+editTextPlace.getText());
        editTextCompanyName.setText(v.getCompName()); //null
        //Log.d("Comp name",""+editTextCompanyName.getText());
        editTextDesignation.setText(v.getVisitorDesignation());
        Log.d("Designation",""+editTextDesignation.getText());
        editTextPurpose.setText(v.getPurpose()); //null
        Log.d("Purpose",""+editTextCompanyName.getText());
        editTextVisitingStaff.setText(v.getVisitingFaculty());
        Log.d("visiting staff",""+editTextVisitingStaff.getText());
        editTextApproverName.setText(v.getApproverName());
        Log.d("approver name",""+editTextApproverName.getText());
        editTextRefMail.setText(v.getRefMail());
        Log.d("Ref mail",""+editTextRefMail.getText());
        editTextAsset1.setText(v.getAsset1());
        Log.d("asset 1",""+editTextAsset1.getText());
        editTextAsset2.setText(v.getAsset2());
        editTextAsset3.setText(v.getAsset3());
        editTextAsset4.setText(v.getAsset4());
        editTextAsset5.setText(v.getAsset5());

        setSpinnerValue(spinnerIDProof, visitor.getIdProofTypeName());
        setSpinnerValue(spinnerVisitorType, visitor.getVisitorTypeName());
        setSpinnerValue(spinnerVisitingArea, visitor.getVisitingAreaName());

        Toast.makeText(this, "Visitor details loaded", Toast.LENGTH_SHORT).show();

    }
}
