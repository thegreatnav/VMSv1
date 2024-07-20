package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.dataitems.Company;
import com.example.vmsv1.dataitems.IDProof;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IDProofTypeMaster extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    Button addNewIdProofType_button,save_button;
    private View inputContainer;
    TextView IdProofTypeId_textview,IdProofTypeName_textview,type_textview,fieldName_textview,displayOrder_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_idproof_type_master);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            defaultGateId=intent.getStringExtra("defaultGateId");
            Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }
        db = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.idProofTypeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewIdProofType_button=findViewById(R.id.idproofbutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        IdProofTypeId_textview=findViewById(R.id.editTextProofTypeId);
        IdProofTypeName_textview=findViewById(R.id.editTextProofTypeName);
        type_textview=findViewById(R.id.editTextType);
        fieldName_textview=findViewById(R.id.editTextFieldName);
        displayOrder_textview=findViewById(R.id.editTextDisplayOrder);
        status_textview=findViewById(R.id.editTextStatus);

        // Example data for dynamic table
        List<String> headers = Arrays.asList("ID Proof Type Id", "ID Proof Type Name", "Type","Field Name","Display Order","Status");
        fetchIDProofTypes(headers);

        addNewIdProofType_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputContainer.getVisibility()==View.GONE)
                    inputContainer.setVisibility(View.VISIBLE);
                else
                    inputContainer.setVisibility(View.GONE);
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String IdProofTypeId_value = IdProofTypeId_textview.getText().toString();
                    String IdProofTypeName_value = IdProofTypeName_textview.getText().toString();
                    String type_value=type_textview.getText().toString();
                    String fieldName_value=fieldName_textview.getText().toString();
                    String displayOrder_value=displayOrder_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewIdProofType(IdProofTypeName_value,type_value,fieldName_value,Integer.parseInt(displayOrder_value),status_value,Integer.parseInt(userId));
                    Log.d("Tag1","message="+String.valueOf(message.get(0)));
                    handler.postDelayed(() -> {
                        fetchIDProofTypes(headers);
                        IdProofTypeId_textview.setText("");
                        IdProofTypeName_textview.setText("");
                        type_textview.setText("");
                        fieldName_textview.setText("");
                        displayOrder_textview.setText("");
                        status_textview.setText("");
                        inputContainer.setVisibility(View.GONE);
                    }, 1000);
                }
                catch (Exception e) {
                    Log.e("BlackList", "Error adding data to the list", e);
                }
            }
        });

    }

    public void fetchIDProofTypes(List<String> headers)
    {
        List<IDProof> idProofList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            idProofList= db.getIdProofTypeList("","","");
            Log.d("Idprooflist",String.valueOf(idProofList.get(0)));
            for (IDProof obj : idProofList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("ID Proof Type Id", obj.getIdProofTypeId());
                dataMap.put("ID Proof Type Name", obj.getIdProofTypeName());
                dataMap.put("Type", obj.getIdProofTypeNameDisp());
                dataMap.put("Field Name", obj.getFieldName());
                dataMap.put("Display Order", obj.getDisplayOrder());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching idproof: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}