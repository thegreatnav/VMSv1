package com.example.vmsv1.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vmsv1.BackgroundTaskExecutor;
import com.example.vmsv1.GridAdapter_ManageVisitor;
import com.example.vmsv1.R;

import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.db.DatabaseHelperSQL;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageVisitor extends AppCompatActivity {

    DatabaseHelperSQL db;
    ProgressBar progressBar;
    GridView gv;

    private ImageButton entry_datepicker_btn;
    private TextView entrydate,entry_date,mobileno, unit_name,mobileno_edit_text,visitorname,visitorname_edit_text,exitstatus;
    Spinner exit_status_spinner, gate_spinner;
    String userId, defaultGateId, sbuId, gateName, sbuName;
    Button search_button;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_visitor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelperSQL();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
            Log.d("act", userId + " is userId");
            defaultGateId = intent.getStringExtra("defaultGateId");
            Log.d("act", defaultGateId + " is defaultGateId");
            sbuId = intent.getStringExtra("sbuId");
            Log.d("act", sbuId + " is sbuId");
        }

        unit_name = findViewById(R.id.unit_name);
        gate_spinner = findViewById(R.id.gate_spinner);

        entrydate=findViewById(R.id.entrydate);
        entry_date = findViewById(R.id.entry_date);
        entry_datepicker_btn = findViewById(R.id.entry_datepicker_btn);
        mobileno=findViewById(R.id.mobileno);
        mobileno_edit_text=findViewById(R.id.mobileno_edit_text);
        visitorname=findViewById(R.id.visitorname);
        visitorname_edit_text=findViewById(R.id.visitorname_edit_text);
        exitstatus=findViewById(R.id.exitstatus);
        exit_status_spinner = findViewById(R.id.exit_status_spinner);
        search_button=findViewById(R.id.search_button);
        gv = findViewById(R.id.gridview1);
        progressBar=findViewById(R.id.progressBar);

        entry_datepicker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ManageVisitor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {
                        entry_date.setText((dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year1);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        List<List<String>> gatespinnerArray;
        gatespinnerArray = db.getGateListSpinner("MTL", sbuId);

        if (gatespinnerArray != null && !gatespinnerArray.isEmpty()) {
            ArrayList<String> gatespinnerarray2 = new ArrayList<String>();
            for (int i = 0; i < gatespinnerArray.size(); i++) {
                gatespinnerarray2.add(gatespinnerArray.get(i).get(2));
            }
            ArrayAdapter<String> gateadapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, gatespinnerarray2);
            gateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gate_spinner.setAdapter(gateadapter);

            sbuName = gatespinnerArray.get(0).get(0);
            unit_name.setText(sbuName);
        } else {
            Log.e("ManageVisitor", "Gate spinner array is empty or null");
            // Handle the empty array case appropriately, e.g., show a message or disable related UI elements
            Toast.makeText(this, "No gates available", Toast.LENGTH_SHORT).show();
        }

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Not sure");
        spinnerArray.add("Yes");
        spinnerArray.add("No");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exit_status_spinner.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String entryDate_value = entry_date.getText().toString();
                String mobilenum_value = mobileno_edit_text.getText().toString();
                String visitorname_value = visitorname_edit_text.getText().toString();
                String exitstatus_value = exit_status_spinner.getSelectedItem().toString();

                if (entry_date.getVisibility() == View.VISIBLE)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    search_button.setText("BACK");
                    entrydate.setVisibility(View.GONE);
                    entry_date.setVisibility(View.GONE);
                    entry_datepicker_btn.setVisibility(View.GONE);
                    mobileno.setVisibility(View.GONE);
                    mobileno_edit_text.setVisibility(View.GONE);
                    visitorname.setVisibility(View.GONE);
                    visitorname_edit_text.setVisibility(View.GONE);
                    exitstatus.setVisibility(View.GONE);
                    exit_status_spinner.setVisibility(View.GONE);
                }
                else
                {
                    search_button.setText("SEARCH");
                    entrydate.setVisibility(View.VISIBLE);
                    entry_date.setVisibility(View.VISIBLE);
                    entry_datepicker_btn.setVisibility(View.VISIBLE);
                    mobileno.setVisibility(View.VISIBLE);
                    mobileno_edit_text.setVisibility(View.VISIBLE);
                    visitorname.setVisibility(View.VISIBLE);
                    visitorname_edit_text.setVisibility(View.VISIBLE);
                    exitstatus.setVisibility(View.VISIBLE);
                    exit_status_spinner.setVisibility(View.VISIBLE);
                }

                if(entryDate_value.equals("") && ((!mobilenum_value.equals(""))||(!exitstatus_value.equals("Not sure"))||(!visitorname_value.equals(""))))
                {
                    Date currentUtilDate = new Date();
                    Timestamp currentTimestamp = new Timestamp(currentUtilDate.getTime());

                    String dateString = "01/01/2000 00:00:00";
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date parsedDate = null;
                    try {
                        parsedDate = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Timestamp oldestTimestamp = new Timestamp(parsedDate.getTime());

                    if(exitstatus_value.equals("Yes"))
                        exitstatus_value="Y";
                    else if(exitstatus_value.equals("No"))
                        exitstatus_value="N";
                    else
                        exitstatus_value="";

                    String finalExitstatus_value = exitstatus_value;
                    BackgroundTaskExecutor.runOnBackgroundThread(() -> {
                        List<VisitorSearchResult> visitorList = new ArrayList<>();
                        visitorList = db.getVisitorList(Integer.parseInt(sbuId), Integer.parseInt(defaultGateId), oldestTimestamp, currentTimestamp, 0, visitorname_value, mobilenum_value, "", "", 0, 0, "", "", "", "", "", finalExitstatus_value, Integer.parseInt(userId));

                        List<VisitorSearchResult> finalVisitorList = visitorList;
                        mainHandler.post(() -> {
                            progressBar.setVisibility(View.GONE);
                            gv.setVisibility(View.VISIBLE);
                            GridAdapter_ManageVisitor gridadapter = new GridAdapter_ManageVisitor(getApplicationContext(), finalVisitorList);
                            gv.setAdapter(gridadapter);
                        });
                    });
                }
                else if(entryDate_value.equals("") && mobilenum_value.equals("") && visitorname_value.equals("") && (exitstatus_value.equals("Not sure")))
                {
                    BackgroundTaskExecutor.runOnBackgroundThread(() -> {
                        List<VisitorSearchResult> visitorList = new ArrayList<>();
                        int num = 5;
                        VisitorSearchResult vs_obj = db.getVisitorDetails(num);

                        while (vs_obj != null) {
                            visitorList.add(vs_obj);
                            num++;
                            vs_obj = db.getVisitorDetails(num);
                        }

                        mainHandler.post(() -> {
                            progressBar.setVisibility(View.GONE);
                            gv.setVisibility(View.VISIBLE);
                            GridAdapter_ManageVisitor gridadapter = new GridAdapter_ManageVisitor(getApplicationContext(), visitorList);
                            gv.setAdapter(gridadapter);
                        });
                    });
                }
                else
                {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                    Date entryDate, utilDate = null;
                    Timestamp sqlTimestamp = null;
                    try {
                        entryDate = inputDateFormat.parse(entryDate_value);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String formattedDate = outputDateFormat.format(entryDate);
                    try {
                        utilDate = outputDateFormat.parse(formattedDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    sqlTimestamp = new Timestamp(utilDate.getTime());

                    Timestamp finalSqlTimestamp = sqlTimestamp;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(utilDate);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date updatedDate = calendar.getTime();
                    Timestamp updatedTimestamp = new Timestamp(updatedDate.getTime());
                    if(exitstatus_value.equals("Yes"))
                        exitstatus_value="Y";
                    else if(exitstatus_value.equals("No"))
                        exitstatus_value="N";
                    else
                        exitstatus_value="";

                    String finalExitstatus_value1 = exitstatus_value;
                    BackgroundTaskExecutor.runOnBackgroundThread(() -> {
                        List<VisitorSearchResult> visitorList = new ArrayList<>();
                        visitorList = db.getVisitorList(Integer.parseInt(sbuId), Integer.parseInt(defaultGateId), finalSqlTimestamp, updatedTimestamp, 0, visitorname_value, mobilenum_value, "", "", 0, 0, "", "", "", "", "", finalExitstatus_value1, Integer.parseInt(userId));

                        List<VisitorSearchResult> finalVisitorList = visitorList;
                        mainHandler.post(() -> {
                            progressBar.setVisibility(View.GONE);
                            gv.setVisibility(View.VISIBLE);
                            GridAdapter_ManageVisitor gridadapter = new GridAdapter_ManageVisitor(getApplicationContext(), finalVisitorList);
                            gv.setAdapter(gridadapter);
                        });
                    });
                }
            }
        });

    }
}
