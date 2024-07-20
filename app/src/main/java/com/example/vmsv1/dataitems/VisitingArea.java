package com.example.vmsv1.dataitems;

import android.util.Log;

public class VisitingArea {
    private String compId;
    private String compName;
    private int sbuId;
    private String sbuName;
    private int areaId;
    private String areaName;
    private String status;
    private int createdBy;
    private String createdDate;
    private int updatedBy;
    private String updatedDate;

    // Getters and Setters
    public String getCompId() { return compId; }
    public void setCompId(String compId) { this.compId = compId; }

    public String getCompName() { return compName; }
    public void setCompName(String compName) { this.compName = compName; }

    public int getSbuId() { return sbuId; }
    public void setSbuId(int sbuId) { this.sbuId = sbuId; }

    public String getSbuName() { return sbuName; }
    public void setSbuName(String sbuName) { this.sbuName = sbuName; }

    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }

    public String getVisitingAreaName()  {
        return areaName;}
}

