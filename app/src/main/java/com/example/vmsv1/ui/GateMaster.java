package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GateMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gate_master);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            String value = intent.getStringExtra("userId");
            Toast.makeText(this, "Received value: " + value, Toast.LENGTH_LONG).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        List<String> headers = Arrays.asList("Gate ID", "Gate Name", "Company","SBU","Status");

        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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