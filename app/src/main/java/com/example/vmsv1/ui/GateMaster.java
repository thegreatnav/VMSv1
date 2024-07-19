package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.vmsv1.ItemDomain;
import com.example.vmsv1.R;

import com.example.vmsv1.dataitems.Company;
import com.example.vmsv1.dataitems.Gate;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GateMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gate_master);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            defaultGateId=intent.getStringExtra("defaultGateId");
            Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        List<String> headers = Arrays.asList("Gate ID", "Gate Name", "Company","SBU","Status");

        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Gate> gateList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            gateList= db.getGateList("", Integer.parseInt(sbuId),"","");
            Log.d("Gatelist",String.valueOf(gateList.get(0)));
            for (Gate obj : gateList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("Gate ID", obj.getGateId());
                dataMap.put("Gate Name", obj.getGateName());
                dataMap.put("Company", obj.getCompName());
                dataMap.put("SBU", obj.getSbuName());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching gatelist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }

        /*FirebaseFunctionCalls.getGateMaster(db, new DataRetrievedCallback() {
            @Override
            public void onGridViewDataRetrieved(ArrayList<Item> itemList) {
            }

            @Override
            public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {
                runOnUiThread(() -> {
                    tableAdapter = new TableAdapter(dataList, headers, false, "",db);
                    recyclerView.setAdapter(tableAdapter);
                });
            }
            @Override
            public void onLoginCallback(boolean isSuccess,String message,String userId,String defaultGateId,String sbuId)
            {

            }

            @Override
            public void onCompanyNameCallback(String companyName) {

            }
            public void onBlacklistDeletedCallback(boolean isSuccess)
            {

            }

            @Override
            public void onVisitorDetailsRetrieved(FirebaseFunctionCalls.Visitor visitorTypeName) {

            }
        });*/
    }
}