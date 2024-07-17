package com.example.vmsv1;

import static java.sql.Types.NULL;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.functions.FirebaseFunctions;
//import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.FirebaseApp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class testactivity extends AppCompatActivity {

    FirebaseFirestore db;
    ProgressBar progressBar;
    GridView gv;

    ArrayList<ArrayList<String>> data_db = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_testactivity);

        progressBar = findViewById(R.id.progressBar);
        gv = findViewById(R.id.gridview1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar.setVisibility(View.VISIBLE);
        gv.setVisibility(View.GONE);

        /*FirebaseFunctionCalls.getVisitorEntry(db, new DataRetrievedCallback() {
            @Override
            public void onGridViewDataRetrieved(ArrayList<Item> itemList) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    gv.setVisibility(View.VISIBLE);
                    GridAdapter gridadapter = new GridAdapter(getApplicationContext(), itemList);
                    gv.setAdapter(gridadapter);
                });
            }

            @Override
            public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {

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
    }

}*/
    }
}