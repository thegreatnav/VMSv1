package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.vmsv1.ItemDomain;
import com.example.vmsv1.R;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationMaster extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_master);

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

        recyclerView = findViewById(R.id.LMrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        List<String> headers = Arrays.asList("Location ID", "Location Name", "Status");

        // Sample data for dynamic table
        List<DataModel> dataList = new ArrayList<>();
        Map<String, Object> data1 = new HashMap<>();
        data1.put("Location ID", 1);
        data1.put("Location Name", " A");
        data1.put("Status", 1000);
        dataList.add(new DataModel(data1));

        Map<String, Object> data2 = new HashMap<>();
        data2.put("Location ID", 2);
        data2.put("Location Name", " B");
        data2.put("Status", 2000);
        dataList.add(new DataModel(data2));

        Map<String, Object> data3 = new HashMap<>();
        data3.put("Location ID", 3);
        data3.put("Location Name", " C");
        data3.put("Status", 3000);
        dataList.add(new DataModel(data3));

        /*FirebaseFunctionCalls.getLocationMaster(db, new DataRetrievedCallback() {
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
        });*/
    }
}