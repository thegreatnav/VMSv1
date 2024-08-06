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

import java.sql.SQLException;
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

    private String userId;
    private String defaultGateId;
    private String sbuId,mo_no;

    private VisitorSearchResult visitor;
    private EditText editTextAsset1;
    private EditText editTextAsset2;
    private EditText editTextAsset3;
    private EditText editTextAsset4;
    private EditText editTextAsset5;

    private EditText editTextSecurityPersonnel;
    private EditText editTextSecurityId;

    private TextView textViewInfo,sbu;
    private SharedViewModel sharedViewModel;

    private String selectedNumberIdProof;
    private String selectedFileIdProof,sbuName;

    private long unique_id;

    private Spinner gate_spinner;
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
            if(intent.hasExtra("MobileNum")) {
                mo_no = intent.getStringExtra("MobileNum");
                Log.d("visitor entry", "mo_no=" + mo_no);
            }
            if(intent.hasExtra("uniqueId")) {
                unique_id = Long.parseLong(intent.getStringExtra("uniqueId"));
                Log.d("visitor entry", "uniqueid=" + unique_id);
            }
            Log.d("visitor entry","userid="+userId);
            Log.d("visitor entry","sbuid="+sbuId);
            Log.d("visitor entry","defaultid="+defaultGateId);
            sharedViewModel.setUserId(userId);
            sharedViewModel.setSbuId(sbuId);
            sharedViewModel.setDefaultGateId(defaultGateId);
        }

        // Initialize UI elements
        initializeUI();

        executorService.execute(() -> {
            List<List<String>> gatespinnerArray = dbsql.getGateListSpinner("MTL", sbuId);

            ArrayList<String> gatespinnerarray2 = new ArrayList<>();
            for (List<String> gate : gatespinnerArray) {
                gatespinnerarray2.add(gate.get(2));
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> gateadapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, gatespinnerarray2);
                gateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gate_spinner.setAdapter(gateadapter);

                sbuName = gatespinnerArray.get(0).get(0);
                sbu.setText(sbuName);
            });
        });

        Future<List<IDProof>> futureID = executorService.submit(() -> dbsql.getIdProofTypeList("", "", "Active"));
        retrieveDropDownList(futureID, spinnerIDProof);

        Future<List<VisitorType>> futureVisitorType = executorService.submit(() -> dbsql.getVisitorTypeList("", "Active"));
        retrieveDropDownList(futureVisitorType, spinnerVisitorType);

        Future<List<VisitingArea>> futureVisitingAreas = executorService.submit(() -> dbsql.getVisitingAreaList("", Integer.parseInt(sbuId), "", "Active"));
        retrieveDropDownList(futureVisitingAreas, spinnerVisitingArea);

        if (intent.hasExtra("uniqueId") && intent.hasExtra("MobileNum")) {
            Log.d("vis en","inside if");
            String mo_no = intent.getStringExtra("MobileNum");
            executorService.execute(() -> {
                Log.d("visitor entry1", "mo_no=" + mo_no);
                Log.d("visitor entry1", "defaultgateid=" + defaultGateId);
                Log.d("visitor entry1", "userId=" + userId);

                List<VisitorSearchResult> visitorDetails = dbsql.getVisitorSearchByMobile(mo_no, Integer.parseInt(defaultGateId), Integer.parseInt(userId));
                Log.d("nnn",visitorDetails.get(0).getMobileNo());
                if (!visitorDetails.isEmpty()) {
                    Log.d("vis en","inside 2nd if");
                    VisitorSearchResult visitor = visitorDetails.get(0);
                    runOnUiThread(() -> prefillFields(visitor));
                } else {
                    Log.d("vis en","inside 2nd if");
                    runOnUiThread(() -> Toast.makeText(VisitorEntry.this, "No visitor details found", Toast.LENGTH_SHORT).show());
                }
            });
        }



        // Set listeners for buttons
        buttonSave.setOnClickListener(v -> {
            if (validateInputs()) {
                executorService.execute(() -> {
                    if (selectedNumberIdProof != null) {
                        try {
                            List<String> IDProofType = dbsql.getIDProofTypeIdByName(selectedNumberIdProof);
                            Log.d("ID proof type id ", "" + IDProofType.get(0));
                            ID = Integer.parseInt(IDProofType.get(0));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (selectedFileIdProof != null) {
                        try {
                            List<String> IDProofType = dbsql.getIDProofTypeIdByName(selectedFileIdProof);
                            Log.d("ID proof file type id ", "" + IDProofType.get(0));
                            ID = Integer.parseInt(IDProofType.get(0));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        ID = 6;
                    }

                    String mobileNum = editTextMobileNumber.getText().toString();
                    unique_id = getUniqueId(mobileNum);

                    if(!(editTextIDProofNumber.getText().toString().isEmpty()))
                        IDProofNum = Integer.parseInt(editTextIDProofNumber.getText().toString());
                    else
                        IDProofNum=0;
                    List<String> update = null;
                    try {
                        if(unique_id!=-2) {
                            update = dbsql.updateVisitorIDProofDetails(unique_id, ID, String.valueOf(IDProofNum), null, null);
                            Log.d("Update Status", "Update saved with unique Id " + update.get(0) + " to database");
                        }
                        else
                        {
                            List<String> tempVisitor;
                            tempVisitor=dbsql.addNewVisitorEntry("MTL",Integer.parseInt(sbuId),1,Integer.parseInt(defaultGateId),editTextMobileNumber.getText().toString(),editTextVisitorName.getText().toString(),editTextPlace.getText().toString(),editTextDesignation.getText().toString(),editTextCompanyName.getText().toString(),spinnerVisitorType.getSelectedItemPosition(),editTextPurpose.getText().toString(),editTextVisitingStaff.getText().toString(),editTextApproverName.getText().toString(),spinnerVisitingArea.getSelectedItemPosition(),editTextReferenceMail.getText().toString(),editTextAsset1.getText().toString(),editTextAsset2.getText().toString(),editTextAsset3.getText().toString(),editTextAsset4.getText().toString(),editTextAsset5.getText().toString(),editTextSecurityPersonnel.getText().toString(),editTextSecurityId.getText().toString(),"","",ID,"","","","","",Integer.parseInt(userId));
                            if(tempVisitor.get(0).equals("Id : null"))
                            {
                                Log.d("V","Visitor not added");
                            }
                            else
                            {
                                Log.d("V","Visitor added with "+tempVisitor.get(0));
                                String temp=tempVisitor.get(0);
                                Log.d("V","UniqueId generated = "+temp);
                                unique_id=Integer.parseInt(temp);
                            }
                        }
                    } catch (SQLException e) {
                        Log.d("Update Status", "Update not saved to database");
                        throw new RuntimeException(e);
                    }

                    long finalUnique_id = unique_id;
                    runOnUiThread(() -> {
                        saveData();
                        Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
                        Log.d("Before intent mobileNo", mobileNum);
                        Log.d("gateid", defaultGateId);
                        Log.d("userId", userId);
                        Log.d("ID", String.valueOf(ID));
                        intentPhoto.putExtra("VisitorEntry.MobileNumber", mobileNum);
                        intentPhoto.putExtra("VisitorEntry.gateId", defaultGateId);
                        intentPhoto.putExtra("VisitorEntry.userId", userId);
                        intentPhoto.putExtra("VisitorEntry.sbuId", sbuId);
                        intentPhoto.putExtra("VisitorEntry.ID", String.valueOf(ID));
                        intentPhoto.putExtra("unique_Id", String.valueOf(finalUnique_id));
                        if(IDProofNum!=0)
                            intentPhoto.putExtra("IDProofNum",String.valueOf(IDProofNum));
                        startActivity(intentPhoto);
                    });
                });
            }
        });


        buttonSearch.setOnClickListener(v -> {
            String mobileNo = editTextMobileNumber.getText().toString();
            String gateId = defaultGateId;

            Log.d("mobile num visitor entry ", mobileNo);
            Log.d("gate id visitor entry", gateId);
            Log.d("User id visitor entry", userId);

            if (mobileNo.isEmpty() || !isNumeric(mobileNo) || mobileNo.length() != 10) {
                Toast.makeText(VisitorEntry.this, "Enter valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                List<VisitorSearchResult> visitorDetails = dbsql.getVisitorSearchByMobile(mobileNo, Integer.parseInt(gateId), Integer.parseInt(userId));

                runOnUiThread(() -> {
                    if (visitorDetails.isEmpty()) {
                        Toast.makeText(VisitorEntry.this, "No visitor found", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        visitor = visitorDetails.get(0);
                        prefillFields(visitor);
                    }
                });
            });
        });

        openCameraButton.setOnClickListener(v -> {
            Intent intentPhoto = new Intent(VisitorEntry.this, PhotoCapture.class);
            String mobileNum = editTextMobileNumber.getText().toString();
            List<String>IDProofType;
            if(!editTextIDProofNumber.getText().toString().equals(""))
            {
                IDProofNum = Integer.parseInt(editTextIDProofNumber.getText().toString());
            }
            else
            {
                editTextIDProofNumber.setError("Please enter ID Proof Number");
            }
            if(selectedNumberIdProof!= null)
            {
                try {
                    IDProofType = dbsql.getIDProofTypeIdByName(selectedNumberIdProof);
                    Log.d("ID proof type id ",""+ IDProofType.get(0));
                    ID = Integer.parseInt(IDProofType.get(0));
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else if(selectedFileIdProof != null)
            {
                try
                {
                    IDProofType = dbsql.getIDProofTypeIdByName(selectedFileIdProof);
                    Log.d("ID proof file type id ",""+ IDProofType.get(0));
                    ID = Integer.parseInt(IDProofType.get(0));
                }
                catch (SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
                ID =6;

            Log.d("Before intent mobileNo",""+mobileNum);
            Log.d("gateid",""+defaultGateId);
            Log.d("userId",""+userId);
            Log.d("ID",""+ID);

            unique_id = getUniqueId(mobileNum);

            if(!(editTextIDProofNumber.getText().toString().isEmpty()))
                IDProofNum = Integer.parseInt(editTextIDProofNumber.getText().toString());
            else
                IDProofNum=0;
            List<String> update = null;
            try {
                if(unique_id!=-2) {
                    update = dbsql.updateVisitorIDProofDetails(unique_id, ID, String.valueOf(IDProofNum), null, null);
                    Log.d("Update Status", "Update saved with unique Id " + update.get(0) + " to database");
                }
                else
                {
                    List<String> tempVisitor;
                    tempVisitor=dbsql.addNewVisitorEntry("MTL",Integer.parseInt(sbuId),1,Integer.parseInt(defaultGateId),editTextMobileNumber.getText().toString(),editTextVisitorName.getText().toString(),editTextPlace.getText().toString(),editTextDesignation.getText().toString(),editTextCompanyName.getText().toString(),spinnerVisitorType.getSelectedItemPosition(),editTextPurpose.getText().toString(),editTextVisitingStaff.getText().toString(),editTextApproverName.getText().toString(),spinnerVisitingArea.getSelectedItemPosition(),editTextReferenceMail.getText().toString(),editTextAsset1.getText().toString(),editTextAsset2.getText().toString(),editTextAsset3.getText().toString(),editTextAsset4.getText().toString(),editTextAsset5.getText().toString(),editTextSecurityPersonnel.getText().toString(),editTextSecurityId.getText().toString(),"","",ID,"","","","","",Integer.parseInt(userId));
                    if(tempVisitor.get(0).equals("Id : null"))
                    {
                        Log.d("V","Visitor not added");
                    }
                    else
                    {
                        Log.d("V","Visitor added with "+tempVisitor.get(0));
                        String temp=tempVisitor.get(0);
                        Log.d("V","UniqueId generated = "+temp);
                        unique_id=Integer.parseInt(temp);
                    }
                }
            } catch (SQLException e) {
                Log.d("Update Status", "Update not saved to database");
                throw new RuntimeException(e);
            }

            long finalUnique_id = unique_id;
            intentPhoto.putExtra("VisitorEntry.MobileNumber",mobileNum);
            intentPhoto.putExtra("VisitorEntry.gateId",defaultGateId);
            intentPhoto.putExtra("VisitorEntry.userId",userId);
            intentPhoto.putExtra("VisitorEntry.sbuId",sbuId);
            intentPhoto.putExtra("VisitorEntry.ID",String.valueOf(ID));
            intentPhoto.putExtra("unique_Id", String.valueOf(finalUnique_id));
            if(IDProofNum!=0)
                intentPhoto.putExtra("IDProofNum",String.valueOf(IDProofNum));
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
                // Handle case when nothing is selected if neede
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
                        Log.d("Spinner Data", "Fetched successfully");
                    } else {
                        Log.d("Spinner Data", "Could not fetch");
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
        gate_spinner = findViewById(R.id.gate_spinner);
        sbu = findViewById(R.id.unit_name);

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
            editTextIDProofNumber.setText(v.getIdProofNo());

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

        private void clearFields() {
        editTextVisitorName.setText("");
        editTextIDProofNumber.setText("");
        editTextPlace.setText("");
        editTextCompanyName.setText("");
        editTextDesignation.setText("");
        editTextPurpose.setText("");
        editTextVisitingStaff.setText("");
        editTextApproverName.setText("");
        editTextReferenceMail.setText("");
        editTextAsset1.setText("");
        editTextAsset2.setText("");
        editTextAsset3.setText("");
        editTextAsset4.setText("");
        editTextAsset5.setText("");
        editTextSecurityPersonnel.setText("");
        editTextSecurityId.setText("");

        // Assuming you want to reset the spinners to their default positions
        spinnerIDProof.setSelection(0);
        spinnerVisitorType.setSelection(0);
        spinnerVisitingArea.setSelection(0);

        Toast.makeText(this, "Fields cleared", Toast.LENGTH_SHORT).show();
    }

    private long getUniqueId(String mobileNum)
    {
        Future<Integer> future = executorService.submit(() -> {
            List<VisitorSearchResult> visitorSearchResults = dbsql.getVisitorSearchByMobile(mobileNum, Integer.parseInt(defaultGateId), Integer.parseInt(userId));
            if(!visitorSearchResults.isEmpty()) {
                Log.d("Future visitorsearchresult","Id found "+visitorSearchResults.get(0).getUniqueId());
                VisitorSearchResult visitor_details = visitorSearchResults.get(0);
                return visitor_details.getUniqueId();
            }
            else
            {
                Log.d("Future visitorsearchresult","Id not found");
                return -2;
            }
        });

        try {
            return future.get();
        } catch (Exception e) {
            Log.e("VisitorEntry", "Error getting unique ID", e);
            return -1;
        }

    }
}
