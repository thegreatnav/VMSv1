package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.vmsv1.dataitems.BlackListVisitor;
import com.example.vmsv1.dataitems.Company;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    Button addNewCompany_button,save_button;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    private View inputContainer;
    TextView companyId_textview,companyName_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_master);
        handler = new Handler(Looper.getMainLooper());

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

        recyclerView = findViewById(R.id.CMrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewCompany_button=findViewById(R.id.cmbutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        companyId_textview=findViewById(R.id.editTextCompanyId);
        companyName_textview=findViewById(R.id.editTextCompanyName);
        status_textview=findViewById(R.id.editTextStatus);

        List<String> headers = Arrays.asList("Company ID", "Company Name", "Status");
        fetchCompanies(headers);

        addNewCompany_button.setOnClickListener(new View.OnClickListener() {
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
                    String companyId_value = companyId_textview.getText().toString();
                    String companyName_value = companyName_textview.getText().toString();
                    String status_value = status_textview.getText().toString();

                    List<String> message=db.addNewCompany(companyId_value,companyName_value,status_value,Integer.parseInt(userId));
                    handler.postDelayed(() -> {
                        fetchCompanies(headers);
                        companyId_textview.setText("");
                        companyName_textview.setText("");
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

    private void fetchCompanies(List<String> headers) {
        List<Company> companyList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            companyList= db.getCompanyList("","");
            Log.d("Companylist",String.valueOf(companyList.get(0)));
            for (Company company : companyList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("Company ID", company.getCompId());
                dataMap.put("Company Name", company.getCompName());
                dataMap.put("Status", company.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, false, null,db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching companylist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}
