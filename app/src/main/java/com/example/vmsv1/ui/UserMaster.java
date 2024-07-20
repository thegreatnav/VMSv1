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
import com.example.vmsv1.dataitems.UserProfile;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMaster extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    DatabaseHelperSQL db;
    private List<DataModel> dataList;
    String userId, defaultGateId, sbuId;
    Button addNewLocation_button,save_button;
    private View inputContainer;
    TextView locationId_textview,locationName_textview,status_textview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_master);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId") && intent.hasExtra("sbuId")) {
            userId = intent.getStringExtra("userId");
            sbuId = intent.getStringExtra("sbuId");
            defaultGateId=intent.getStringExtra("defaultGateId");
            Toast.makeText(this, userId + " " + sbuId, Toast.LENGTH_LONG).show();
        }
        db = new DatabaseHelperSQL();
        handler=new Handler(Looper.getMainLooper());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.UserRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNewLocation_button=findViewById(R.id.lmbutton);
        inputContainer=findViewById(R.id.inputContainer);
        save_button=findViewById(R.id.buttonSave);
        locationId_textview=findViewById(R.id.editTextLocationId);
        locationName_textview=findViewById(R.id.editTextLocationName);
        status_textview=findViewById(R.id.editTextStatus);

        // Example data for dynamic table
        List<String> headers = Arrays.asList("User Id", "Username", "Full Name","Login Method","SBU","Status","Reset");
        List<UserProfile> userList = new ArrayList<>();
        dataList = new ArrayList<>();
        try {
            userList= db.getUserList("","","", Integer.parseInt(sbuId),"");
            Log.d("Userlist",String.valueOf(userList.get(0)));
            for (UserProfile obj : userList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("User Id", obj.getUserId());
                dataMap.put("Username", obj.getUsername());
                dataMap.put("Full Name", obj.getFullName());
                dataMap.put("Login Method", obj.getLoginType());
                dataMap.put("SBU", obj.getSbuName());
                dataMap.put("Status", obj.getStatus());
                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, true, "Reset Password",db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching Userlist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}