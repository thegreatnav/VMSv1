package com.example.vmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.R;
import com.example.vmsv1.dataitems.BlackListVisitor;
import com.example.vmsv1.db.DatabaseHelperSQL;
import com.example.vmsv1.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackList extends AppCompatActivity implements TableAdapter.DeleteVisitorCallback, TableAdapter.AddVisitorCallback {

    private DatabaseHelperSQL db;
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private List<DataModel> dataList;
    private EditText editTextMobile, editTextName, editTextReason;
    private View inputContainer;
    private String userId = "1";
    private String sbuId = "4";
    private String defaultGateId = "11";
    private Handler handler;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelperSQL();

        handler = new Handler(Looper.getMainLooper());

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
            defaultGateId = intent.getStringExtra("defaultGateId");
            sbuId = intent.getStringExtra("sbuId");
            Log.d("Black list Act", "userid=" + userId);
            Log.d("Black list Act", "sbuid=" + sbuId);
            Log.d("Black list Act", "defaultid=" + defaultGateId);
            sharedViewModel.setUserId(userId);
            sharedViewModel.setSbuId(sbuId);
            sharedViewModel.setDefaultGateId(defaultGateId);
        } else {
            Toast.makeText(this, "Received userId: " + userId, Toast.LENGTH_LONG).show();
        }

        // Assuming EdgeToEdge is a class or method that handles edge-to-edge display
        // If not, remove or define EdgeToEdge.enable(this);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_black_list);

        Button buttonAddVisitor = findViewById(R.id.buttonAddVisitor);
        Button buttonSave = findViewById(R.id.buttonSave);


        inputContainer = findViewById(R.id.inputContainer);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextName = findViewById(R.id.editTextName);
        editTextReason = findViewById(R.id.editTextReason);

        recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Example data for dynamic table
        List<String> headers = Arrays.asList("Mobile Number", "Name", "Reason", "Date Added", "Action");

        // Sample data for dynamic table
        dataList = new ArrayList<>();

        tableAdapter = new TableAdapter(dataList, headers, true, "Delete", db, this, this);
        recyclerView.setAdapter(tableAdapter);

        buttonAddVisitor.setOnClickListener(v -> inputContainer.setVisibility(View.VISIBLE));

        fetchBlacklist();

        buttonSave.setOnClickListener(v -> {
            try {
                String mobile = editTextMobile.getText().toString();
                String name = editTextName.getText().toString();
                String reason = editTextReason.getText().toString();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

                // Check if visitor already exists in blacklist
                BlackListVisitor existingVisitor = db.checkBlackListVisitor(mobile, Integer.parseInt(userId));
                if (existingVisitor != null) {
                    Toast.makeText(BlackList.this, "Visitor with Mobile Number " + mobile + " is already blacklisted.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Add visitor to blacklist
                addVisitorToBlackList(mobile, name, reason, userId, currentDate);

            } catch (Exception e) {
                Toast.makeText(BlackList.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ActivityTag", "Exception: ", e);
            }
        });
    }

    private void fetchBlacklist() {
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

            tableAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(BlackList.this, "Error fetching blacklist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }

    // Method to delete visitor from blacklist
    private void deleteVisitorFromBlacklist(String mobileNo) {
        try {
            int deleteCount = db.deleteVisitorFromBlackList(mobileNo, Integer.parseInt(userId));
            if (deleteCount > 0) {
                Toast.makeText(BlackList.this, "Visitor with Mobile Number " + mobileNo + " deleted from blacklist.", Toast.LENGTH_LONG).show();

                // Optional: Refresh blacklist after deletion
                fetchBlacklist();
            } else {
                Toast.makeText(BlackList.this, "Failed to delete visitor from blacklist.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(BlackList.this, "Error deleting visitor from blacklist: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }

    @Override
    public void onDeleteVisitor(String mobileNo) {
        deleteVisitorFromBlacklist(mobileNo);
    }

    @Override
    public void onAddVisitor(String mobileNo, String name, String reason, String dateAdded) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Mobile Number", mobileNo);
        dataMap.put("Name", name);
        dataMap.put("Reason", reason);
        dataMap.put("Date Added", dateAdded);

        dataList.add(new DataModel(dataMap));
        tableAdapter.notifyItemInserted(dataList.size()-1);
        tableAdapter.notifyDataSetChanged();
        inputContainer.setVisibility(View.GONE);
    }

    private void addVisitorToBlackList(String mobile, String name, String reason, String userId, String currentDate) {
        try {
            int userID = Integer.parseInt(userId);
            List<String> result = db.addNewBlackListVisitor(mobile, name, reason, userID);
            String idResult = result.get(0);
            String messageResult = result.get(1);

            Toast.makeText(BlackList.this, "Result: " + idResult + ", " + messageResult, Toast.LENGTH_LONG).show();

            if (!idResult.contains("Not Available") && !idResult.contains("0")) {
                // Add the new entry to the data list and update the table
                onAddVisitor(mobile, name, reason, currentDate);

                editTextMobile.setText("");
                editTextName.setText("");
                editTextReason.setText("");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchBlacklist(); // Refresh blacklist after delay
                    }
                }, 2000);


            } else {
                // Show error message
                Toast.makeText(BlackList.this, "Failed to add visitor to blacklist: " + messageResult, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(BlackList.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("ActivityTag", "Exception: ", e);
        }
    }
}
