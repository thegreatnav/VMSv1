package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.IDProofRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Example data for dynamic table
        List<String> headers = Arrays.asList("ID Proof Type Id", "ID Proof Type Name", "Type","Field Name","Display Order","Status");
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