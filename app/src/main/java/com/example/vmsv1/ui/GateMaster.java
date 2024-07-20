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
    Button addNewGate_button,save_button;
    private View inputContainer;
    TextView gateId_textview,gateName_textview,company_textview,sbuid_textview,status_textview;
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
            //Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db=new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        recyclerView = findViewById(R.id.gateRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewGate_button=findViewById(R.id.gatebutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        gateId_textview=findViewById(R.id.editTextGateId);
        gateName_textview=findViewById(R.id.editTextGateName);
        company_textview=findViewById(R.id.editTextCompanyName);
        sbuid_textview=findViewById(R.id.editTextSBUId);
        status_textview=findViewById(R.id.editTextStatus);

        List<String> headers = Arrays.asList("Gate ID", "Gate Name", "Company","SBU","Status");
        fetchGates(headers);

        addNewGate_button.setOnClickListener(new View.OnClickListener() {
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
                    String gateId_value = gateId_textview.getText().toString();
                    String gateName_value = gateName_textview.getText().toString();
                    String companyName_value=company_textview.getText().toString();
                    String sbuId_value=sbuid_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewGate(Integer.parseInt(sbuId_value),gateName_value,status_value,Integer.parseInt(userId));
                    Log.d("Tag1","message="+String.valueOf(message.get(0)));
                    handler.postDelayed(() -> {
                        fetchGates(headers);
                        gateId_textview.setText("");
                        gateName_textview.setText("");
                        company_textview.setText("");
                        sbuid_textview.setText("");
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

    public void fetchGates(List<String> headers)
    {
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
    }
}