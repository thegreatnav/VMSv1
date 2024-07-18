package com.example.vmsv1.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vmsv1.BackgroundTaskExecutor;
import com.example.vmsv1.GridAdapter_DailyVisitor;
import com.example.vmsv1.GridAdapter_ManageVisitor;
import com.example.vmsv1.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.vmsv1.dataitems.VisitorSearchResult;
import com.example.vmsv1.db.DatabaseHelperSQL;

public class DailyVisitor extends AppCompatActivity {

    DatabaseHelperSQL db;
    ProgressBar progressBar;
    String userId, defaultGateId, sbuId, sbuName;
    private ImageButton fromdate_button, todate_button;
    private TextView fromdate, visitorname, visitorname_edit_text, place, place_edit_text, visitortype_edit_text, exitstatus, todate, mobilenum, mobilenum_edit_text, visitorcompany, visitorcompany_edit_text, visitingstaff, visitingstaff_edit_text, fromdate_picker, todate_picker, sbu;
    Spinner exit_status_spinner, visitor_type_spinner, gate_spinner;
    GridView gv;
    Button search_button;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_visitor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gate_spinner = findViewById(R.id.gate_spinner);
        sbu = findViewById(R.id.unit_name);

        fromdate = findViewById(R.id.fromdate);
        fromdate_picker = findViewById(R.id.fromdate_picker);
        fromdate_button = findViewById(R.id.fromdate_btn);
        visitorname = findViewById(R.id.visitorname);
        visitorname_edit_text = findViewById(R.id.visitorname_edit_text);
        place = findViewById(R.id.place);
        place_edit_text = findViewById(R.id.place_edit_text);
        visitortype_edit_text = findViewById(R.id.visitortype_edit_text);
        visitor_type_spinner = findViewById(R.id.visitortype_spinner);
        exitstatus = findViewById(R.id.exitstatus);
        exit_status_spinner = findViewById(R.id.exitstatus_spinner);
        todate = findViewById(R.id.todate);
        todate_picker = findViewById(R.id.todate_picker);
        todate_button = findViewById(R.id.todate_button);
        mobilenum = findViewById(R.id.mobilenum);
        mobilenum_edit_text = findViewById(R.id.mobilenum_edit_text);
        visitorcompany = findViewById(R.id.visitorcompany);
        visitorcompany_edit_text = findViewById(R.id.visitorcompany_edit_text);
        visitingstaff = findViewById(R.id.visitingstaff);
        visitingstaff_edit_text = findViewById(R.id.visitingstaff_edit_text);
        search_button = findViewById(R.id.search);
        gv = findViewById(R.id.gridview1);
        progressBar=findViewById(R.id.progressBar);

