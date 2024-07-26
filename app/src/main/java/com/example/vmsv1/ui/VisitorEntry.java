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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VisitorEntry extends AppCompatActivity {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private DatabaseHelperSQL dbsql;

    private EditText editTextMobileNumber;
    private EditText editTextVisitorName;

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
    private EditText editTextAsset1;
    private EditText editTextAsset2;
    private EditText editTextAsset3;
    private EditText editTextAsset4;
    private EditText editTextAsset5;

    private EditText editTextSecurityPersonnel;
    private EditText editTextSecurityId;

    private TextView textViewInfo;
    private SharedViewModel sharedViewModel;

    private String selectedNumberIdProof;
    private String selectedFileIdProof;
    int ID =0;
    int IDProofNum=0;


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
                if(selectedNumberIdProof!= null)
                {
                    try {
                        List<String>IDProofType = dbsql.getIDProofTypeIdByName(selectedNumberIdProof);
                        Log.d("ID proof type id ",""+ IDProofType.get(0));
                        ID = Integer.parseInt(IDProofType.get(0));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(selectedFileIdProof != null){
                    try {
                        List<String> IDProofType = dbsql.getIDProofTypeIdByName(selectedFileIdProof);
                        Log.d("ID proof file type id ",""+ IDProofType.get(0));
                        ID = Integer.parseInt(IDProofType.get(0));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                    ID =6;

                String mobileNum = editTextMobileNumber.getText().toString();
                long unique_id=getUniqueId(mobileNum);

                IDProofNum = Integer.parseInt(editTextIDProofNumber.getText().toString());
                List<String> update = null;
                try {
                    update = dbsql.updateVisitorIDProofDetails(unique_id,ID, String.valueOf(IDProofNum),null,null);
                    //Toast.makeText(this, "Update saved with unique Id " + String.valueOf(update.get(0)) + " to database", Toast.LENGTH_SHORT).show();
                    Log.d("Update Status","Update saved with unique Id " + String.valueOf(update.get(0)) + " to database");
                } catch (SQLException e) {
                    Log.d("Update Status","Update not saved to database");
                    throw new RuntimeException(e);
                }
                saveData();
                Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
                //String mobileNum = editTextMobileNumber.getText().toString();
                Log.d("Before intent mobileNo",""+mobileNum);
                Log.d("gateid",""+defaultGateId);
                Log.d("userId",""+userId);
                intentPhoto.putExtra("VisitorEntry.MobileNumber",mobileNum);
                intentPhoto.putExtra("VisitorEntry.gateId",defaultGateId);
                intentPhoto.putExtra("VisitorEntry.userId",userId);
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
                    Log.d("visitor",""+visitor.getUniqueId());
                    Log.d("visitor",""+visitor.getMobileNo());
                    Log.d("visitor company1",""+visitor.getVisitorCompany());
                    prefillFields(visitor);
                }
            }
        });

        openCameraButton.setOnClickListener(v -> {
            Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
            String mobileNum = editTextMobileNumber.getText().toString();
            List<String>IDProofType;
            if(!editTextIDProofNumber.getText().toString().equals(""))
            {
                IDProofNum = Integer.parseInt(editTextIDProofNumber.getText().toString());
            }
            else{
                editTextIDProofNumber.setError("Please enter ID Proof Number");
            }
            if(selectedNumberIdProof!= null)
            {
                try {
                    IDProofType = dbsql.getIDProofTypeIdByName(selectedNumberIdProof);
                    Log.d("ID proof type id ",""+ IDProofType.get(0));
                    ID = Integer.parseInt(IDProofType.get(0));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(selectedFileIdProof != null){
                try {
                    IDProofType = dbsql.getIDProofTypeIdByName(selectedFileIdProof);
                    Log.d("ID proof file type id ",""+ IDProofType.get(0));
                    ID = Integer.parseInt(IDProofType.get(0));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                ID =6;

            Log.d("Before intent mobileNo",""+mobileNum);
            Log.d("gateid",""+defaultGateId);
            Log.d("userId",""+userId);
            intentPhoto.putExtra("VisitorEntry.MobileNumber",mobileNum);
            intentPhoto.putExtra("VisitorEntry.gateId",defaultGateId);
            intentPhoto.putExtra("VisitorEntry.userId",userId);
            intentPhoto.putExtra("IDProofType",String.valueOf(ID));
            Log.d("ID",""+ID);
            if(IDProofNum!=0)
            {
                intentPhoto.putExtra("IDProofNum",IDProofNum);
            }
            if(selectedNumberIdProof!=null)
            {
                intentPhoto.putExtra("NumberIDProof",selectedNumberIdProof);
            }
            Log.d("NUMBER ID PROOF",""+selectedNumberIdProof);
            if(selectedFileIdProof!=null)
            {
                intentPhoto.putExtra("FileIDProof",selectedFileIdProof);
            }
            Log.d("FILE ID PROOF",""+selectedFileIdProof);
            startActivity(intentPhoto);
        });


        // Set listener for ID proof spinner
        spinnerIDProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Save the selected item name to the appropriate variable
                boolean b = selectedItem.equals("Passport") || selectedItem.equals("Driving License")
                        || selectedItem.equals("Voter ID") || selectedItem.equals("PAN Card");
                if (selectedItem.equals("Aadhaar No.") || selectedItem.equals("Employee ID")) {
                    selectedNumberIdProof = selectedItem;
                    selectedFileIdProof = null; // Reset file ID proof selection
                    Log.d("SelectedNumberIdProof", "Selected Number ID Proof: " + selectedNumberIdProof);
                } else if (b) {
                    selectedFileIdProof = selectedItem;
                    selectedNumberIdProof = null; // Reset number ID proof selection
                    Log.d("SelectedFileIdProof", "Selected File ID Proof: " + selectedFileIdProof);
                } else {
                    selectedNumberIdProof = null;
                    selectedFileIdProof = null;
                }

                boolean isNumberIDProof = selectedItem.equals("Aadhaar No.") || selectedItem.equals("Employee ID");
                boolean isFileIDProof = b;

                editTextIDProofNumber.setVisibility(isNumberIDProof ? View.VISIBLE : View.GONE);
                textViewInfo.setVisibility(isNumberIDProof ? View.VISIBLE : View.GONE);
                openCameraButton.setVisibility(isFileIDProof ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected if needed
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
                        Log.d("Spinner Data","Fetched successfully");
                    } else {
                        Log.d("Spinner Data","Could not fetch");
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
        editTextPurpose = findViewById(R.id.editTextPurpose);
        buttonSearch = findViewById(R.id.buttonSearch);
        editTextAsset1 = findViewById(R.id.editTextAsset1);
        editTextAsset2 = findViewById(R.id.editTextAsset2);
        editTextAsset3 = findViewById(R.id.editTextAsset3);
        editTextAsset4 = findViewById(R.id.editTextAsset4);
        editTextAsset5 = findViewById(R.id.editTextAsset5);
        editTextSecurityPersonnel = findViewById(R.id.editTextSecurityPersonnel);
        editTextSecurityId = findViewById(R.id.editTextSecurityId);
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
                editTextApproverName, editTextReferenceMail,editTextSecurityPersonnel,editTextSecurityId
        };
        if ( !isNumeric(editTextMobileNumber.getText().toString()) || editTextMobileNumber.getText().toString().length() != 10) {
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
        if(v.getIdProofNo()!=null)
        {
            editTextIDProofNumber.setText(v.getIdProofNo());
        }
        editTextPlace.setText(v.getLocationName());
        Log.d("location",""+editTextPlace.getText());
        editTextCompanyName.setText(v.getCompName()); //null
        Log.d("Comp name",""+editTextCompanyName.getText());
        editTextDesignation.setText(v.getVisitorDesignation());
        Log.d("Designation",""+editTextDesignation.getText());
        editTextPurpose.setText(v.getPurpose()); //null
        Log.d("Purpose",""+editTextPurpose.getText());
        editTextVisitingStaff.setText(v.getVisitingFaculty());
        Log.d("visiting staff",""+editTextVisitingStaff.getText());
        editTextApproverName.setText(v.getApproverName());
        Log.d("approver name",""+editTextApproverName.getText());
        Log.d("ref mail from v: ",""+v.getRefMail());
        editTextReferenceMail.setText(v.getRefMail());
        Log.d("Ref mail",""+editTextReferenceMail.getText());
        editTextAsset1.setText(v.getAsset1());
        Log.d("asset 1",""+editTextAsset1.getText());
        editTextAsset2.setText(v.getAsset2());
        editTextAsset3.setText(v.getAsset3());
        editTextAsset4.setText(v.getAsset4());
        editTextAsset5.setText(v.getAsset5());

        Log.d("security name",""+v.getSecurityName());
        editTextSecurityPersonnel.setText(v.getSecurityName());
        Log.d("security id",""+v.getSecurityId());
        editTextSecurityId.setText(Integer.toString(v.getSecurityId()));

        setSpinnerValue(spinnerIDProof, visitor.getIdProofTypeName());
        setSpinnerValue(spinnerVisitorType, visitor.getVisitorTypeName());
        setSpinnerValue(spinnerVisitingArea, visitor.getVisitingAreaName());

        Toast.makeText(this, "Visitor details loaded", Toast.LENGTH_SHORT).show();
    }
    private long getUniqueId(String mobileNum)
    {
        Log.d("gateId",""+defaultGateId);
        Log.d("userId",""+userId);
        List<VisitorSearchResult> visitorSearchResults = dbsql.getVisitorSearchByMobile(mobileNum,Integer.parseInt(defaultGateId),Integer.parseInt(userId));
        Log.d("visitor details list",""+visitorSearchResults);
        VisitorSearchResult visitor_details = visitorSearchResults.get(0);
        Log.d("visitor details",""+visitor_details);

        return visitor_details.getUniqueId();

    }
}
