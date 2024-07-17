package com.example.vmsv1.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.ItemDomain;
import com.example.vmsv1.MainActivity;
import com.example.vmsv1.R;

import com.example.vmsv1.db.DatabaseHelperSQL;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {

    //FirebaseFirestore db;
    DatabaseHelperSQL dbsql;
    String userId,defaultGateId,sbuId;

    private Button login_btn;
    private EditText password;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //FirebaseApp.initializeApp(this);
        //db = FirebaseFirestore.getInstance();

        login_btn = findViewById(R.id.login);
        password =findViewById(R.id.ps);
        username =findViewById(R.id.un);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String us =username.getText().toString().trim();
                String p =password.getText().toString().trim();

                login_validation(us, p, new DataRetrievedCallback(){
                    @Override
                    public void onGridViewDataRetrieved(ArrayList<Item> itemList) {

                    }

                    @Override
                    public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {

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

                    @Override
                    public void onLoginCallback(boolean isSuccess, String message, String userId,String defaultGateId,String sbuId) {
                        runOnUiThread(() -> Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show());
                        if (isSuccess) {
                            Intent i = new Intent(LoginPage.this, MainActivity.class);
                            /*Log.d("Act","userid="+userId);
                            Log.d("Act","sbuid="+sbuId);
                            Log.d("Act","defaultid="+defaultGateId);
                            i.putExtra("userId",userId);
                            i.putExtra("sbuId",sbuId);
                            i.putExtra("defaultGateId",defaultGateId);
                            startActivity(i);
                            finish();

                        }
                    }
                });
                */

                String us = username.getText().toString().trim();
                String p = password.getText().toString().trim();



                if (us.isEmpty() || p.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbsql = new DatabaseHelperSQL();
                List<String> values = dbsql.loginValidation(us, p);

                if (values.get(0).equals("0")) {
                    Toast.makeText(LoginPage.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
                else if (!values.get(3).equals("Active")){
                    Toast.makeText(LoginPage.this, "User is Inactive", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    String UserID = values.get(0);
                    String FullName = values.get(4);
                    String SbuID = values.get(5);
                    String DefaultGateId = values.get(6);

                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    intent.putExtra("userId", UserID);
                    intent.putExtra("sbuId", SbuID);
                    intent.putExtra("FullName", FullName);
                    intent.putExtra("defaultGateId",DefaultGateId);
                    startActivity(intent);
                }
            }
        });
    }

    /*private void login_validation(String username, String password, DataRetrievedCallback callback) {
        if (password.isEmpty() || username.isEmpty()) {
            callback.onLoginCallback(false, "Username or password cannot be empty","","","");
            return;
        }

        db.collection("User_Master")
                .whereEqualTo("UserName", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                String storedPassword = document.getString("Password");
                                userId=String.valueOf(document.get("UserId"));
                                //Log.d("Act","Userid="+userId);
                                defaultGateId=String.valueOf(document.get("DefaultGateId"));
                                //Log.d("Act","defaulgateid="+defaultGateId);
                                sbuId=String.valueOf(document.get("SBUId"));
                                //Log.d("Act","Sbuid="+sbuId);
                                if (password.equals(storedPassword)) {
                                    callback.onLoginCallback(true, "Login successful",userId,defaultGateId,sbuId);
                                } else {
                                    callback.onLoginCallback(false, "Invalid password","","","");
                                }
                            } else {
                                callback.onLoginCallback(false, "User does not exist","","","");
                            }
                        } else {
                            callback.onLoginCallback(false, "Error getting user data","","","");
                        }
                    }
                });
    }*/

}