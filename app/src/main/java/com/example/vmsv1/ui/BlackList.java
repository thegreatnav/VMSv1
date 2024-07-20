package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.dataitems.BlackListVisitor;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackList extends AppCompatActivity {

    DatabaseHelperSQL db;

    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<DataModel> dataList;
    private EditText editTextMobile, editTextName, editTextReason;
    private View inputContainer;
    String userId="5";
    String sbuId="4";
    String defaultGateId="11";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelperSQL();
        handler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
            if(intent.hasExtra("sbuId"))
            {
                sbuId=intent.getStringExtra("sbuId");
            }
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_black_list);

        Button buttonAddVisitor = findViewById(R.id.buttonAddVisitor);
        Button buttonSave = findViewById(R.id.buttonSave);

        inputContainer = findViewById(R.id.inputContainer);
        editTextMobile = findViewById(R.id.editTextLocationId);
        editTextName = findViewById(R.id.editTextStatus);
        editTextReason = findViewById(R.id.editTextLocationName);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> headers = Arrays.asList("Mobile Number", "Name", "Reason","Date Added","Action");
        fetchBlacklist(headers);

        buttonAddVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputContainer.getVisibility()==View.GONE)
                    inputContainer.setVisibility(View.VISIBLE);
                else
                    inputContainer.setVisibility(View.GONE);
            }
        });

        buttonSave.setOnClickListener(v -> {
            try {

                String mobile = editTextMobile.getText().toString();
                String name = editTextName.getText().toString();
                String reason = editTextReason.getText().toString();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

                List<String> message=db.addNewBlackListVisitor(mobile,name,reason,Integer.parseInt(userId));
                Toast.makeText(getApplicationContext(),String.valueOf(message.get(1)),Toast.LENGTH_SHORT).show();
                handler.postDelayed(() -> {
                    fetchBlacklist(headers);
                    editTextMobile.setText("");
                    editTextName.setText("");
                    editTextReason.setText("");
                    inputContainer.setVisibility(View.GONE);
                }, 1000);
            }
            catch (Exception e) {
                Log.e("BlackList", "Error adding data to the list", e);
            }
        });
    }

    private void fetchBlacklist(List<String> headers) {
        dataList = new ArrayList<>();
        try {
            List<BlackListVisitor> blackListVisitors = db.getBlackListVisitorList("", "", Integer.parseInt(userId));
            dataList.clear();
            for (BlackListVisitor visitor : blackListVisitors) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("Mobile Number", visitor.getMobileNo());
                dataMap.put("Name", visitor.getName());
                dataMap.put("Reason", visitor.getReason());
                dataMap.put("Date Added", visitor.getCreatedDate());

                dataList.add(new DataModel(dataMap));
            }
            tableAdapter = new TableAdapter(dataList, headers, true, "Delete",db,userId);
            recyclerView.setAdapter(tableAdapter);
        } catch (Exception e) {
            Toast.makeText(BlackList.this, "Error fetching blacklist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}
