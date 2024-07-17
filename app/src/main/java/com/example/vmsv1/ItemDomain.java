package com.example.vmsv1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDomain {

    String visitorId,mob_no,visitor_name,visitor_designation,visitor_place,visitor_company,visiting_staff,approver_name,purpose,visiting_area,gate_id,image;

    String entry_time,entry_date,exit_time;


    boolean camera_status,nda_status,print_status,exit_status;

    public ItemDomain(String visitorId, String mob_no, String visitor_name, String visitor_designation,String visitor_place,String visitor_company,String visiting_staff,String approver_name,String purpose,String visiting_area,String entry_time,String entry_date,String exit_time,boolean camera_status,boolean nda_status,boolean print_status,boolean exit_status,String gate_id,String image)
    {
        this.visitorId =visitorId;
        this.mob_no=mob_no;
        this.visitor_name=visitor_name;
        this.visitor_designation=visitor_designation;
        this.visitor_place=visitor_place;
        this.visitor_company=visitor_company;
        this.visiting_staff=visiting_staff;
        this.approver_name=approver_name;
        this.purpose=purpose;
        this.visiting_area=visiting_area;
        this.entry_time=entry_time;
        this.entry_date = entry_date;
        this.exit_time=exit_time;

        this.camera_status=camera_status;
        this.nda_status=nda_status;
        this.print_status=print_status;
        this.exit_status=exit_status;
        this.gate_id= gate_id;
        this.image = image;
    }
    public ItemDomain() {
        // Default constructor
    }


    //getters
    public String getVisitorId() {
        return visitorId;
    }
    public String getMobNum() { return mob_no; }
    public String getVisitor_name() { return visitor_name; }
    public String getVisitor_designation() { return visitor_designation; }
    public String getVisitor_place() { return visitor_place; }
    public String getVisitor_company() { return visitor_company; }
    public String getVisiting_staff() { return visiting_staff; }
    public String getApprover_name() { return approver_name; }
    public String getPurpose() { return purpose; }
    public String getVisiting_area() { return visiting_area; }

    public String getEntry_time() { return entry_time; }
    public String getExit_time() { return exit_time; }

    public boolean getCamera_status() { return camera_status; }
    public boolean getNDA_status() { return nda_status; }
    public boolean getPrint_status() { return print_status; }
    public boolean getExit_status() { return exit_status; }
    public String getGate_id() {return gate_id;}
    public String getImage() {return image;}

    //setters
    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }
    public void setMobNum(String mob_no) {
        this.mob_no = mob_no;
    }
    public void setVisitorName(String visitor_name) {
        this.visitor_name = visitor_name;
    }
    public void setVisitorDesignation(String visitor_designation) {
        this.visitor_designation = visitor_designation;
    }
    public void setVisitorPlace(String visitor_place) {
        this.visitor_place = visitor_place;
    }
    public void setVisitorCompany(String visitor_company) {
        this.visitor_company = visitor_company;
    }
    public void setVisitingStaff(String visiting_staff) {
        this.visiting_staff = visiting_staff;
    }
    public void setApproverName(String approver_name) {
        this.approver_name = approver_name;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public void setVisitingArea(String visiting_area) {
        this.visiting_area = visiting_area;
    }
    public void setEntryTime(String entry_time) {
        this.entry_time = entry_time;
    }
    public void setEntryDate(String entry_date) {
        this.entry_date = entry_date;
    }
    public void setExitTime(String exit_time) {
        this.exit_time = exit_time;
    }
    public void setCameraStatus(boolean camera_status) {
        this.camera_status = camera_status;
    }
    public void setNdaStatus(boolean nda_status) {
        this.nda_status = nda_status;
    }
    public void setPrintStatus(boolean print_status) {
        this.print_status = print_status;
    }
    public void setExitStatus(boolean exit_status) {
        this.exit_status = exit_status;
    }
    public void setGateID(String gate_id)
    {
        this.gate_id= gate_id;
    }
    public void setImage(String image)
    {
        this.image = image;
    }
}
