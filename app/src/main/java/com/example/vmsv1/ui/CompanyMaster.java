package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.vmsv1.GridAdapter;
import com.example.vmsv1.ItemDomain;
import com.example.vmsv1.R;

import com.example.vmsv1.ui.TableAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    FirebaseFirestore db;
    String userId, defaultGateId, sbuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_master);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.CMrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // Example data for dynamic table
        List<String> headers = Arrays.asList("Company ID", "Company Name", "Status");

     /*   FirebaseFunctionCalls.getCompanyMaster(db, new DataRetrievedCallback() {
            @Override
            public void onGridViewDataRetrieved(ArrayList<ItemDomain> itemList) {
            }

            @Override
            public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {
                runOnUiThread(() -> {
                    tableAdapter = new TableAdapter(dataList, headers, false, "",db);
                    recyclerView.setAdapter(tableAdapter);
                });
            }

            @Override
            public void onCompanyNameCallback(String companyName) {

            }
            @Override
            public void onLoginCallback(boolean isSuccess,String message,String userId,String defaultGateId,String sbuId)
            {

            }
            public void onBlacklistDeletedCallback(boolean isSuccess)
            {

            }

            @Override
            public void onVisitorDetailsRetrieved(FirebaseFunctionCalls.Visitor visitorTypeName) {

            }
        });
    }*/
    }
}