        fromdate_button.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DailyVisitor.this, (view, year1, monthOfYear, dayOfMonth) ->
                    fromdate_picker.setText((dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        todate_button.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DailyVisitor.this, (view, year1, monthOfYear, dayOfMonth) ->
                    todate_picker.setText((dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        List<List<String>> gatespinnerArray;
        gatespinnerArray = db.getGateListSpinner("MTL", sbuId);

        ArrayList<String> gatespinnerarray2 = new ArrayList<>();
        for (List<String> gate : gatespinnerArray) {
            gatespinnerarray2.add(gate.get(2));
        }
        ArrayAdapter<String> gateadapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, gatespinnerarray2);
        gateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gate_spinner.setAdapter(gateadapter);

        sbuName = gatespinnerArray.get(0).get(0);
        sbu.setText(sbuName);
        progressBar.setVisibility(View.GONE);
        gv.setVisibility(View.GONE);

        search_button.setOnClickListener(view -> {

            /*SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String fromdateInString = fromdate_picker.getText().toString();
            Date entrydatefrom;
            java.sql.Date entrydatefrom3=null;
            try {
                // Parse the input date string to java.util.Date
                entrydatefrom = inputDateFormat.parse(fromdateInString);
                Log.d("Tag1", "entry1=" + entrydatefrom);

                // Format the java.util.Date to the desired string format
                String formattedDate = outputDateFormat.format(entrydatefrom);
                Log.d("Tag1", "formattedDate=" + formattedDate);

                // Convert java.util.Date to java.sql.Date
                entrydatefrom3 = new java.sql.Date(entrydatefrom.getTime());
                Log.d("Tag1", "entry3=" + entrydatefrom3);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String todateInString = todate_picker.getText().toString();
            Date entrydateto,entrydateto2;
            java.sql.Date entrydateto3;

            try {
                entrydateto = inputDateFormat.parse(todateInString);
                entrydateto2= outputDateFormat.parse(String.valueOf(entrydateto));
                Log.d("Tag1","entry2="+String.valueOf(entrydateto2));
                entrydateto3 = new java.sql.Date(entrydateto2.getTime());
                Log.d("Tag1","entry3="+String.valueOf(entrydateto3));


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }*/

            String visitorname_text=visitorname_edit_text.getText().toString();

            String mobilenumber_text=mobilenum_edit_text.getText().toString();

            String visitorplace_text=place_edit_text.getText().toString();

            String visitorcompany_text=visitorcompany_edit_text.getText().toString();

            String visitortypeid_text=visitor_type_spinner.getSelectedItem().toString();

            if (fromdate.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
                search_button.setText("BACK");
                fromdate.setVisibility(View.GONE);
                fromdate_picker.setVisibility(View.GONE);
                fromdate_button.setVisibility(View.GONE);
                visitorname.setVisibility(View.GONE);
                visitorname_edit_text.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
                place_edit_text.setVisibility(View.GONE);
                visitortype_edit_text.setVisibility(View.GONE);
                visitor_type_spinner.setVisibility(View.GONE);
                exitstatus.setVisibility(View.GONE);
                exit_status_spinner.setVisibility(View.GONE);
                todate.setVisibility(View.GONE);
                todate_picker.setVisibility(View.GONE);
                todate_button.setVisibility(View.GONE);
                mobilenum.setVisibility(View.GONE);
                mobilenum_edit_text.setVisibility(View.GONE);
                visitorcompany.setVisibility(View.GONE);
                visitorcompany_edit_text.setVisibility(View.GONE);
                visitingstaff.setVisibility(View.GONE);
                visitingstaff_edit_text.setVisibility(View.GONE);
            } else {
                search_button.setText("SEARCH");
                fromdate.setVisibility(View.VISIBLE);
                fromdate_picker.setVisibility(View.VISIBLE);
                fromdate_button.setVisibility(View.VISIBLE);
                visitorname.setVisibility(View.VISIBLE);
                visitorname_edit_text.setVisibility(View.VISIBLE);
                place.setVisibility(View.VISIBLE);
                place_edit_text.setVisibility(View.VISIBLE);
                visitortype_edit_text.setVisibility(View.VISIBLE);
                visitor_type_spinner.setVisibility(View.VISIBLE);
                exitstatus.setVisibility(View.VISIBLE);
                exit_status_spinner.setVisibility(View.VISIBLE);
                todate.setVisibility(View.VISIBLE);
                todate_picker.setVisibility(View.VISIBLE);
                todate_button.setVisibility(View.VISIBLE);
                mobilenum.setVisibility(View.VISIBLE);
                mobilenum_edit_text.setVisibility(View.VISIBLE);
                visitorcompany.setVisibility(View.VISIBLE);
                visitorcompany_edit_text.setVisibility(View.VISIBLE);
                visitingstaff.setVisibility(View.VISIBLE);
                visitingstaff_edit_text.setVisibility(View.VISIBLE);
            }

            //java.sql.Date finalEntrydatefrom = entrydatefrom3;
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
                    GridAdapter_DailyVisitor gridadapter = new GridAdapter_DailyVisitor(getApplicationContext(), visitorList);
                    gv.setAdapter(gridadapter);
                });
            });
        });

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Both");
        spinnerArray.add("Yes");
        spinnerArray.add("No");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exit_status_spinner.setAdapter(adapter);

        List<String> visitortype_spinnerArray = new ArrayList<>();
        visitortype_spinnerArray.add("1");
        visitortype_spinnerArray.add("2");
        visitortype_spinnerArray.add("3");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, visitortype_spinnerArray);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visitor_type_spinner.setAdapter(adapter1);
    }
}
