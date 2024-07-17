package com.example.vmsv1;

import java.sql.Timestamp;

public class Visitor {

    private int uniqueId;
    private String visitorId;
    private int compId;
    private String compName;
    private int sbuId;
    private String sbuName;
    private String labelAddress;
    private int locationId;
    private String locationName;
    private int gateId;
    private String gateName;
    private String mobileNo;
    private String visitorName;
    private String visitorPlace;
    private String visitorCompany;
    private String visitorDesignation;
    private int visitorTypeId;
    private String visitorTypeName;
    private String purpose;
    private String visitingFaculty;
    private int visitingAreaId;
    private String visitingAreaName;
    private String approverName;
    private String refMail;
    private String asset1;
    private String asset2;
    private String asset3;
    private String asset4;
    private String asset5;
    private String securityName;
    private String securityId;
    private String photoFilePath;
    private String photoFileName;
    private int idProofType;
    private String idProofTypeName;
    private String idProofValueType;
    private String idProofNo;
    private String idProofFilePath;
    private String idProofFileName;
    private String approvalMailFilePath;
    private String approvalMailFileName;
    private Timestamp entryDatetime;
    private String entryCreatedBy;
    private Timestamp exitDatetime;
    private String exitCreatedBy;
    private String createdBy;
    private Timestamp createdDate;
    private String updatedBy;
    private Timestamp updatedDate;
    private String blackListNumber;
    private String blackListReason;

    public Visitor(int uniqueId, String visitorId, int compId, String compName, int sbuId, String sbuName,
                   String labelAddress, int locationId, String locationName, int gateId, String gateName,
                   String mobileNo, String visitorName, String visitorPlace,
                   String visitorDesignation, int visitorTypeId, String visitorTypeName, String purpose,
                   String visitingFaculty, int visitingAreaId, String visitingAreaName, String approverName,
                   String refMail, String asset1, String asset2, String asset3, String asset4, String asset5,
                   String securityName, String securityId, String photoFilePath, String photoFileName,
                   int idProofType, String idProofTypeName, String idProofValueType, String idProofNo,
                   String idProofFilePath, String idProofFileName, String approvalMailFilePath,
                   String approvalMailFileName, Timestamp entryDatetime, String entryCreatedBy,
                   Timestamp exitDatetime, String exitCreatedBy, String createdBy, Timestamp createdDate,
                   String updatedBy, Timestamp updatedDate, String blackListNumber, String blackListReason) {
        this.uniqueId = uniqueId;
        this.visitorId = visitorId;
        this.compId = compId;
        this.compName = compName;
        this.sbuId = sbuId;
        this.sbuName = sbuName;
        this.labelAddress = labelAddress;
        this.locationId = locationId;
        this.locationName = locationName;
        this.gateId = gateId;
        this.gateName = gateName;
        this.mobileNo = mobileNo;
        this.visitorName = visitorName;
        this.visitorPlace = visitorPlace;
        this.visitorDesignation = visitorDesignation;
        this.visitorTypeId = visitorTypeId;
        this.visitorTypeName = visitorTypeName;
        this.purpose = purpose;
        this.visitingFaculty = visitingFaculty;
        this.visitingAreaId = visitingAreaId;
        this.visitingAreaName = visitingAreaName;
        this.approverName = approverName;
        this.refMail = refMail;
        this.asset1 = asset1;
        this.asset2 = asset2;
        this.asset3 = asset3;
        this.asset4 = asset4;
        this.asset5 = asset5;
        this.securityName = securityName;
        this.securityId = securityId;
        this.photoFilePath = photoFilePath;
        this.photoFileName = photoFileName;
        this.idProofType = idProofType;
        this.idProofTypeName = idProofTypeName;
        this.idProofValueType = idProofValueType;
        this.idProofNo = idProofNo;
        this.idProofFilePath = idProofFilePath;
        this.idProofFileName = idProofFileName;
        this.approvalMailFilePath = approvalMailFilePath;
        this.approvalMailFileName = approvalMailFileName;
        this.entryDatetime = entryDatetime;
        this.entryCreatedBy = entryCreatedBy;
        this.exitDatetime = exitDatetime;
        this.exitCreatedBy = exitCreatedBy;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.blackListNumber = blackListNumber;
        this.blackListReason = blackListReason;
    }
    public int getUniqueId() { return uniqueId; }
    public String getVisitorId() { return visitorId; }
    public int getCompId() { return compId; }
    public String getCompName() { return compName; }
    public int getSbuId() { return sbuId; }
    public String getSbuName() { return sbuName; }
    public String getLabelAddress() { return labelAddress; }
    public int getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }
    public int getGateId() { return gateId; }
    public String getGateName() { return gateName; }
    public String getMobileNo() { return mobileNo; }
    public String getVisitorName() { return visitorName; }
    public String getVisitorPlace() { return visitorPlace; }
    public String getVisitorDesignation() { return visitorDesignation; }
    public int getVisitorTypeId() { return visitorTypeId; }
    public String getVisitorTypeName() { return visitorTypeName; }
    public String getPurpose() { return purpose; }
    public String getVisitingFaculty() { return visitingFaculty; }
    public int getVisitingAreaId() { return visitingAreaId; }
    public String getVisitingAreaName() { return visitingAreaName; }
    public String getApproverName() { return approverName; }
    public String getRefMail() { return refMail; }
    public String getAsset1() { return asset1; }
    public String getAsset2() { return asset2; }
    public String getAsset3() { return asset3; }
    public String getAsset4() { return asset4; }
    public String getAsset5() { return asset5; }
    public String getSecurityName() { return securityName; }
    public String getSecurityId() { return securityId; }
    public String getPhotoFilePath() { return photoFilePath; }
    public String getPhotoFileName() { return photoFileName; }
    public int getIdProofType() { return idProofType; }
    public String getIdProofTypeName() { return idProofTypeName; }
    public String getIdProofValueType() { return idProofValueType; }
    public String getIdProofNo() { return idProofNo; }
    public String getIdProofFilePath() { return idProofFilePath; }
    public String getIdProofFileName() { return idProofFileName; }
    public String getApprovalMailFilePath() { return approvalMailFilePath; }
    public String getApprovalMailFileName() { return approvalMailFileName; }
    public Timestamp getEntryDatetime() { return entryDatetime; }
    public String getEntryCreatedBy() { return entryCreatedBy; }
    public Timestamp getExitDatetime() { return exitDatetime; }
    public String getExitCreatedBy() { return exitCreatedBy; }
    public String getCreatedBy() { return createdBy; }
    public Timestamp getCreatedDate() { return createdDate; }
    public String getUpdatedBy() { return updatedBy; }
    public Timestamp getUpdatedDate() { return updatedDate; }
    public String getBlackListNumber() { return blackListNumber; }
    public String getBlackListReason() { return blackListReason; }
}