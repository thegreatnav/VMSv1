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
import com.example.vmsv1.dataitems.VisitorType;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitorTypeMaster extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    Button addNewVisitorType_button,save_button;
    private View inputContainer;
    TextView visitorTypeName_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visitor_type_master);

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

        recyclerView = findViewById(R.id.VisitorTypeRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewVisitorType_button=findViewById(R.id.visitortypebutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        visitorTypeName_textview=findViewById(R.id.editTextVisitorTypeName);
        status_textview=findViewById(R.id.editTextStatus);

        List<String> headers = Arrays.asList("Id", "Visitor Type Name", "Status");
        fetchVisitorTypes(headers);

        addNewVisitorType_button.setOnClickListener(new View.OnClickListener() {
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
                    String visitorTypeName_value = visitorTypeName_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewVisitorType(visitorTypeName_value,status_value,Integer.parseInt(userId));
                    Log.d("Tag1","message="+String.valueOf(message.get(0)));
                    handler.postDelayed(() -> {
                        fetchVisitorTypes(headers);
                        visitorTypeName_textview.setText("");
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

    public void fetchVisitorTypes(List<String> headers)
    {
        List<VisitorType> visitorTypeList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            visitorTypeList= db.getVisitorTypeList("","");
            Log.d("visitorTypeList",String.valueOf(visitorTypeList.get(0)));
            for (VisitorType obj : visitorTypeList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("Id", obj.getVisitorTypeId());
                dataMap.put("Visitor Type Name", obj.getVisitorTypeName());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching visitorTypeList: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}