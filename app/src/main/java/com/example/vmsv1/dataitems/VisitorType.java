package com.example.vmsv1.dataitems;


public class VisitorType {
    private int visitorTypeId;
    private String visitorTypeName;
    private String visitorTypeNameDisp;
    private String status;
    private int createdBy;
    private String createdDate;
    private int updatedBy;
    private String updatedDate;

    public int getVisitorTypeId() {
        return visitorTypeId;
    }

    public void setVisitorTypeId(int visitorTypeId) {
        this.visitorTypeId = visitorTypeId;
    }

    public String getVisitorTypeName() {
        return visitorTypeName;
    }

    public void setVisitorTypeName(String visitorTypeName) {
        this.visitorTypeName = visitorTypeName;
    }

    public String getVisitorTypeNameDisp() {
        return visitorTypeNameDisp;
    }

    public void setVisitorTypeNameDisp(String visitorTypeNameDisp) {
        this.visitorTypeNameDisp = visitorTypeNameDisp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}