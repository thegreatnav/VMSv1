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
import com.example.vmsv1.dataitems.SBU;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SBUMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    Button addNewSbu_button,save_button;
    private View inputContainer;
    TextView SbuId_textview,SbuName_textview,company_textview,location_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sbumaster);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            defaultGateId=intent.getStringExtra("defaultGateId");
            //Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }
        db = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.sbuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewSbu_button=findViewById(R.id.sbumasterbutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        SbuId_textview=findViewById(R.id.editTextSBUId);
        SbuName_textview=findViewById(R.id.editTextSbuName);
        company_textview=findViewById(R.id.editTextCompanyId);
        location_textview=findViewById(R.id.editTextLocationId);
        status_textview=findViewById(R.id.editTextStatus);

        // Example data for dynamic table
        List<String> headers = Arrays.asList("SBU Id", "SBU Name","Company","Location","Status");
        fetchSBU(headers);

        addNewSbu_button.setOnClickListener(new View.OnClickListener() {
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
                    String SbuId_value = SbuId_textview.getText().toString();
                    String SbuName_value = SbuName_textview.getText().toString();
                    String companyId_value=company_textview.getText().toString();
                    String locationId_value=location_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewSBU(companyId_value,SbuName_value, Integer.parseInt(locationId_value),status_value,Integer.parseInt(userId));
                    Log.d("Tag1","message="+String.valueOf(message.get(0)));
                    handler.postDelayed(() -> {
                        fetchSBU(headers);
                        SbuId_textview.setText("");
                        SbuName_textview.setText("");
                        company_textview.setText("");
                        location_textview.setText("");
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

    public void fetchSBU(List<String> headers)
    {
        List<SBU> sbuList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            sbuList= db.getSBUList("","",0,"");
            Log.d("SBUList",String.valueOf(sbuList.get(0)));
            for (SBU obj : sbuList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("SBU Id", obj.getSbuId());
                dataMap.put("SBU Name", obj.getSbuName());
                dataMap.put("Company", obj.getCompName());
                dataMap.put("Location", obj.getLocationName());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching sbulist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}
