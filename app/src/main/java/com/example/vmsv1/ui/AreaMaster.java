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
import com.example.vmsv1.dataitems.VisitingArea;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button addNewArea_button,save_button;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    private View inputContainer;
    TextView vaId_textview,vaName_textview,companyName_textview,sbuId_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_area_master);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            defaultGateId=intent.getStringExtra("defaultGateId");
        }
        db = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.AreaRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewArea_button=findViewById(R.id.vabutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        vaId_textview=findViewById(R.id.editTextAreaId);
        vaName_textview=findViewById(R.id.editTextAreaName);
        companyName_textview=findViewById(R.id.editTextCompanyName);
        sbuId_textview=findViewById(R.id.editTextSBUName);
        status_textview=findViewById(R.id.editTextStatus);

        List<String> headers = Arrays.asList("Visiting Area Id", "Visiting Area Name", "Company","SBU","Status");
        fetchVisitingAreas(headers);

        addNewArea_button.setOnClickListener(new View.OnClickListener() {
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
                    String vaId_value = vaId_textview.getText().toString();
                    String vaName_value = vaName_textview.getText().toString();
                    String companyName_value=companyName_textview.getText().toString();
                    String sbuId_value=sbuId_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewVisitingArea(Integer.parseInt(sbuId_value),vaName_value,status_value,Integer.parseInt(userId));
                    Log.d("Tag1","message="+String.valueOf(message.get(0)));
                    handler.postDelayed(() -> {
                        fetchVisitingAreas(headers);
                        vaId_textview.setText("");
                        vaName_textview.setText("");
                        companyName_textview.setText("");
                        sbuId_textview.setText("");
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

    public void fetchVisitingAreas(List<String> headers)
    {
        List<VisitingArea> areaList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            areaList= db.getVisitingAreaList("", Integer.parseInt("0"),"","");
            Log.d("Arealist",String.valueOf(areaList.get(0)));
            for (VisitingArea obj : areaList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("Visiting Area Id", obj.getAreaId());
                dataMap.put("Visiting Area Name", obj.getVisitingAreaName());
                dataMap.put("Company", obj.getCompName());
                dataMap.put("SBU", obj.getSbuName());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching arealist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}