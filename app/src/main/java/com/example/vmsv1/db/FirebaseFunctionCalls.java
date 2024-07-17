package com.example.vmsv1.db;

import static java.sql.Types.NULL;

import com.example.vmsv1.DataModel;
import com.example.vmsv1.ItemDomain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

/*public class FirebaseFunctionCalls {

    public static void getVisitorEntry(FirebaseFirestore db,DataRetrievedCallback callback) {

        ArrayList<String> visitor_ids = new ArrayList<>();
        ArrayList<String> mob_no = new ArrayList<>();
        ArrayList<String> visitor_name = new ArrayList<>();
        ArrayList<String> visitor_designation = new ArrayList<>();
        ArrayList<String> visitor_place = new ArrayList<>();
        ArrayList<String> visitor_company = new ArrayList<>();
        ArrayList<String> visiting_staff = new ArrayList<>();
        ArrayList<String> approver_name = new ArrayList<>();
        ArrayList<String> purpose = new ArrayList<>();
        ArrayList<String> visiting_area = new ArrayList<>();
        ArrayList<String> entry_time = new ArrayList<>();
        ArrayList<String> exit_time = new ArrayList<>();
        ArrayList<Boolean> camera_status = new ArrayList<>();
        ArrayList<String> camera_status_string = new ArrayList<>();
        ArrayList<Boolean> nda_status = new ArrayList<>();
        ArrayList<String> nda_status_string = new ArrayList<>();
        ArrayList<Boolean> print_status = new ArrayList<>();
        ArrayList<String> print_status_string = new ArrayList<>();
        ArrayList<Boolean> exit_status = new ArrayList<>();
        ArrayList<String> exit_status_string = new ArrayList<>();

        ArrayList<ItemDomain> itemList = new ArrayList<>();
        db.collection("Visitor_Entry")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=-1;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                i++;

                                Log.d("ActivityTag", document.getId() + " => " + document.getData());

                                String visitorIdString=(String.valueOf(document.get("VisitorId")));
                                if(visitorIdString.equals("0"))
                                    visitor_ids.add("-");
                                else
                                    visitor_ids.add(visitorIdString);
                                Log.d("ActivityTag", (String.valueOf(document.get("VisitorId"))));

                                String mobileNoString=(String.valueOf(document.get("MobileNo")));
                                if(mobileNoString.equals("0"))
                                    mob_no.add("-");
                                else
                                    mob_no.add(mobileNoString);
                                Log.d("ActivityTag", (String.valueOf( document.get("MobileNo"))));

                                String visitorNameString=(String.valueOf(document.get("VisitorName")));
                                if(visitorNameString.equals("0"))
                                    visitor_name.add("-");
                                else
                                    visitor_name.add(visitorNameString);
                                Log.d("ActivityTag", (String.valueOf( document.get("VisitorName"))));

                                String visitorDesignationString=(String.valueOf(document.get("visitorDesignation")));
                                if(visitorDesignationString.equals("0"))
                                    visitor_designation.add("-");
                                else
                                    visitor_designation.add(visitorDesignationString);
                                Log.d("ActivityTag", (String.valueOf(document.get("visitorDesignation"))));

                                String visitorPlaceString=(String.valueOf(document.get("VisitorPlace")));
                                if(visitorPlaceString.equals("0"))
                                    visitor_place.add("-");
                                else
                                    visitor_place.add(visitorPlaceString);
                                Log.d("ActivityTag", (String.valueOf(document.get("VisitorPlace"))));

                                String visitorCompanyString=(String.valueOf(document.get("VisitorCompany")));
                                if(visitorCompanyString.equals("0"))
                                    visitor_company.add("-");
                                else
                                    visitor_company.add(visitorCompanyString);
                                Log.d("ActivityTag", (String.valueOf( document.get("VisitorCompany"))));

                                String visitingStaffString=(String.valueOf(document.get("VisitingStaff")));
                                if(visitingStaffString.equals("0"))
                                    visiting_staff.add("-");
                                else
                                    visiting_staff.add(visitingStaffString);
                                Log.d("ActivityTag", (String.valueOf(document.get("VisitingStaff"))));

                                String approverNameString=(String.valueOf(document.get("ApproverName")));
                                if(approverNameString.equals("0"))
                                    approver_name.add("-");
                                else
                                    approver_name.add(approverNameString);
                                Log.d("ActivityTag", (String.valueOf(document.get("ApproverName"))));

                                String purposeString=(String.valueOf(document.get("Purpose")));
                                if(purposeString.equals("0"))
                                    purpose.add("-");
                                else
                                    purpose.add(purposeString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Purpose"))));

                                String visitingAreaIdString=(String.valueOf(document.get("VisitingAreaId")));
                                if(visitingAreaIdString.equals("0"))
                                    visiting_area.add("-");
                                else
                                    visiting_area.add(visitingAreaIdString);
                                Log.d("ActivityTag", (String.valueOf( document.get("VisitingAreaId"))));

                                String entrytimeString=(String.valueOf(document.get("EntryDateTime")));
                                if(entrytimeString.equals("0"))
                                    entry_time.add("-");
                                else
                                {
                                    Date entry_time_item;
                                    SimpleDateFormat entry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                    try {
                                        entry_time_item = entry.parse(String.valueOf(entrytimeString));
                                        Log.d("ActivityTag", "Parse entry success");

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    entry_time.add(entrytimeString);
                                }
                                Log.d("ActivityTag", "Added item to entry_time");

                                String exittimeString=String.valueOf(document.get("ExitDateTime"));
                                if(exittimeString.equals("0"))
                                    exit_time.add("-");
                                else
                                {
                                    Date exit_time_item;
                                    SimpleDateFormat exit = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                    try {
                                        exit_time_item = exit.parse(String.valueOf(exittimeString));
                                        Log.d("ActivityTag", "Parse exit success");
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    exit_time.add(exittimeString);
                                }
                                Log.d("ActivityTag", "Added item to exit_time");


                                camera_status_string.add(String.valueOf(document.get("CameraStatus")));
                                Log.d("ActivityTag",String.valueOf(document.get("CameraStatus")));
                                if(camera_status_string.get(i).equals("Y"))
                                    camera_status.add(true);
                                else
                                    camera_status.add(false);

                                nda_status_string.add(String.valueOf(document.get("NDAStatus")));
                                Log.d("ActivityTag",String.valueOf(document.get("NDAStatus")));
                                if(nda_status_string.get(i).equals("Y"))
                                    nda_status.add(true);
                                else
                                    nda_status.add(false);

                                print_status_string.add(String.valueOf(document.get("PrintStatus")));
                                Log.d("ActivityTag",String.valueOf(document.get("PrintStatus")));
                                if(print_status_string.get(i).equals("Y"))
                                    print_status.add(true);
                                else
                                    print_status.add(false);

                                exit_status_string.add(String.valueOf(document.get("ExitStatus")));
                                Log.d("ActivityTag",String.valueOf(document.get("ExitStatus")));
                                if(exit_status_string.get(i).equals("Y"))
                                    exit_status.add(true);
                                else
                                    exit_status.add(false);

                                itemList.add(new Item(visitor_ids.get(i), mob_no.get(i), visitor_name.get(i), visitor_designation.get(i), visitor_place.get(i), visitor_company.get(i), visiting_staff.get(i), approver_name.get(i), purpose.get(i), visiting_area.get(i), entry_time.get(i), exit_time.get(i), camera_status.get(i), nda_status.get(i), print_status.get(i), exit_status.get(i)));

                            }
                        } else {
                            Log.e("ActivityTag", "Error getting documents.", task.getException());
                        }
                        callback.onGridViewDataRetrieved(itemList);
                    }
                });
    }

    public static void addVisitorEntry(FirebaseFirestore db)
    {
        Map<String, Object> visitor = new HashMap<>();
        visitor.put("UniqueId", "2");
        visitor.put("VisitorId", "0002");
        visitor.put("CompId","MTL");
        visitor.put("SBUId","1");
        visitor.put("LocationId","1");
        visitor.put("GateId","1");
        visitor.put("MobileNo", "7624988510");
        visitor.put("VisitorName", "Shayana A Hegde");
        visitor.put("VisitorPlace", "Manipal");
        visitor.put("VisitorCompany", "Manipal Technologies Limited");
        visitor.put("visitorDesignation", "Senior Executive-Quality Assurance & Process");
        visitor.put("VisitorTypeId","1");
        visitor.put("Purpose", "Official");
        visitor.put("VisitingStaff", "Nisha Palan");
        visitor.put("VisitingAreaId", "2");
        visitor.put("ApproverName", "Avita");
        visitor.put("RefMail","shayana.hegde@manipalgroup.info");
        visitor.put("Asset1","Laptop");
        visitor.put("Asset2","Mobile");
        visitor.put("Asset3","Book");
        visitor.put("Asset4","Pen");
        visitor.put("Asset5",NULL);
        visitor.put("SecurityName","Arun");
        visitor.put("SecurityId",100);
        visitor.put("PhotoFilePath","VisitorPhotos/");
        visitor.put("PhotoFileName","20240229_132643_970.jpg");
        visitor.put("IDProofType","1");
        visitor.put("IDProofNo","771904360738");
        visitor.put("IDProofPath","proofs/proofnumber");
        visitor.put("IDProofFileName","id1");
        visitor.put("ApprovalMailFilePath","approvalmails/mails");
        visitor.put("ApprovalMailFileName","mail1.pdf");
        visitor.put("EntryDateTime", "2024-02-29 13:26:43.927");
        visitor.put("EntryCreatedBy",3);
        visitor.put("NDAStatus", "Y");
        visitor.put("NDAUniqueId", 2);
        visitor.put("NDARemarks", NULL);
        visitor.put("NDAUpdatedBy", 3);
        visitor.put("NDAUpdatedDate", "2024-02-29 13:27:48.243");
        visitor.put("ExitDateTime", NULL);
        visitor.put("ExitCreatedBy",NULL);
        visitor.put("CreatedBy",3);
        visitor.put("CreatedDate","2024-02-29 13:26:43.927");
        visitor.put("UpdatedBy",3);
        visitor.put("UpdatedDate","2024-02-29 13:27:48.243");
        visitor.put("CameraStatus", "N");
        visitor.put("PrintStatus", "Y");
        visitor.put("ExitStatus", "N");

        db.collection("Visitor_Entry")
                .whereEqualTo("VisitorId", 0002)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "Visitor with ID already exists.");
                        } else {
                            db.collection("Visitor_Entry")
                                    .add(visitor)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "Visitor added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding visitor", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void addCompanyMaster(FirebaseFirestore db)
    {
        Map<String, Object> company = new HashMap<>();
        company.put("CompId", "MTL");
        company.put("CompName","Manipal Technologies Limited");
        company.put("Status","Active");
        company.put("CreatedBy",1);
        company.put("CreatedDate","2024-01-31 08:04:43.783");
        company.put("UpdatedBy",1);
        company.put("UpdatedDate","2024-01-31 08:04:43.783");

        db.collection("Company_Master")
                .whereEqualTo("CompId", 3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "Company with ID already exists.");
                        } else {
                            db.collection("Company_Master")
                                    .add(company)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "Company added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding company", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void getCompanyMaster(FirebaseFirestore db, DataRetrievedCallback callback)
    {
        List<DataModel> dataList = new ArrayList<>();

        ArrayList<String> comp_ids = new ArrayList<>();
        ArrayList<String> comp_name = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        db.collection("Company_Master")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=-1;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                i++;

                                Log.d("ActivityTag", document.getId() + " => " + document.getData());

                                String compIdString=(String.valueOf(document.get("CompId")));
                                if(compIdString.equals("0"))
                                    comp_ids.add("-");
                                else
                                    comp_ids.add(compIdString);
                                Log.d("ActivityTag", (String.valueOf( document.get("CompId"))));

                                String compNameString=(String.valueOf(document.get("CompName")));
                                if(compNameString.equals("0"))
                                    comp_name.add("-");
                                else
                                    comp_name.add(compNameString);
                                Log.d("ActivityTag", (String.valueOf(document.get("CompName"))));

                                String statusString=(String.valueOf(document.get("Status")));
                                if(statusString.equals("0"))
                                    status.add("-");
                                else
                                    status.add(statusString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Status"))));

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("Company ID", comp_ids.get(i));
                                data1.put("Company Name", comp_name.get(i));
                                data1.put("Status", status.get(i));
                                dataList.add(new DataModel(data1));

                            }
                        } else {
                            Log.e("ActivityTag", "Error getting documents.", task.getException());
                        }
                        callback.onRecyclerViewDataRetrieved(dataList);
                    }
                });
    }

    public static void addGateMaster(FirebaseFirestore db)
    {
        Map<String, Object> gate = new HashMap<>();
        gate.put("GateId", 3);
        gate.put("GateName","VIP Gate");
        gate.put("SBUId",2);
        gate.put("Status","Active");
        gate.put("CreatedBy",1);
        gate.put("CreatedDate","2024-03-11 15:47:11.050");
        gate.put("UpdatedBy",3);
        gate.put("UpdatedDate","2024-06-14 17:26:13.690");

        db.collection("Gate_Master")
                .whereEqualTo("GateId", 3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "Gate with ID already exists.");
                        } else {
                            db.collection("Gate_Master")
                                    .add(gate)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "Gate added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding gate", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void getGateMaster(FirebaseFirestore db, DataRetrievedCallback callback)
    {
        List<DataModel> dataList = new ArrayList<>();

        ArrayList<String> gate_ids = new ArrayList<>();
        ArrayList<String> gate_name = new ArrayList<>();
        ArrayList<String> company = new ArrayList<>();
        ArrayList<String> sbu = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        db.collection("Gate_Master")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=-1;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                i++;

                                Log.d("ActivityTag", document.getId() + " => " + document.getData());

                                String gateIdString=(String.valueOf(document.get("GateId")));
                                if(gateIdString.equals("0"))
                                    gate_ids.add("-");
                                else
                                    gate_ids.add(gateIdString);
                                Log.d("ActivityTag", (String.valueOf(document.get("GateId"))));

                                String gateNameString=(String.valueOf(document.get("GateName")));
                                if(gateNameString.equals("0"))
                                    gate_name.add("-");
                                else
                                    gate_name.add(gateNameString);
                                Log.d("ActivityTag", (String.valueOf(document.get("GateName"))));

                                String sbuIdString=(String.valueOf(document.get("SBUId")));
                                if(sbuIdString.equals("0"))
                                    sbu.add("-");
                                else
                                    sbu.add(sbuIdString);
                                Log.d("ActivityTag", (String.valueOf( document.get("SBUId"))));

                                String statusString=(String.valueOf(document.get("Status")));
                                if(statusString.equals("0"))
                                    status.add("-");
                                else
                                    status.add(statusString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Status"))));

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("Gate ID", gate_ids.get(i));
                                Log.d("Activity","Gate ids.get "+i+": "+gate_ids.get(i));
                                data1.put("Gate Name",gate_name.get(i));
                                data1.put("Company","Manipal Technologies Limited");
                                data1.put("SBU",sbu.get(i));
                                data1.put("Status", status.get(i));
                                dataList.add(new DataModel(data1));

                            }
                        } else {
                            Log.e("ActivityTag", "Error getting documents.", task.getException());
                        }
                        callback.onRecyclerViewDataRetrieved(dataList);
                    }
                });
    }

    public static void addIDProofMaster(FirebaseFirestore db)
    {
        Map<String, Object> idProof = new HashMap<>();
        idProof.put("IDProofTypeId", 6);
        idProof.put("IDProofTypeName","Employee ID");
        idProof.put("ValueType","Text");
        idProof.put("FieldName","Employee ID No.");
        idProof.put("DisplayOrder",0);
        idProof.put("Status","Active");
        idProof.put("CreatedBy",3);
        idProof.put("CreatedDate","2024-06-01 13:20:49.597");
        idProof.put("UpdatedBy",NULL);
        idProof.put("UpdatedDate",NULL);

        db.collection("IDProofType_Master")
                .whereEqualTo("IDProofTypeId", 6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "IDProofType with ID already exists.");
                        } else {
                            db.collection("IDProofType_Master")
                                    .add(idProof)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "idProof added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding idProof", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void addLocationMaster(FirebaseFirestore db)
    {
        Map<String, Object> location = new HashMap<>();
        location.put("LocationId", 1);
        location.put("LocationName","Manipal");
        location.put("Status","Active");
        location.put("CreatedBy",1);
        location.put("CreatedDate","2024-01-31 08:04:43.783");
        location.put("UpdatedBy",1);
        location.put("UpdatedDate","2024-01-31 08:04:43.783");

        db.collection("Location_Master")
                .whereEqualTo("LocationId", 3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "Location with ID already exists.");
                        } else {
                            db.collection("Location_Master")
                                    .add(location)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "location added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding location", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void getLocationMaster(FirebaseFirestore db, DataRetrievedCallback callback)
    {
        List<DataModel> dataList = new ArrayList<>();

        ArrayList<String> location_ids = new ArrayList<>();
        ArrayList<String> location_name = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        db.collection("Location_Master")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=-1;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                i++;
                                Log.d("ActivityTag", document.getId() + " => " + document.getData());

                                String locationIdString=(String.valueOf(document.get("LocationId")));
                                if(locationIdString.equals("0"))
                                    location_ids.add("-");
                                else
                                    location_ids.add(locationIdString);
                                Log.d("ActivityTag", (String.valueOf(document.get("LocationId"))));

                                String locationNameString=(String.valueOf(document.get("LocationName")));
                                if(locationNameString.equals("0"))
                                    location_name.add("-");
                                else
                                    location_name.add(locationNameString);
                                Log.d("ActivityTag", (String.valueOf(document.get("LocationName"))));

                                String statusString=(String.valueOf(document.get("Status")));
                                if(statusString.equals("0"))
                                    status.add("-");
                                else
                                    status.add(statusString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Status"))));

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("Location ID", location_ids.get(i));
                                data1.put("Location Name",location_name.get(i));
                                data1.put("Status", status.get(i));
                                dataList.add(new DataModel(data1));

                            }
                        } else {
                            Log.e("ActivityTag", "Error getting documents.", task.getException());
                        }
                        callback.onRecyclerViewDataRetrieved(dataList);
                    }
                });
    }

    public static void addNDAVariables(FirebaseFirestore db)
    {
        Map<String, Object> nda = new HashMap<>();
        nda.put("UniqueId", 2);
        nda.put("SBUId", 2);
        nda.put("UnitAddress","Manipal Technologies Limited (MTL) – Unit-5, Shivalli Industrial Area, Manipal, Karnataka State, India - 576 104");
        nda.put("UnitDescription","Manipal Technologies Limited (MTL) – Unit-5 is engaged in business of providing Commercial Products. Manipal Technologies Limited (MTL) – Unit-5 is certified for Quality Management System (QMS), Environment Management System (EMS), Information Security Management System (ISMS), Occupational Health and Safety (OH&S) Management System, Energy Management System (EnMS), Forest Stewardship Council (FSC) and SEDEX. Manipal Technologies Limited (MTL) – Unit-5 is production facility has proprietary processes and sensitive confidential data.");
        nda.put("Var1","Manipal Technologies Limited (MTL) – Unit-5.");
        nda.put("Var2","Manipal Global Print Solutions Production/Processing Area");
        nda.put("Var3","1.00");
        nda.put("Var4","MTL_MGPS_QS_T001_V1.00");

        db.collection("NDA_Variables")
                .whereEqualTo("UniqueId", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "NDA with ID already exists.");
                        } else {
                            db.collection("NDA_Variables")
                                    .add(nda)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "nda added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding nda", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void addSBUMaster(FirebaseFirestore db)
    {
        Map<String, Object> sbu = new HashMap<>();
        sbu.put("SBUId", 2);
        sbu.put("SBUName","Unit 5");
        sbu.put("CompId","MTL");
        sbu.put("LocationId",1);
        sbu.put("LabelAddress","Unit 5, Shivalli Industrial Area, Manipal - 576 104");
        sbu.put("Status","Active");
        sbu.put("CreatedBy",1);
        sbu.put("CreatedDate","2024-01-31 08:11:57.773");
        sbu.put("UpdatedBy",NULL);
        sbu.put("UpdatedDate",NULL);

        db.collection("SBU_Master")
                .whereEqualTo("SBUId", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "SBU with ID already exists.");
                        } else {
                            db.collection("SBU_Master")
                                    .add(sbu)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "sbu added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding sbu", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void getSBUMaster(FirebaseFirestore db, DataRetrievedCallback callback) {
        List<DataModel> dataList = new ArrayList<>();

        ArrayList<String> sbu_ids = new ArrayList<>();
        ArrayList<String> sbu_name = new ArrayList<>();
        ArrayList<String> company_name = new ArrayList<>();
        ArrayList<String> location = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        db.collection("SBU_Master")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String sbuIdString = (String.valueOf(document.get("SBUId")));
                            sbu_ids.add(sbuIdString.equals("0") ? "-" : sbuIdString);

                            String sbuNameString = (String.valueOf(document.get("SBUName")));
                            sbu_name.add(sbuNameString.equals("0") ? "-" : sbuNameString);

                            String locationString = (String.valueOf(document.get("LocationId")));
                            location.add(locationString.equals("0") ? "-" : locationString);

                            String statusString = (String.valueOf(document.get("Status")));
                            status.add(statusString.equals("0") ? "-" : statusString);

                            // For each document, retrieve the company name asynchronously
                            String companyIdString = (String.valueOf(document.get("CompId")));
                            Log.d("ActivityTag","companyIdString="+companyIdString);
                            getCompanyNameFromCompId(db, companyIdString, new DataRetrievedCallback() {
                                @Override
                                public void onGridViewDataRetrieved(ArrayList<Item> itemList) {

                                }

                                @Override
                                public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {
                                    // Not used here
                                }
                                @Override
                                public void onLoginCallback(boolean isSuccess,String message,String userId,String defaultGateId,String sbuId)
                                {

                                }

                                @Override
                                public void onBlacklistDeletedCallback(boolean isSuccess) {

                                }

                                @Override
                                public void onVisitorDetailsRetrieved(FirebaseFunctionCalls.Visitor visitorTypeName) {

                                }

                                @Override
                                public void onCompanyNameCallback(String companyName) {
                                    company_name.add(companyName.equals("0") ? "-" : companyName);

                                    Map<String, Object> data1 = new HashMap<>();
                                    data1.put("SBU ID", sbu_ids.remove(0));
                                    data1.put("SBU Name", sbu_name.remove(0));
                                    data1.put("Company", company_name.remove(0));
                                    data1.put("Location", location.remove(0));
                                    data1.put("Status", status.remove(0));
                                    dataList.add(new DataModel(data1));

                                    // Check if we have processed all documents
                                    if (dataList.size() == task.getResult().size()) {
                                        callback.onRecyclerViewDataRetrieved(dataList);
                                    }
                                }

                            });
                        }
                    } else {
                        Log.e("ActivityTag", "Error getting documents.", task.getException());
                    }
                });
    }


    public static void getCompanyNameFromCompId(FirebaseFirestore db, String companyId, DataRetrievedCallback callback) {
        db.collection("Company_Master").whereEqualTo("CompId", companyId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String companyName = document.getString("CompName"); // Adjust field name accordingly
                        if (companyName != null) {
                            callback.onCompanyNameCallback(companyName);
                        } else {
                            Log.d("Firestore", "No company name found");
                            callback.onCompanyNameCallback(null);
                        }
                    }
                } else {
                    Log.d("Firestore", "Task failed with ", task.getException());
                    callback.onCompanyNameCallback(null);
                }
            }
        });

    }

    public static void getGateNameFromGateId(FirebaseFirestore db, String gateId, DataRetrievedCallback callback) {
        db.collection("Gate_Master").whereEqualTo("GateId", Long.parseLong(gateId)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String gateName = document.getString("GateName"); // Adjust field name accordingly
                        if (gateName != null) {
                            callback.onCompanyNameCallback(gateName);
                        } else {
                            Log.d("Firestore", "No gate name found");
                            callback.onCompanyNameCallback(null);
                        }
                    }
                } else {
                    Log.d("Firestore", "Task failed with ", task.getException());
                    callback.onCompanyNameCallback(null);
                }
            }
        });

    }


    public static void getSbuNameFromSbuId(FirebaseFirestore db, String sbuId, DataRetrievedCallback callback) {
        Log.d("tag","here1");

        db.collection("SBU_Master").whereEqualTo("SBUId", Long.parseLong(sbuId)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("tag","here2");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String sbuName = document.getString("SBUName");
                        Log.d("tag","here3");// Adjust field name accordingly
                        if (sbuName != null) {
                            Log.d("tag","here4");
                            callback.onCompanyNameCallback(sbuName);
                        } else {
                            Log.d("Firestore", "No sbu name found");
                            callback.onCompanyNameCallback(null);
                        }
                    }
                } else {
                    Log.d("Firestore", "Task failed with ", task.getException());
                    callback.onCompanyNameCallback(null);
                }
            }
        });

    }


    public static void addUserMaster(FirebaseFirestore db)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("UserId", 8);
        user.put("UserName","testunit5");
        user.put("Password","test");
        user.put("LoginType","CUST");
        user.put("FullName","Unit 5");
        user.put("Status","Active");
        user.put("AdminFlag","N");
        user.put("SBUId",2);
        user.put("DefaultGateId",2);
        user.put("CreatedBy",1);
        user.put("CreatedDate","2024-05-13 09:01:17.450");
        user.put("UpdatedBy",NULL);
        user.put("UpdatedDate",NULL);

        db.collection("User_Master")
                .whereEqualTo("UserId", 8)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "User with ID already exists.");
                        } else {
                            db.collection("User_Master")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "user added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding user", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void addVisitingAreaMaster(FirebaseFirestore db)
    {
        String[] areanames=new String[]{"ACORO (Regulated Area)","Binding Area (Regulated Area)","Print Finish Area (Regulated Area)","Stores Area (Regulated Area)","POD Area (Regulated Area)","SRD Area (Regulated Area)","Planning (Regulated Area)","Sourcing and Procurement (Regulated Area)","QA Room (Regulated Area)","PMS Office (Regulated Area)","VIP Lobby (Controlled Area)","Employee Entrance (Controlled Area)","Car Parking (Controlled Area)","Conference Room (Controlled Area)","Web Offset Printing Area (Regulated Area)","Sheet Fed Printing Area (Regulated Area)","Nalanda Room (Controlled Area)","HR Room (Regulated Area)","ETD Area (Regulated Area)","STP Area (Restricted Area)"};
        for(int i=15;i<35;i++) {
            Map<String, Object> visitingarea = new HashMap<>();
            visitingarea.put("AreaId", i);
            visitingarea.put("AreaName", areanames[i-15]);
            visitingarea.put("SBUId", 2);
            visitingarea.put("Status", "Active");
            visitingarea.put("CreatedBy", 1);
            visitingarea.put("CreatedDate", "2024-05-13 09:16:59.403");
            visitingarea.put("UpdatedBy", 3);
            visitingarea.put("UpdatedDate", "2024-06-14 17:12:35.230");

            db.collection("VisitingArea_Master")
                    .whereEqualTo("AreaId", i)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Log.d("ActivityTag", "Area with ID already exists.");
                            } else {
                                db.collection("VisitingArea_Master")
                                        .add(visitingarea)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("ActivityTag", "visitingarea added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("ActivityTag", "Error adding visitingarea", e);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    /*public static void addVisitorBlacklist(FirebaseFirestore db)
    {
        Map<String, Object> blacklist = new HashMap<>();
        blacklist.put("CompId", "MTL");
        blacklist.put("SBUId",1);
        blacklist.put("MobileNo","94969995601");
        blacklist.put("Name","Navneeth");
        blacklist.put("Reason","Late");
        blacklist.put("CreatedBy",1);
        blacklist.put("CreatedDate","2024-01-31 08:04:43.783");

        db.collection("Visitor_Blacklist")
                .whereEqualTo("MobileNo", "94969995601")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Log.d("ActivityTag", "Blacklisted person with mobile number already exists.");
                        } else {
                            db.collection("Visitor_Blacklist")
                                    .add(blacklist)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("ActivityTag", "blacklist added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActivityTag", "Error adding blacklist", e);
                                        }
                                    });
                        }
                    }
                });
    }

    public static void getVisitorBlacklist(FirebaseFirestore db, DataRetrievedCallback callback)
    {
        List<DataModel> dataList = new ArrayList<>();

        ArrayList<String> mobile_nos = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> reasons = new ArrayList<>();
        ArrayList<String> updated_dates = new ArrayList<>();

        db.collection("Visitor_Blacklist_Log")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=-1;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                i++;

                                Log.d("ActivityTag", document.getId() + " => " + document.getData());

                                String mobileNoString=(String.valueOf(document.get("MobileNo")));
                                if(mobileNoString.equals("0"))
                                    mobile_nos.add("-");
                                else
                                    mobile_nos.add(mobileNoString);
                                Log.d("ActivityTag", (String.valueOf( document.get("MobileNo"))));

                                String nameString=(String.valueOf(document.get("Name")));
                                if(nameString.equals("0"))
                                    names.add("-");
                                else
                                    names.add(nameString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Name"))));

                                String reasonString=(String.valueOf(document.get("Reason")));
                                if(reasonString.equals("0"))
                                    reasons.add("-");
                                else
                                    reasons.add(reasonString);
                                Log.d("ActivityTag", (String.valueOf(document.get("Reason"))));

                                String updatedDateString=(String.valueOf(document.get("UpdatedDate")));
                                if(updatedDateString.equals("0"))
                                    updated_dates.add("-");
                                else
                                    updated_dates.add(updatedDateString);
                                Log.d("ActivityTag", (String.valueOf(document.get("UpdatedDate"))));

                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("Mobile No", mobile_nos.get(i));
                                data1.put("Name", names.get(i));
                                data1.put("Reason", reasons.get(i));
                                data1.put("Updated Date", updated_dates.get(i));
                                dataList.add(new DataModel(data1));

                            }
                        } else {
                            Log.e("ActivityTag", "Error getting documents.", task.getException());
                        }
                        callback.onRecyclerViewDataRetrieved(dataList);
                    }
                });
    }

    public static void addVisitorBlacklistLog(FirebaseFirestore db,String mobno, String reason,String name,String date,String userId,String sbu)
    {
        CollectionReference collectionRef = db.collection("Visitor_Blacklist_Log");

        // Get the latest log_id
        collectionRef.orderBy("LogId", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int newLogId = 1; // Default to 1 if no documents are found
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                newLogId = Objects.requireNonNull(document.getLong("LogId")).intValue() + 1;
                                break;
                            }
                            Map<String, Object> blacklistlog = new HashMap<>();
                            blacklistlog.put("LogId", newLogId);
                            blacklistlog.put("LogType","New");
                            blacklistlog.put("CompId","MTL");
                            blacklistlog.put("SBUId",sbu);
                            blacklistlog.put("MobileNo",mobno);
                            blacklistlog.put("Name",name);
                            blacklistlog.put("Reason",reason);
                            blacklistlog.put("UpdatedBy",userId);
                            blacklistlog.put("UpdatedDate",date);

                            db.collection("Visitor_Blacklist_Log")
                                    .whereEqualTo("LogId", newLogId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                Log.d("ActivityTag", "Blacklist log with ID already exists.");
                                            } else {
                                                db.collection("Visitor_Blacklist_Log")
                                                        .add(blacklistlog)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d("ActivityTag", "blacklistlog added with ID: " + documentReference.getId());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("ActivityTag", "Error adding blacklistlog", e);
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            Log.d("ActivityTag", "Error getting documents.", task.getException());}
                    }
                });
    }
    public static void removeEntryFromBlacklistLog(FirebaseFirestore db,String delete_name, DataRetrievedCallback callback)
    {
        db.collection("Visitor_Blacklist_Log")
                .whereEqualTo("Name", delete_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    document.getReference().delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> deleteTask) {
                                                    if (deleteTask.isSuccessful()) {
                                                        // Notify success for each document
                                                        callback.onBlacklistDeletedCallback(true);
                                                    } else {
                                                        // Notify failure for each document
                                                        callback.onBlacklistDeletedCallback(false);
                                                    }
                                                }
                                            });
                                }
                            } else {
                                // No documents found matching the query
                                callback.onBlacklistDeletedCallback(false);
                            }
                        } else {
                            // Task failed
                            callback.onBlacklistDeletedCallback(false);
                        }
                    }
                });
    }

    public static void addVisitorTypeMaster(FirebaseFirestore db)
    {
        String[] areanames=new String[]{"ACORO (Regulated Area)","Binding Area (Regulated Area)","Print Finish Area (Regulated Area)","Stores Area (Regulated Area)","POD Area (Regulated Area)","SRD Area (Regulated Area)","Planning (Regulated Area)","Sourcing and Procurement (Regulated Area)","QA Room (Regulated Area)","PMS Office (Regulated Area)","VIP Lobby (Controlled Area)","Employee Entrance (Controlled Area)","Car Parking (Controlled Area)","Conference Room (Controlled Area)","Web Offset Printing Area (Regulated Area)","Sheet Fed Printing Area (Regulated Area)","Nalanda Room (Controlled Area)","HR Room (Regulated Area)","ETD Area (Regulated Area)","STP Area (Restricted Area)"};
        String[] names=new String[]{"Staff","Vendor","Guest","Technician","External Auditor","Others","VIP","Customer","Supplier","Service Provider","Interviewee","Internship"};
        String[] statuses=new String[]{"Active","Inactive","Active","Inactive","Active","Inactive","Active","Active","Active","Active","Active","Active"};
        for(int i=0;i<12;i++) {
            Map<String, Object> visitortype = new HashMap<>();
            visitortype.put("VisitorTypeId", i);
            visitortype.put("VisitorTypeName", names[i]);
            visitortype.put("Status", statuses[i]);
            visitortype.put("CreatedBy", 1);
            visitortype.put("CreatedDate", "2024-01-31 08:04:43.783");
            visitortype.put("UpdatedBy", 1);
            visitortype.put("UpdatedDate", "2024-01-31 08:04:43.783");

            db.collection("VisitorType_Master")
                    .whereEqualTo("VisitorTypeId", i)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Log.d("ActivityTag", "VisitorType with ID already exists.");
                            } else {
                                db.collection("VisitorType_Master")
                                        .add(visitortype)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("ActivityTag", "visitortype added with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("ActivityTag", "Error adding visitortype", e);
                                            }
                                        });
                            }
                        }
                    });
        }
    }
    public static void searchVisitor(FirebaseFirestore db, String mobileNumber, DataRetrievedCallback callback, Context context) {
        Log.d("Search","Inside Firebase function call");
        db.collection("Visitor_Entry")
                .whereEqualTo("MobileNo", mobileNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Complete","Inside on complete method");
                        if (task.isSuccessful()) {
                            Log.d("Task", "Task successful");
                            QuerySnapshot result = task.getResult();
                            if (result != null) {
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                Log.d("Task result", " " + task.getResult());//exists
                                Log.d("Result size", " " + task.getResult().size());
                                Log.d("Get documents", " " + task.getResult().getDocuments());

                                if (!documents.isEmpty()) {

                                    DocumentSnapshot document = documents.get(0); // Assuming only one document matches the query
                                    String visitorName = document.getString("VisitorName");
                                    String place = document.getString("VisitorPlace");
                                    String company = document.getString("VisitorCompany");
                                    String designation = document.getString("visitorDesignation");
                                    String visitorTypeID = document.getString("VisitorTypeID");
                                    Log.d("id","vistortypeid="+visitorTypeID);
                                    String purpose = document.getString("Purpose");
                                    String visitingStaff= document.getString("VisitingStaff");
                                    String approverName = document.getString("ApproverName");
                                    String visitingArea = document.getString("VisitingAreaId");
                                    getVisitorTypeFromVisitorTypeId(db, visitorTypeID, new DataRetrievedCallback() {

                                        @Override
                                        public void onGridViewDataRetrieved(ArrayList<Item> itemList) {

                                        }

                                        @Override
                                        public void onRecyclerViewDataRetrieved(List<DataModel> dataList) {

                                        }

                                        @Override
                                        public void onCompanyNameCallback(String visitorTypeName) {
                                            Log.d("vis",visitorName);
                                            Visitor visitor=new Visitor(visitorName,place,company,designation,visitorTypeName,purpose,visitingStaff,approverName,visitingArea);
                                            callback.onVisitorDetailsRetrieved(visitor);

                                        }

                                        @Override
                                        public void onLoginCallback(boolean isSuccess, String message, String userId, String defaultGateId, String sbuId) {

                                        }

                                        @Override
                                        public void onBlacklistDeletedCallback(boolean isSuccess) {

                                        }

                                        @Override
                                        public void onVisitorDetailsRetrieved(FirebaseFunctionCalls.Visitor visitor) {

                                        }
                                    });

                                } else {
                                    Log.d("No Documents", "Query did not return any documents");
                                    Toast.makeText(context, "Visitor not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Result is null", "Query result is null");
                            }
                        }
                        else {
                            Log.e("Task", "Task failed", task.getException());
                        }
                    }});
    }

    public static void getVisitorTypeFromVisitorTypeId(FirebaseFirestore db, String visitorTypeId,DataRetrievedCallback callback)
    {
        db.collection("VisitorType_Master").whereEqualTo("VisitorTypeId", Long.parseLong(visitorTypeId)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String visitorTypeName = document.getString("VisitorTypeName"); // Adjust field name accordingly
                        if (visitorTypeName != null) {
                            Log.d("Firestore", "visitorTypeName found");
                            callback.onCompanyNameCallback(visitorTypeName);
                        } else {
                            Log.d("Firestore", "No visitorTypeName found");
                            callback.onCompanyNameCallback(null);
                        }
                    }
                } else {
                    Log.d("Firestore", "Task failed with ", task.getException());
                    callback.onCompanyNameCallback(null);
                }
            }
        });
    }

    public static Task<ArrayList<String>> fetchVisitorTypeNames(FirebaseFirestore db) {
        TaskCompletionSource<ArrayList<String>> taskCompletionSourceVisitorType = new TaskCompletionSource<>();

        db.collection("VisitorType_Master")
                .whereEqualTo("Status", "Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> visitorTypeNames = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String visitorTypeName = document.getString("VisitorTypeName");
                                if (visitorTypeName != null) {
                                    visitorTypeNames.add(visitorTypeName);
                                }
                            }
                            taskCompletionSourceVisitorType.setResult(visitorTypeNames);
                            Log.d("Visitor type names",""+visitorTypeNames);
                        } else {
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                            taskCompletionSourceVisitorType.setException(task.getException());
                        }
                    }
                });

        return taskCompletionSourceVisitorType.getTask();
    }

    public static Task<ArrayList<String>> fetchVisitingAreas(FirebaseFirestore db) {
        TaskCompletionSource<ArrayList<String>> taskCompletionSourceArea = new TaskCompletionSource<>();

        db.collection("VisitingArea_Master")
                .whereEqualTo("Status", "Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> visitingAreas = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String visitingArea = document.getString("AreaName");
                                if (visitingArea != null) {
                                    visitingAreas.add(visitingArea);
                                }
                            }
                            taskCompletionSourceArea.setResult(visitingAreas);
                        } else {
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                            taskCompletionSourceArea.setException(task.getException());
                        }
                    }
                });

        return taskCompletionSourceArea.getTask();
    }

    public static Task<ArrayList<String>> fetchIDProofNames(FirebaseFirestore db) {
        TaskCompletionSource<ArrayList<String>> taskCompletionSourceID = new TaskCompletionSource<>();

        db.collection("IDProofType_Master")
                .whereEqualTo("Status", "Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> IDProofs = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String IDProof = document.getString("IDProofTypeName");
                                if (IDProof != null) {
                                    IDProofs.add(IDProof);
                                }
                            }
                            taskCompletionSourceID.setResult(IDProofs);
                        } else {
                            Log.e("Firestore Error", "Error getting documents: ", task.getException());
                            taskCompletionSourceID.setException(task.getException());
                        }
                    }
                });

        return taskCompletionSourceID.getTask();
    }





} */
