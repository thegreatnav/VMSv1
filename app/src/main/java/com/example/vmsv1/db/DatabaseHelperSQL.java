package com.example.vmsv1.db;

import com.example.vmsv1.dataitems.*;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.vmsv1.ItemDomain;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperSQL {

    static Connection conn;
//    final static String connectionUrl = "jdbc:jtds:sqlserver://SQL6032.site4now.net:1433/db_aaabe8_vms;user=db_aaabe8_vms_admin;password=password123;";

    public static Connection getConnection(){

        String connectionUrl = new DefaultValues().getConnectionUrl();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            conn = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception e) { System.out.println(e); }
        return conn;
    }

    public List<String> loginValidation(String username, String password){
        List<String> values = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call SP_verifyLogin(?, ?)}";
        try{
            CallableStatement SP = conn.prepareCall(SP_String);
            SP.setString(1,username);
            SP.setString(2, password);
            ResultSet result = SP.executeQuery();
            if (result.next()) {
                values.add(String.valueOf(result.getInt("ID")));
                values.add(result.getString("UserType"));
                values.add(result.getString("LoginType"));
                values.add(result.getString("Status"));
                values.add(result.getString("FullName"));
                values.add(result.getString("SBUId"));
                values.add(result.getString("DefaultGateId"));
            }
        }
        catch(Exception e){}
        return values;
    }

    public List<List<String>> getGateListSpinner(String companyID, String sbuID){

        List<List<String>> values = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getGateList(?, ?, ?, ?)}";
        try{
            CallableStatement SP = conn.prepareCall(SP_String);

            SP.setString(1,companyID);
            SP.setString(2,sbuID);
            SP.setNull(3, Types.VARCHAR);
            SP.setString(4,"Active");

            ResultSet result = SP.executeQuery();
            while (result.next()) {
                System.out.println("D");
                List<String> temp = new ArrayList<>();
                temp.add(result.getString("SBUName"));
                temp.add(result.getString("GateId"));
                temp.add(result.getString("GateName"));
                values.add(temp);
            }
        }
        catch (Exception e){System.out.println(e.toString());}
        return values;
    }


    public List<String> addNewVisitorExit(int uniqueId, String securityName, String securityId, int userId)
    {
        Connection conn = null;
        CallableStatement sp = null;
        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("VisitorId: Not Available");
        result.add("VisitorName: Not Available");
        result.add("ApproverName: Not Available");
        result.add("VisitorDesignation: Not Available");
        result.add("VisitorCompany: Not Available");
        result.add("VisitingAreaName: Not Available");
        result.add("Assets: Not Available");
        result.add("SecurityName: Not Available");
        result.add("SecurityId: Not Available");
        result.add("EntryDateTime: Not Available");
        result.add("RefMail: Not Available");

        try {
            conn = getConnection();
            String spString = "{call dbo.SP_addNewVisitorExit(?, ?, ?, ?)}";
            sp = conn.prepareCall(spString);

            sp.setInt(1, uniqueId);
            sp.setString(2, securityName);
            sp.setString(3, securityId);
            sp.setInt(4, userId);

            // Execute the stored procedure
            ResultSet rs = sp.executeQuery();

            if (rs.next()) {
                // Retrieve the output parameters from the ResultSet
                int id = rs.getInt("ID");
                String visitorId = rs.getString("VisitorId");
                String visitorName = rs.getString("VisitorName");
                String approverName = rs.getString("ApproverName");
                String visitorDesignation = rs.getString("VisitorDesignation");
                String visitorCompany = rs.getString("VisitorCompany");
                String visitingAreaName = rs.getString("VisitingAreaName");
                String assets = rs.getString("Assets");
                String securityNameResult = rs.getString("SecurityName");
                String securityIdResult = rs.getString("SecurityId");
                String entryDateTime = rs.getString("EntryDateTime");
                String refMail = rs.getString("RefMail");

                // Update the result list
                result.set(0, "ID: " + id);
                result.set(1, "VisitorId: " + visitorId);
                result.set(2, "VisitorName: " + visitorName);
                result.set(3, "ApproverName: " + approverName);
                result.set(4, "VisitorDesignation: " + visitorDesignation);
                result.set(5, "VisitorCompany: " + visitorCompany);
                result.set(6, "VisitingAreaName: " + visitingAreaName);
                result.set(7, "Assets: " + assets);
                result.set(8, "SecurityName: " + securityNameResult);
                result.set(9, "SecurityId: " + securityIdResult);
                result.set(10, "EntryDateTime: " + (entryDateTime != null ? entryDateTime.toString() : "Not Available"));
                result.set(11, "RefMail: " + refMail);
            }

        } catch (SQLException e) {
            System.out.println("Error in addNewVisitorExit: " + e.getMessage());
            result.set(0, "ID: 0");
            result.set(1, "VisitorId: Error occurred while adding visitor exit.");
        } finally {
            // Clean up resources
            try {
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewVisitorType(String visitorTypeName, String status, int userId) {

        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewVisitorType(?, ?, ?)}";

        List<String> result = new ArrayList<>();

        result.add("ID: Not Available");
        result.add("SaveStatus: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);

            sp.setString(1, visitorTypeName);
            sp.setString(2, status);
            sp.setInt(3, userId);

            rs = sp.executeQuery();

            // Process the result set
            if (rs.next()) {
                long id = rs.getLong("ID");
                String message = rs.getString("ErrorMessage");
                String savestatus=rs.getString("SaveStatus");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);
                result.set(2,"SaveStatus: "+savestatus);
            }

        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewBlackListVisitor: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }


    public List<String> addNewVisitingArea(int sbuId, String areaName, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewVisitingArea(?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("SaveStatus: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);
            sp.setInt(1, sbuId);
            sp.setString(2, areaName);
            sp.setString(3, status);
            sp.setInt(4, userId);

            // Execute the stored procedure
            rs = sp.executeQuery();

            // Retrieve results from the ResultSet
            if (rs.next()) {
                int id = rs.getInt("ID");
                String saveStatus = rs.getString("SaveStatus");
                String errorMessage = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, "ID: " + id);
                result.set(1, "SaveStatus: " + saveStatus);
                result.set(2, "ErrorMessage: " + errorMessage);
            }

        } catch (SQLException e) {
            System.out.println("Error in addNewVisitingArea: " + e.getMessage());
            result.set(0, "ID: Not Available");
            result.set(1, "SaveStatus: Error");
            result.set(2, "ErrorMessage: Error occurred while adding visiting area.");
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewUser(String loginType, String userName, String password, String fullName, int sbuId, int defaultGateId, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewUser(?, ?, ?, ?, ?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("SaveStatus: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);
            sp.setString(1, loginType);
            sp.setString(2, userName);
            sp.setString(3, password);
            sp.setString(4, fullName);
            sp.setInt(5, sbuId);
            sp.setInt(6, defaultGateId);
            sp.setString(7, status);
            sp.setInt(8, userId);

            rs = sp.executeQuery();

            // Process the result set
            if (rs.next()) {
                long id = rs.getLong("ID");
                String message = rs.getString("ErrorMessage");
                String savestatus=rs.getString("SaveStatus");

                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);
                result.set(2," SaveStatus: "+savestatus);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewUser: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }


    public List<String> addNewSBU(String compId, String sbuName, int locationId, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewSBU(?, ?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("SaveStatus: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try{
            sp=conn.prepareCall(spString);
            sp.setString(1, compId);
            sp.setString(2, sbuName);
            sp.setInt(3, locationId);
            sp.setString(4, status);
            sp.setInt(5, userId);

            rs = sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("Id");
                String savestatus=rs.getString("SaveStatus");
                String message = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, "ID: " + id);
                result.set(1, "Message: " + message);
                result.set(2, "SaveStatus:"+savestatus);
            }

        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewSBU: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewLocation(String locationName, String status, int userId)
    {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewLocation(?, ?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("Id: 0");  // Default values in case of failure
        result.add("Message: Error occurred while adding location.");

        CallableStatement sp = null;
        ResultSet rs=null;

        try {
            sp = conn.prepareCall(spString);
            sp.setString(1, locationName);
            sp.setString(2, status);
            sp.setInt(3, userId);

            sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                String message = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);
            }

        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewLocation: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }
        return result;
    }


    public List<String> addNewIdProofType(String idProofTypeName, String valueType, String fieldName, int displayOrder, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewIdProofType(?, ?, ?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try{
            sp=conn.prepareCall(spString);
            sp.setString(1, idProofTypeName);
            sp.setString(2, valueType);
            sp.setString(3, fieldName);
            sp.setInt(4, displayOrder);
            sp.setString(5, status);
            sp.setInt(6, userId);

            rs=sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                long count=rs.getLong("Count");
                String savestatus=rs.getString("SaveStatus");
                String message = rs.getString("Message");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);
                result.set(2, "Count: " + count);
                result.set(3, "SaveStatus: " + savestatus);

            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewIdProofType: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewGate(int sbuId, String gateName, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewGate(?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("ID: Not Available");
        result.add("SaveStatus: Not Available");
        result.add("ErrorMessage: Not Available");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);
            sp.setInt(1, sbuId);
            sp.setString(2, gateName);
            sp.setString(3, status);
            sp.setInt(4, userId);

            rs = sp.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID");
                String saveStatus = rs.getString("SaveStatus");
                String errorMessage = rs.getString("ErrorMessage");

                result.set(0, "ID: " + id);
                result.set(1, "SaveStatus: " + saveStatus);
                result.set(2, "ErrorMessage: " + errorMessage);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewGate: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewCompany(String compId, String companyName, String status, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewCompany(?, ?, ?, ?)}";

        List<String> result = new ArrayList<>();

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);
            sp.setString(1, compId);
            sp.setString(2, companyName);
            sp.setString(3, status);
            sp.setInt(4, userId);

            rs = sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("Id");
                String message = rs.getString("Message");
                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);

            }
        }catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewCompany " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewBlackListVisitor(String mobileNo, String name, String reason, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_addNewBlackListVisitor(?, ?, ?, ?)}";
        Log.d("Tag1", "mobileno=" + mobileNo);
        Log.d("Tag1", "name=" + name);
        Log.d("Tag1", "reason=" + reason);
        Log.d("Tag1", "userid=" + userId);

        List<String> result = new ArrayList<>();
        result.add("Id: 0");  // Default values in case of failure
        result.add("Message: Error occurred while adding visitor to blacklist.");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp = conn.prepareCall(spString);
            // Set input parameters
            sp.setString(1, mobileNo);
            sp.setString(2, name);
            sp.setString(3, reason);
            sp.setInt(4, userId);

            // Execute the stored procedure
            rs = sp.executeQuery();

            // Process the result set
            if (rs.next()) {
                long id = rs.getLong("Id");
                String message = rs.getString("Message");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Message: " + message);
            }

        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewBlackListVisitor: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }



    public List<BlackListVisitor> getBlackListVisitorList(String mobileNo, String name, int userId) {
        List<BlackListVisitor> blackListVisitors = new ArrayList<>();
        Connection conn = getConnection();
        String spString = "{call dbo.SP_getBlackListVisitorList(?, ?, ?)}";

        try {
            CallableStatement sp = conn.prepareCall(spString);
            sp.setString(1, mobileNo);
            sp.setString(2, name);
            sp.setInt(3, userId);

            ResultSet rs = sp.executeQuery();

            while (rs.next()) {
                BlackListVisitor visitor = new BlackListVisitor();
                visitor.setCompId(rs.getString("CompId"));
                visitor.setSbuId(rs.getInt("SBUId"));
                visitor.setMobileNo(rs.getString("MobileNo"));
                visitor.setName(rs.getString("Name"));
                visitor.setReason(rs.getString("Reason"));
                visitor.setCreatedBy(rs.getInt("CreatedBy"));
                visitor.setCreatedDate(rs.getString("CreatedDate"));

                blackListVisitors.add(visitor);
            }
        } catch (Exception e) {
            System.out.println("Error in getBlackListVisitorList: " + e.getMessage());
        }

        return blackListVisitors;
    }

    public int deleteVisitorFromBlackList(String mobileNo, int userId) {
        Connection conn = getConnection();
        String spString = "{call dbo.SP_deleteVisitorFromBlackList(?, ?)}";

        try {
            CallableStatement sp = conn.prepareCall(spString);
            sp.setString(1, mobileNo);
            sp.setInt(2, userId);

            ResultSet rs = sp.executeQuery();
            if (rs.next()) {
                return rs.getInt("Count");
            }
        } catch (Exception e) {
            System.out.println("Error in deleteVisitorFromBlackList: " + e.getMessage());
        }

        return 0;
    }

    public BlackListVisitor checkBlackListVisitor(String mobileNo, int userId) {
        BlackListVisitor visitor = null;
        Connection conn = getConnection();
        String spString = "{call dbo.SP_checkBlackListVisitor(?, ?)}";

        try {
            CallableStatement sp = conn.prepareCall(spString);
            sp.setString(1, mobileNo);
            sp.setInt(2, userId);

            ResultSet rs = sp.executeQuery();

            if (rs.next()) {
                visitor = new BlackListVisitor();
                visitor.setCompId(rs.getString("CompId"));
                visitor.setSbuId(rs.getInt("SBUId"));
                visitor.setMobileNo(rs.getString("MobileNo"));
                visitor.setName(rs.getString("Name"));
                visitor.setReason(rs.getString("Reason"));
                visitor.setCreatedBy(rs.getInt("CreatedBy"));
                visitor.setCreatedDate(rs.getString("CreatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in checkBlackListVisitor: " + e.getMessage());
        }

        return visitor;
    }

    public Company getCompanyDetails(String compId) {
        Company company = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getCompanyDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setString(1, compId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                company = new Company();
                company.setCompId(rs.getString("CompId"));
                company.setCompName(rs.getString("CompName"));
                company.setStatus(rs.getString("Status"));
                company.setCreatedBy(rs.getInt("CreatedBy"));
                company.setCreatedDate(rs.getString("CreatedDate"));
                company.setUpdatedBy(rs.getInt("UpdatedBy"));
                company.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getCompanyDetails: " + e);
        }
        return company;
    }

    public List<Company> getCompanyList(String companyName, String status) {
        List<Company> companies = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getCompanyList(?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, companyName);
            SP.setString(2, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                Company company = new Company();
                company.setCompId(rs.getString("CompId"));
                company.setCompName(rs.getString("CompName"));
                company.setStatus(rs.getString("Status"));
                company.setCreatedBy(rs.getInt("CreatedBy"));
                company.setCreatedDate(rs.getString("CreatedDate"));
                company.setUpdatedBy(rs.getInt("UpdatedBy"));
                company.setUpdatedDate(rs.getString("UpdatedDate"));
                companies.add(company);
            }
        } catch (Exception e) {
            System.out.println("Error in getCompanyList: " + e);
        }
        return companies;
    }

    public Gate getGateDetails(int gateId) {
        Gate gate = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getGateDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, gateId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                gate = new Gate();
                gate.setCompId(rs.getString("CompId"));
                gate.setCompName(rs.getString("CompName"));
                gate.setSbuId(rs.getInt("SBUId"));
                gate.setSbuName(rs.getString("SBUName"));
                gate.setGateId(rs.getInt("GateId"));
                gate.setGateName(rs.getString("GateName"));
                gate.setStatus(rs.getString("Status"));
                gate.setCreatedBy(rs.getInt("CreatedBy"));
                gate.setCreatedDate(rs.getString("CreatedDate"));
                gate.setUpdatedBy(rs.getInt("UpdatedBy"));
                gate.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getGateDetails: " + e);
        }
        return gate;
    }

    public List<Gate> getGateList(String compId, int sbuId, String gateName, String status) {
        List<Gate> gates = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getGateList(?, ?, ?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, compId);
            SP.setInt(2, sbuId);
            SP.setString(3, gateName);
            SP.setString(4, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                Gate gate = new Gate();
                gate.setCompId(rs.getString("CompId"));
                gate.setCompName(rs.getString("CompName"));
                gate.setSbuId(rs.getInt("SBUId"));
                gate.setSbuName(rs.getString("SBUName"));
                gate.setGateId(rs.getInt("GateId"));
                gate.setGateName(rs.getString("GateName"));
                gate.setStatus(rs.getString("Status"));
                gate.setCreatedBy(rs.getInt("CreatedBy"));
                gate.setCreatedDate(rs.getString("CreatedDate"));
                gate.setUpdatedBy(rs.getInt("UpdatedBy"));
                gate.setUpdatedDate(rs.getString("UpdatedDate"));
                gates.add(gate);
            }
        } catch (Exception e) {
            System.out.println("Error in getGateList: " + e);
        }
        return gates;
    }

    public List<NDADetails> getNDADetails(int sbuId) {
        List<NDADetails> ndaDetailsList = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getSBUDetailedInfo(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setInt(1, sbuId);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                NDADetails ndaDetail = new NDADetails();
                ndaDetail.setSbuId(rs.getInt("sbuid"));
                ndaDetail.setUnitAddress(rs.getString("unitaddress"));
                ndaDetail.setUnitDescription(rs.getString("unitdescription"));
                ndaDetail.setVar1(rs.getString("var1"));
                ndaDetail.setVar2(rs.getString("var2"));
                ndaDetail.setVar3(rs.getString("var3"));
                ndaDetail.setVar4(rs.getString("var4"));
                ndaDetailsList.add(ndaDetail);
            }
        } catch (Exception e) {
            System.out.println("Error in getNDADetails: " + e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e);
            }
        }
        return ndaDetailsList;
    }

    public IDProof getIdProofTypeDetails(int idProofTypeId) {
        IDProof idProofType = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getIdProofTypeDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, idProofTypeId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                idProofType = new IDProof();
                idProofType.setIdProofTypeId(rs.getInt("IDProofTypeId"));
                idProofType.setIdProofTypeName(rs.getString("IDProofTypeName"));
                idProofType.setIdProofTypeNameDisp(rs.getString("IDProofTypeNameDisp"));
                idProofType.setValueType(rs.getString("ValueType"));
                idProofType.setFieldName(rs.getString("FieldName"));
                idProofType.setDisplayOrder(rs.getInt("DisplayOrder"));
                idProofType.setStatus(rs.getString("Status"));
                idProofType.setCreatedBy(rs.getInt("CreatedBy"));
                idProofType.setCreatedDate(rs.getString("CreatedDate"));
                idProofType.setUpdatedBy(rs.getInt("UpdatedBy"));
                idProofType.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getIdProofTypeDetails: " + e);
        }
        return idProofType;
    }

    public List<IDProof> getIdProofTypeList(String idProofTypeName, String valueType, String status) {
        List<IDProof> idProofTypes = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getIdProofTypeList(?, ?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, idProofTypeName);
            SP.setString(2, valueType);
            SP.setString(3, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                IDProof idProofType = new IDProof();
                idProofType.setIdProofTypeId(rs.getInt("IDProofTypeId"));
                idProofType.setIdProofTypeName(rs.getString("IDProofTypeName"));
                idProofType.setIdProofTypeNameDisp(rs.getString("IDProofTypeNameDisp"));
                idProofType.setValueType(rs.getString("ValueType"));
                idProofType.setFieldName(rs.getString("FieldName"));
                idProofType.setDisplayOrder(rs.getInt("DisplayOrder"));
                idProofType.setStatus(rs.getString("Status"));
                idProofType.setCreatedBy(rs.getInt("CreatedBy"));
                idProofType.setCreatedDate(rs.getString("CreatedDate"));
                idProofType.setUpdatedBy(rs.getInt("UpdatedBy"));
                idProofType.setUpdatedDate(rs.getString("UpdatedDate"));
                idProofTypes.add(idProofType);
            }
        } catch (Exception e) {
            System.out.println("Error in getIdProofTypeList: " + e);
        }
        return idProofTypes;
    }

    public Location getLocationDetails(int locationId) {
        Location location = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getLocationDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, locationId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                location = new Location();
                location.setLocationId(rs.getInt("LocationId"));
                location.setLocationName(rs.getString("LocationName"));
                location.setStatus(rs.getString("Status"));
                location.setCreatedBy(rs.getInt("CreatedBy"));
                location.setCreatedDate(rs.getString("CreatedDate"));
                location.setUpdatedBy(rs.getInt("UpdatedBy"));
                location.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getLocationDetails: " + e);
        }
        return location;
    }

    public List<Location> getLocationList(String locationName, String status) {
        List<Location> locations = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getLocationList(?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, locationName);
            SP.setString(2, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                Location location = new Location();
                location.setLocationId(rs.getInt("LocationId"));
                location.setLocationName(rs.getString("LocationName"));
                location.setLocationNameDisp(rs.getString("LocationNameDisp"));
                location.setStatus(rs.getString("Status"));
                location.setCreatedBy(rs.getInt("CreatedBy"));
                location.setCreatedDate(rs.getString("CreatedDate"));
                location.setUpdatedBy(rs.getInt("UpdatedBy"));
                location.setUpdatedDate(rs.getString("UpdatedDate"));
                locations.add(location);
            }
        } catch (Exception e) {
            System.out.println("Error in getLocationList: " + e);
        }
        return locations;
    }

    public SBU getSBUDetails(int sbuId) {
        SBU sbuDetails = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getSBUDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, sbuId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                sbuDetails = new SBU();
                sbuDetails.setCompId(rs.getString("CompId"));
                sbuDetails.setCompName(rs.getString("CompName"));
                sbuDetails.setSbuId(rs.getInt("SBUId"));
                sbuDetails.setSbuName(rs.getString("SBUName"));
                sbuDetails.setLocationId(rs.getInt("LocationId"));
                sbuDetails.setLocationName(rs.getString("LocationName"));
                sbuDetails.setStatus(rs.getString("Status"));
                sbuDetails.setCreatedBy(rs.getInt("CreatedBy"));
                sbuDetails.setCreatedDate(rs.getString("CreatedDate"));
                sbuDetails.setUpdatedBy(rs.getInt("UpdatedBy"));
                sbuDetails.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getSBUDetails: " + e);
        }
        return sbuDetails;
    }

    public List<String> getUserPassword(int userId)
    {
        Connection conn = getConnection();
        List<String> result = new ArrayList<>();
        result.add("Password: null");
        String SP_String = "{call dbo.SP_getUserPassword(?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);
            SP.setInt(1, userId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                String password = rs.getString("Password");
                result.set(0, password);
            }
        } catch (Exception e) {
            System.out.println("Error in getSBUList: " + e);
        }
        return result;
    }

    public String getUserIdByAndroidId(String androidId) {
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_GetUserIdByAndroidId(?)}";
        String userId = null;
        String saveStatus = null;
        String errorMessage = null;

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            SP.setString(1, androidId);
            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                saveStatus = rs.getString("SaveStatus");
                errorMessage = rs.getString("ErrorMessage");
                userId = rs.getString("UserId");
            }

            if ("Y".equals(saveStatus)) {
                return userId;
            } else {
                System.out.println("Error: " + errorMessage);
                return "";
            }

        } catch (Exception e) {
            System.out.println("Error in getUserIdByAndroidId: " + e);
            return "";
        }
    }

    public List<SBU> getSBUList(String compId, String sbuName, int locationId, String status) {
        List<SBU> sbuList = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getSBUList(?, ?, ?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            SP.setString(1, compId);
            SP.setString(2, sbuName);
            SP.setInt(3, locationId);
            SP.setString(4, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                SBU sbu = new SBU();
                sbu.setCompId(rs.getString("CompId"));
                sbu.setCompName(rs.getString("CompName"));
                sbu.setSbuId(rs.getInt("SBUId"));
                sbu.setSbuName(rs.getString("SBUName"));
                sbu.setLocationId(rs.getInt("LocationId"));
                sbu.setLocationName(rs.getString("LocationName"));
                sbu.setSbuNameDisp(rs.getString("SBUNameDisp"));
                sbu.setStatus(rs.getString("Status"));
                sbu.setCreatedBy(rs.getInt("CreatedBy"));
                sbu.setCreatedDate(rs.getString("CreatedDate"));
                sbu.setUpdatedBy(rs.getInt("UpdatedBy"));
                sbu.setUpdatedDate(rs.getString("UpdatedDate"));
                sbuList.add(sbu);
            }
        } catch (Exception e) {
            System.out.println("Error in getSBUList: " + e);
        }
        return sbuList;
    }

    public UserProfile getUserDetails(int userId) {

        UserProfile userDetails = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getUserDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, userId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                userDetails = new UserProfile();
                userDetails.setUserId(rs.getInt("UserId"));
                userDetails.setUsername(rs.getString("Username"));
                userDetails.setLoginType(rs.getString("LoginType"));
                userDetails.setFullName(rs.getString("FullName"));
                userDetails.setStatus(rs.getString("Status"));
                userDetails.setSbuId(rs.getInt("SBUId"));
                userDetails.setSbuName(rs.getString("SBUName"));
                userDetails.setLocationId(rs.getInt("LocationId"));
                userDetails.setLocationName(rs.getString("LocationName"));
                userDetails.setCompId(rs.getString("CompId"));
                userDetails.setCompName(rs.getString("CompName"));
                userDetails.setSbuNameDisp(rs.getString("SBUNameDisp"));
                userDetails.setDefaultGateId(rs.getInt("DefaultGateId"));
                userDetails.setGateName(rs.getString("GateName"));
                userDetails.setAdminFlag(rs.getString("AdminFlag"));
                userDetails.setCreatedBy(rs.getInt("CreatedBy"));
                userDetails.setCreatedDate(rs.getString("CreatedDate"));
                userDetails.setUpdatedBy(rs.getInt("UpdatedBy"));
                userDetails.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getUserDetails: " + e);
        }
        return userDetails;
    }

    public List<UserProfile> getUserList(String username, String loginType, String fullName, int sbuId, String status) {
        List<UserProfile> userList = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getUserList(?, ?, ?, ?, ?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, username);
            SP.setString(2, loginType);
            SP.setString(3, fullName);
            SP.setInt(4, sbuId);
            SP.setString(5, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setUserId(rs.getInt("UserId"));
                userProfile.setUsername(rs.getString("Username"));
                userProfile.setLoginType(rs.getString("LoginType"));
                userProfile.setFullName(rs.getString("FullName"));
                userProfile.setStatus(rs.getString("Status"));
                userProfile.setSbuId(rs.getInt("SBUId"));
                userProfile.setSbuName(rs.getString("SBUName"));
                userProfile.setLocationId(rs.getInt("LocationId"));
                userProfile.setLocationName(rs.getString("LocationName"));
                userProfile.setCompId(rs.getString("CompId"));
                userProfile.setCompName(rs.getString("CompName"));
                userProfile.setSbuNameDisp(rs.getString("SBUNameDisp"));
                userProfile.setDefaultGateId(rs.getInt("DefaultGateId"));
                userProfile.setGateName(rs.getString("GateName"));
                userProfile.setAdminFlag(rs.getString("AdminFlag"));
                userProfile.setCreatedBy(rs.getInt("CreatedBy"));
                userProfile.setCreatedDate(rs.getString("CreatedDate"));
                userProfile.setUpdatedBy(rs.getInt("UpdatedBy"));
                userProfile.setUpdatedDate(rs.getString("UpdatedDate"));
                userList.add(userProfile);
            }
        } catch (Exception e) {
            System.out.println("Error in getUserList: " + e);
        }
        return userList;
    }

    public UserProfile getUserProfile(int userId) {
        UserProfile userProfile = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getUserProfile(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, userId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                userProfile = new UserProfile();
                userProfile.setUserId(rs.getInt("UserId"));
                userProfile.setUsername(rs.getString("Username"));
                userProfile.setFullName(rs.getString("FullName"));
                userProfile.setAdminFlag(rs.getString("AdminFlag"));
                userProfile.setSbuId(rs.getInt("SBUId"));
                userProfile.setDefaultGateId(rs.getInt("DefaultGateId"));
                userProfile.setStatus(rs.getString("Status"));
            }
        } catch (Exception e) {
            System.out.println("Error in getUserProfile: " + e);
        }
        return userProfile;
    }

    public VisitorSearchResult getVisitorDetails(long uniqueId) {
        VisitorSearchResult visitorDetails = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitorDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setLong(1, uniqueId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                visitorDetails = new VisitorSearchResult();
                visitorDetails.setUniqueId(rs.getInt("UniqueId"));
                visitorDetails.setVisitorId(rs.getInt("VisitorId"));
                visitorDetails.setCompId(rs.getString("CompId"));
                visitorDetails.setCompName(rs.getString("CompName"));
                visitorDetails.setSbuId(rs.getInt("SBUId"));
                visitorDetails.setSbuName(rs.getString("SBUName"));
                visitorDetails.setLabelAddress(rs.getString("LabelAddress"));
                visitorDetails.setLocationId(rs.getInt("LocationId"));
                visitorDetails.setLocationName(rs.getString("LocationName"));
                visitorDetails.setGateId(rs.getInt("GateId"));
                visitorDetails.setGateName(rs.getString("GateName"));
                visitorDetails.setMobileNo(rs.getString("MobileNo"));
                visitorDetails.setVisitorName(rs.getString("VisitorName"));
                visitorDetails.setVisitorPlace(rs.getString("VisitorPlace"));
                visitorDetails.setVisitorCompany(rs.getString("VisitorCompany"));
                visitorDetails.setVisitorDesignation(rs.getString("VisitorDesignation"));
                visitorDetails.setVisitorTypeId(rs.getInt("VisitorTypeId"));
                visitorDetails.setVisitorTypeName(rs.getString("VisitorTypeName"));
                visitorDetails.setPurpose(rs.getString("Purpose"));
                visitorDetails.setVisitingFaculty(rs.getString("VisitingFaculty"));
                visitorDetails.setVisitingAreaId(rs.getInt("VisitingAreaId"));
                visitorDetails.setVisitingAreaName(rs.getString("VisitingAreaName"));
                visitorDetails.setApproverName(rs.getString("ApproverName"));
                visitorDetails.setRefMail(rs.getString("RefMail"));
                visitorDetails.setAsset1(rs.getString("Asset1"));
                visitorDetails.setAsset2(rs.getString("Asset2"));
                visitorDetails.setAsset3(rs.getString("Asset3"));
                visitorDetails.setAsset4(rs.getString("Asset4"));
                visitorDetails.setAsset5(rs.getString("Asset5"));
                visitorDetails.setSecurityName(rs.getString("SecurityName"));
                visitorDetails.setSecurityId(rs.getInt("SecurityId"));
                visitorDetails.setPhotoFilePath(rs.getString("PhotoFilePath"));
                visitorDetails.setPhotoFileName(rs.getString("PhotoFileName"));
                visitorDetails.setIdProofType(rs.getInt("IDProofType"));
                visitorDetails.setIdProofTypeName(rs.getString("IDProofTypeName"));
                visitorDetails.setIdProofValueType(rs.getString("IdProofValueType"));
                visitorDetails.setIdProofNo(rs.getString("IDproofNo"));
                visitorDetails.setIdProofFilePath(rs.getString("IdProofFilePath"));
                visitorDetails.setIdProofFileName(rs.getString("IdProofFileName"));
                visitorDetails.setApprovalMailFilePath(rs.getString("ApprovalMailFilePath"));
                visitorDetails.setApprovalMailFileName(rs.getString("ApprovalMailFileName"));
                visitorDetails.setEntryDatetime(rs.getString("EntryDatetime"));
                visitorDetails.setEntryCreatedBy(rs.getInt("EntryCreatedBy"));
                visitorDetails.setNdaStatus(rs.getString("NDAStatus"));
                visitorDetails.setNdaRemarks(rs.getString("NDARemarks"));
                visitorDetails.setNdaUpdatedBy(rs.getInt("NDAUpdatedBy"));
                visitorDetails.setNdaUpdatedDate(rs.getString("NDAUpdatedDate"));
                visitorDetails.setExitDatetime(rs.getString("ExitDatetime"));
                visitorDetails.setExitCreatedBy(rs.getInt("ExitCreatedBy"));
                visitorDetails.setCreatedBy(rs.getInt("CreatedBy"));
                visitorDetails.setCreatedDate(rs.getString("CreatedDate"));
                visitorDetails.setUpdatedBy(rs.getInt("UpdatedBy"));
                visitorDetails.setUpdatedDate(rs.getString("UpdatedDate"));

            }
        } catch (Exception e) {
            System.out.println("Error in getVisitorDetails: " + e);
        }
        return visitorDetails;
    }

    public List<VisitingArea> getVisitingAreaList(String compId, int sbuId, String areaName, String status) {
        List<VisitingArea> visitingAreas = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitingAreaList(?, ?, ?, ?)}";
        Log.d("after database call","SP_String");

        try {
            CallableStatement SP = conn.prepareCall(SP_String);
            Log.d("SP",""+SP);
            // Set input parameters
            SP.setString(1, compId);
            SP.setInt(2, sbuId);
            SP.setString(3, areaName);
            SP.setString(4, status);

            ResultSet rs = SP.executeQuery();
            Log.d("rs",""+rs);
            while (rs.next()) {
                VisitingArea va = new VisitingArea();
                va.setCompId(rs.getString("CompId"));
                va.setCompName(rs.getString("CompName"));
                va.setSbuId(rs.getInt("SBUId"));
                va.setSbuName(rs.getString("SBUName"));
                va.setAreaId(rs.getInt("AreaId"));
                va.setAreaName(rs.getString("AreaName"));
                va.setStatus(rs.getString("Status"));
                va.setCreatedBy(rs.getInt("CreatedBy"));
                va.setCreatedDate(rs.getString("CreatedDate"));
                va.setUpdatedBy(rs.getInt("UpdatedBy"));
                va.setUpdatedDate(rs.getString("UpdatedDate"));
                visitingAreas.add(va);
            }
        } catch (Exception e) {
            System.out.println("Error in getVisitingAreaList: " + e);
        }
        Log.d("Visiting areas",""+visitingAreas);
        return visitingAreas;
    }

    public VisitingArea getVisitingAreaDetails(int areaId) {
        VisitingArea visitingArea = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitingAreaDetails(?)}";

        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, areaId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                visitingArea = new VisitingArea();
                visitingArea.setCompId(rs.getString("CompId"));
                visitingArea.setCompName(rs.getString("CompName"));
                visitingArea.setSbuId(rs.getInt("SBUId"));
                visitingArea.setSbuName(rs.getString("SBUName"));
                visitingArea.setAreaId(rs.getInt("AreaId"));
                visitingArea.setAreaName(rs.getString("AreaName"));
                visitingArea.setStatus(rs.getString("Status"));
                visitingArea.setCreatedBy(rs.getInt("CreatedBy"));
                visitingArea.setCreatedDate(rs.getString("CreatedDate"));
                visitingArea.setUpdatedBy(rs.getInt("UpdatedBy"));
                visitingArea.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        } catch (Exception e) {
            System.out.println("Error in getVisitingAreaDetails: " + e);
        }
        return visitingArea;
    }

    public List<VisitorSearchResult> getVisitorList(int sbuId, int gateId, Timestamp entryDateFrom, Timestamp entryDateTo,
                                                    int visitorId, String visitorName, String mobileNo,
                                                    String visitorPlace, String visitorCompany, int visitorTypeId,
                                                    int visitingAreaId, String visitingFaculty, String approverName,
                                                    String securityName, String securityId, String ndaStatus,
                                                    String exitStatus, int userId) {
        List<VisitorSearchResult> results = new ArrayList<>();
        Connection conn = null;
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;

        Log.d("getVisitorList", "sbuId: " + sbuId + ", gateId: " + gateId + ", entryDateFrom: " + entryDateFrom +
                ", entryDateTo: " + entryDateTo + ", visitorId: " + visitorId + ", visitorName: " + visitorName +
                ", mobileNo: " + mobileNo + ", visitorPlace: " + visitorPlace + ", visitorCompany: " + visitorCompany +
                ", visitorTypeId: " + visitorTypeId + ", visitingAreaId: " + visitingAreaId + ", visitingFaculty: " + visitingFaculty +
                ", approverName: " + approverName + ", securityName: " + securityName + ", securityId: " + securityId +
                ", ndaStatus: " + ndaStatus + ", exitStatus: " + exitStatus + ", userId: " + userId);

        String SP_String = "{call dbo.SP_getVisitorList(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try {
            conn = getConnection();
            if (conn == null) {
                throw new SQLException("Failed to establish a database connection.");
            }

            callableStatement = conn.prepareCall(SP_String);

            // Set input parameters
            callableStatement.setInt(1, sbuId);
            callableStatement.setInt(2, gateId);
            callableStatement.setTimestamp(3, entryDateFrom);
            callableStatement.setTimestamp(4, entryDateTo);
            callableStatement.setInt(5, visitorId);
            callableStatement.setString(6, visitorName);
            callableStatement.setString(7, mobileNo);
            callableStatement.setString(8, visitorPlace);
            callableStatement.setString(9, visitorCompany);
            callableStatement.setInt(10, visitorTypeId);
            callableStatement.setInt(11, visitingAreaId);
            callableStatement.setString(12, visitingFaculty);
            callableStatement.setString(13, approverName);
            callableStatement.setString(14, securityName);
            callableStatement.setString(15, securityId);
            callableStatement.setString(16, ndaStatus);
            callableStatement.setString(17, exitStatus);
            callableStatement.setInt(18, userId);

            resultSet = callableStatement.executeQuery();
            int count=0;
            while (resultSet.next()) {
                count++;
                Log.d("Tag1","count="+count);

                VisitorSearchResult result = new VisitorSearchResult();
                result.setUniqueId(resultSet.getInt("UniqueId"));
                result.setVisitorId(resultSet.getInt("VisitorId"));
                result.setCompId(resultSet.getString("CompId"));
                result.setCompName(resultSet.getString("CompName"));
                result.setSbuId(resultSet.getInt("SBUId"));
                result.setSbuName(resultSet.getString("SBUName"));
                result.setLabelAddress(resultSet.getString("LabelAddress"));
                result.setLocationId(resultSet.getInt("LocationId"));
                result.setLocationName(resultSet.getString("LocationName"));
                result.setGateId(resultSet.getInt("GateId"));
                result.setGateName(resultSet.getString("GateName"));
                result.setMobileNo(resultSet.getString("MobileNo"));
                result.setVisitorName(resultSet.getString("VisitorName"));
                result.setVisitorPlace(resultSet.getString("VisitorPlace"));
                result.setVisitorCompany(resultSet.getString("VisitorCompany"));
                result.setVisitorDesignation(resultSet.getString("VisitorDesignation"));
                result.setVisitorTypeId(resultSet.getInt("VisitorTypeId"));
                result.setVisitorTypeName(resultSet.getString("VisitorTypeName"));
                result.setPurpose(resultSet.getString("Purpose"));
                result.setVisitingFaculty(resultSet.getString("VisitingFaculty"));
                result.setVisitingAreaId(resultSet.getInt("VisitingAreaId"));
                result.setVisitingAreaName(resultSet.getString("VisitingAreaName"));
                result.setApproverName(resultSet.getString("ApproverName"));
                result.setRefMail(resultSet.getString("RefMail"));
                result.setAsset1(resultSet.getString("Asset1"));
                result.setAsset2(resultSet.getString("Asset2"));
                result.setAsset3(resultSet.getString("Asset3"));
                result.setAsset4(resultSet.getString("Asset4"));
                result.setAsset5(resultSet.getString("Asset5"));
                result.setSecurityName(resultSet.getString("SecurityName"));
                result.setSecurityId(resultSet.getInt("SecurityId"));
                result.setPhotoFilePath(resultSet.getString("PhotoFilePath"));
                result.setPhotoFileName(resultSet.getString("PhotoFileName"));
                result.setIdProofType(resultSet.getInt("IDProofType"));
                result.setIdProofTypeName(resultSet.getString("IDProofTypeName"));
                result.setIdProofValueType(resultSet.getString("IdProofValueType"));
                result.setIdProofNo(resultSet.getString("IDproofNo"));
                result.setIdProofFilePath(resultSet.getString("IdProofFilePath"));
                result.setIdProofFileName(resultSet.getString("IdProofFileName"));
                result.setApprovalMailFilePath(resultSet.getString("ApprovalMailFilePath"));
                result.setApprovalMailFileName(resultSet.getString("ApprovalMailFileName"));
                result.setEntryDatetime(resultSet.getString("EntryDatetime"));
                result.setEntryCreatedBy(resultSet.getInt("EntryCreatedBy"));
                result.setNdaStatus(resultSet.getString("NDAStatus"));
                result.setNdaRemarks(resultSet.getString("NDARemarks"));
                result.setNdaUpdatedBy(resultSet.getInt("NDAUpdatedBy"));
                result.setNdaUpdatedDate(resultSet.getString("NDAUpdatedDate"));
                result.setExitDatetime(resultSet.getString("ExitDatetime"));
                result.setExitCreatedBy(resultSet.getInt("ExitCreatedBy"));
                result.setCreatedBy(resultSet.getInt("CreatedBy"));
                result.setCreatedDate(resultSet.getString("CreatedDate"));
                result.setUpdatedBy(resultSet.getInt("UpdatedBy"));
                result.setUpdatedDate(resultSet.getString("UpdatedDate"));
                result.setNdaUniqueId(resultSet.getInt("NDAUniqueId"));

                results.add(result);
            }
        } catch (Exception e) {
            Log.e("Tag1", "Error in getVisitorList: ", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (callableStatement != null) callableStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("Tag1", "Error closing resources: ", e);
            }
        }
        return results;
    }


    public List<VisitorSearchResult> getVisitorSearchByMobile(String mobileNo, int gateId, int userId)
    {

        List<VisitorSearchResult> results = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitorSearchbyMobile(?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, mobileNo);
            SP.setInt(2, gateId);
            SP.setInt(3, userId);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                VisitorSearchResult result = new VisitorSearchResult();
                result.setUniqueId(rs.getInt("UniqueId"));
                result.setVisitorId(rs.getInt("VisitorId"));
                result.setCompId(rs.getString("CompId"));
                result.setCompName(rs.getString("CompName"));
                result.setSbuId(rs.getInt("SBUId"));
                result.setSbuName(rs.getString("SBUName"));
                result.setLabelAddress(rs.getString("LabelAddress"));
                result.setLocationId(rs.getInt("LocationId"));
                result.setLocationName(rs.getString("LocationName"));
                result.setGateId(rs.getInt("GateId"));
                result.setGateName(rs.getString("GateName"));
                result.setMobileNo(rs.getString("MobileNo"));
                result.setVisitorName(rs.getString("VisitorName"));
                result.setVisitorPlace(rs.getString("VisitorPlace"));
                result.setVisitorCompany(rs.getString("VisitorCompany"));
                result.setVisitorDesignation(rs.getString("VisitorDesignation"));
                result.setVisitorTypeId(rs.getInt("VisitorTypeId"));
                result.setVisitorTypeName(rs.getString("VisitorTypeName"));
                result.setPurpose(rs.getString("Purpose"));
                result.setVisitingFaculty(rs.getString("VisitingFaculty"));
                result.setVisitingAreaId(rs.getInt("VisitingAreaId"));
                result.setVisitingAreaName(rs.getString("VisitingAreaName"));
                result.setApproverName(rs.getString("ApproverName"));
                result.setRefMail(rs.getString("RefMail"));
                result.setAsset1(rs.getString("Asset1"));
                result.setAsset2(rs.getString("Asset2"));
                result.setAsset3(rs.getString("Asset3"));
                result.setAsset4(rs.getString("Asset4"));
                result.setAsset5(rs.getString("Asset5"));
                result.setSecurityName(rs.getString("SecurityName"));
                result.setSecurityId(rs.getInt("SecurityId"));
                result.setPhotoFilePath(rs.getString("PhotoFilePath"));
                result.setPhotoFileName(rs.getString("PhotoFileName"));
                result.setIdProofType(rs.getInt("IDProofType"));
                result.setIdProofTypeName(rs.getString("IDProofTypeName"));
                result.setIdProofValueType(rs.getString("IdProofValueType"));
                result.setIdProofNo(rs.getString("IDproofNo"));
                result.setIdProofFilePath(rs.getString("IdProofFilePath"));
                result.setIdProofFileName(rs.getString("IdProofFileName"));
                result.setApprovalMailFilePath(rs.getString("ApprovalMailFilePath"));
                result.setApprovalMailFileName(rs.getString("ApprovalMailFileName"));
                result.setEntryDatetime(rs.getString("EntryDatetime"));
                result.setEntryCreatedBy(rs.getInt("EntryCreatedBy"));
                result.setExitDatetime(rs.getString("ExitDatetime"));
                result.setExitCreatedBy(rs.getInt("ExitCreatedBy"));
                result.setCreatedBy(rs.getInt("CreatedBy"));
                result.setCreatedDate(rs.getString("CreatedDate"));
                result.setUpdatedBy(rs.getInt("UpdatedBy"));
                result.setUpdatedDate(rs.getString("UpdatedDate"));
                result.setBlackListNumber(rs.getString("BlackListNumber"));
                result.setBlackListReason(rs.getString("BlackListReason"));

                results.add(result);
            }
        }
        catch (Exception e) {
            System.out.println("Error in getVisitorSearchByMobile: " + e);
        }
        return results;
    }


    public VisitorType getVisitorTypeDetails(int visitorTypeId) {
        VisitorType visitorType = null;
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitorTypeDetails(?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameter
            SP.setInt(1, visitorTypeId);

            ResultSet rs = SP.executeQuery();

            if (rs.next()) {
                visitorType = new VisitorType();
                visitorType.setVisitorTypeId(rs.getInt("VisitorTypeId"));
                visitorType.setVisitorTypeName(rs.getString("VisitorTypeName"));
                visitorType.setStatus(rs.getString("Status"));
                visitorType.setCreatedBy(rs.getInt("CreatedBy"));
                visitorType.setCreatedDate(rs.getString("CreatedDate"));
                visitorType.setUpdatedBy(rs.getInt("UpdatedBy"));
                visitorType.setUpdatedDate(rs.getString("UpdatedDate"));
            }
        }
        catch (Exception e) {
            System.out.println("Error in getVisitorTypeDetails: " + e);
        }
        return visitorType;
    }


    public List<VisitorType> getVisitorTypeList(String visitorTypeName, String status) {
        List<VisitorType> visitorTypes = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_getVisitorTypeList(?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, visitorTypeName);
            SP.setString(2, status);

            ResultSet rs = SP.executeQuery();

            while (rs.next()) {
                VisitorType vt = new VisitorType();
                vt.setVisitorTypeId(rs.getInt("VisitorTypeId"));
                vt.setVisitorTypeName(rs.getString("VisitorTypeName"));
                vt.setVisitorTypeNameDisp(rs.getString("VisitorTypeNameDisp"));
                vt.setStatus(rs.getString("Status"));
                vt.setCreatedBy(rs.getInt("CreatedBy"));
                vt.setCreatedDate(rs.getString("CreatedDate"));
                vt.setUpdatedBy(rs.getInt("UpdatedBy"));
                vt.setUpdatedDate(rs.getString("UpdatedDate"));
                visitorTypes.add(vt);
            }
        }
        catch (Exception e) {
            System.out.println("Error in getVisitorTypeList: " + e);
        }
        return visitorTypes;
    }

    public List<String> resetUserPassword(String userId, String password, int updatedBy) {
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_ResetUserPassword(?, ?, ?)}";

        List<String> result = new ArrayList<>();
        result.add("Id: -1");  // Default values in case of failure
        result.add("Count: 0");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            sp=conn.prepareCall(SP_String);

            // Set input parameters
            sp.setString(1, userId);
            sp.setString(2, password);
            sp.setInt(3, updatedBy);

            rs = sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                long count = rs.getLong("Count");

                result.set(0, "Id: " + id);
                result.set(1, "Message: " + count);
            }

        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in resetUserPassword: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }


    public List<String> updateCompanyDetails(String compId, String companyName, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateCompanyDetails(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, compId);
            SP.setString(2, companyName);
            SP.setString(3, status);
            SP.setInt(4, userId);

            // Register output parameters
            SP.registerOutParameter(5, Types.VARCHAR); // ID
            SP.registerOutParameter(6, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(7, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(SP.getString(5)); // ID
            result.add(SP.getString(6)); // SaveStatus
            result.add(SP.getString(7)); // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateCompanyDetails: " + e);
        }
        return result;
    }


    public List<String> updateGateDetails(int gateId, int sbuId, String gateName, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateGateDetails(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setInt(1, gateId);
            SP.setInt(2, sbuId);
            SP.setString(3, gateName);
            SP.setString(4, status);
            SP.setInt(5, userId);

            // Register output parameters
            SP.registerOutParameter(6, Types.INTEGER); // ID
            SP.registerOutParameter(7, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(8, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getInt(6))); // ID
            result.add(SP.getString(7)); // SaveStatus
            result.add(SP.getString(8)); // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateGateDetails: " + e);
        }
        return result;
    }


    public List<String> updateIdProofTypeDetails(int idProofTypeId, String idProofTypeName, String valueType,
                                                 String fieldName, int displayOrder, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateIdProofTypeDetails(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setInt(1, idProofTypeId);
            SP.setString(2, idProofTypeName);
            SP.setString(3, valueType);
            SP.setString(4, fieldName);
            SP.setInt(5, displayOrder);
            SP.setString(6, status);
            SP.setInt(7, userId);

            // Register output parameters
            SP.registerOutParameter(8, Types.INTEGER);  // ID
            SP.registerOutParameter(9, Types.INTEGER);  // Count
            SP.registerOutParameter(10, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(11, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getInt(8)));    // ID
            result.add(String.valueOf(SP.getInt(9)));    // Count
            result.add(SP.getString(10));                // SaveStatus
            result.add(SP.getString(11));                // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateIdProofTypeDetails: " + e);
        }
        return result;
    }


    public List<String> updateNDAStatus(long uniqueId, String ndaStatus, String ndaRemarks, long ndaUniqueId, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateNDAStatus(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setLong(1, uniqueId);
            SP.setString(2, ndaStatus);
            SP.setString(3, ndaRemarks);
            SP.setLong(4, ndaUniqueId);
            SP.setInt(5, userId);

            // Register output parameters
            SP.registerOutParameter(6, Types.NUMERIC); // ID
            SP.registerOutParameter(7, Types.INTEGER); // Count

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getBigDecimal(6))); // ID
            result.add(String.valueOf(SP.getInt(7))); // Count
        }
        catch (Exception e) {
            System.out.println("Error in updateNDAStatus: " + e);
        }
        return result;
    }


    public List<String> updateSBUDetails(String compId, int sbuId, String sbuName, int locationId, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateSBUDetails(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setString(1, compId);
            SP.setInt(2, sbuId);
            SP.setString(3, sbuName);
            SP.setInt(4, locationId);
            SP.setString(5, status);
            SP.setInt(6, userId);

            // Register output parameters
            SP.registerOutParameter(7, Types.INTEGER); // ID
            SP.registerOutParameter(8, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(9, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getInt(7))); // ID
            result.add(SP.getString(8)); // SaveStatus
            result.add(SP.getString(9)); // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateSBUDetails: " + e);
        }
        return result;
    }


    public List<String> updateVisitingAreaDetails(int areaId, int sbuId, String areaName, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateVisitingAreaDetails(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            // Set input parameters
            SP.setInt(1, areaId);
            SP.setInt(2, sbuId);
            SP.setString(3, areaName);
            SP.setString(4, status);
            SP.setInt(5, userId);

            // Register output parameters
            SP.registerOutParameter(6, Types.INTEGER); // ID
            SP.registerOutParameter(7, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(8, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getInt(6))); // ID
            result.add(SP.getString(7)); // SaveStatus
            result.add(SP.getString(8)); // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateVisitingAreaDetails: " + e);
        }
        return result;
    }



    public List<String> updateVisitorTypeDetails(int visitorTypeId, String visitorTypeName, String status, int userId) {
        List<String> result = new ArrayList<>();
        Connection conn = getConnection();
        String SP_String = "{call dbo.SP_updateVisitorTypeDetails(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement SP = conn.prepareCall(SP_String);

            SP.setInt(1, visitorTypeId);
            SP.setString(2, visitorTypeName);
            SP.setString(3, status);
            SP.setInt(4, userId);

            // Output parameters
            SP.registerOutParameter(5, Types.INTEGER); // ID
            SP.registerOutParameter(6, Types.VARCHAR); // SaveStatus
            SP.registerOutParameter(7, Types.VARCHAR); // ErrorMessage

            SP.execute();

            // Retrieve output parameters
            result.add(String.valueOf(SP.getInt(5))); // ID
            result.add(SP.getString(6)); // SaveStatus
            result.add(SP.getString(7)); // ErrorMessage
        }
        catch (Exception e) {
            System.out.println("Error in updateVisitorTypeDetails: " + e);
        }
        return result;
    }


    public List<ItemDomain> getRecyclerView(String companyID, String sbuID){


        List<ItemDomain> visitors = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDomain visitor = new ItemDomain();
            visitor.setVisitorId("ID" + i);
            visitor.setVisitorName("Visitor " + i);
            visitor.setVisitorDesignation("Designation " + i);
            visitor.setEntryDate("2024-07-07");
            visitor.setEntryTime("12:00:00");
            visitor.setApproverName("Approver " + i);
            visitor.setPurpose("Purpose " + i);
            visitor.setVisitingArea("Area " + i);
            visitor.setVisitingStaff("Staff " + i);
            visitor.setGateID("Gate " + i);


            if(i==1)
                visitor.setImage("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");
            if(i==2)
                visitor.setImage("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");
            if(i==3)
                visitor.setImage("/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMSEhAQEhIVEBAPFRAQEBAVEhUQDw8QFRUWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMsNygtLisBCgoKDg0OFxAPFy0dHR0tLS0tLS0tLSstKysrLS0tLS0tLS0tLS0tLSstLS0tLS0tLS0tLS0rLS0tLS0rLS0tLf/AABEIALcBEwMBIgACEQEDEQH/xAAbAAACAgMBAAAAAAAAAAAAAAADBAECAAUGB//EADoQAAICAQIEAwYEBAUFAQAAAAECABEDEiEEBTFBIlFhBhMycYGRQqGx8BTB0eEjUmJy8QcWM0OSgv/EABgBAQEBAQEAAAAAAAAAAAAAAAABAgME/8QAIREBAQEAAgMBAAIDAAAAAAAAAAERAiEDEjFBIlETYYH/2gAMAwEAAhEDEQA/ACFZiY4wmKWCSSIom0xssrkIECosy6YOHuSwl8eOpd6masLq0cwvFgIbGJlo9jeFDxMXCoplQ0ry2uLqsJpkBNcgZJC4zKutfPyhTK5IZMk1T6xZ2HkAC1/OK+8yuSNLADfUNgfQWdpm1ucHRe8mLlH2mnUWKDW3dSVLD7m/tBNnYdrPbbTkHyrZx9ZPdr/G6EZJPvZp+XczXLa3Tjt0v6dvlHu9TWsWWGDllGyygxmVbDKyk5pU55VsUE2KDV24iCOeQcUj3UJrPeTJdMUL7uUKNKtGWSU0wFGWV0RtklNEBdscXy4psCsBkSBr/dzI0cciEA1QGTNK5MsBc1aSLgWY5gwQfDpNhjWY1pTRKHDDtMWXUxTFw8aTBJSHWQVXCIVccssm41cQEhFUdTuBB6pqeecwbGtBqJ6AScrka48dp/Nxm16TjA8yCSPodonm5kwGwof5moflVmc9ynmYLMciHK3/AKR+En0JIHr22mq5r7RPkyHDjpgLL5v/AEoq/EEA8v8ANW/rsTwtteiSRteb+1LJe/5KDf2qa7l3/UAMdLeEnYZAACD2sd/pU5TnCO+wsjruNz8/Lzrymrwcte+hq9v5TUkztN76en8X7SYmpeIWrNY+Jxg0h9e4hc3EOFFsM6EWGXrp7Ejt9Ntu2wnEJyvI6aSpKkGxV9Lrb51GOAGbDjCaja7oepHXb5EESbDt0xz6iHRqcbq3Y/6W8x29J13LuZDKiMdnCkMD11L+/wA557hb3gD0UfbVpHhJ89M6LkYY67B2BAPYyy9pY7rhzYB8+nyhGWaXlnFmwpJJrpsB9Jtw07R571VWxwZxQ8wrKhb3cr7uMlZQiAELJMuVlGEAOQwVy+SVUQIJg2aGKwZEALMYJmMYZYJlk0wA3Ml6mRq40qYrhsPDR/Fi9IwmP0i0gXD4I4MUnGIYGQLPilNEcYQZSWAarDIssuOGXHAoFk6YYY5cY4QponKe1OVVyDWNiLG+7UR4R5b1c7b3U4f27QHLiB/CNvPe7/STlOm/He3HZxn4nMq4yURdtvDqXobr8PavKdnyX2RFUw2bSz+oH4b8hGvZXlwo5CN2r8p2PC4/oJwr1yOf/wC0sRs6bPW/U+kNg9iEJs/TYTqMQH9IZc9RkL/onwXs5iRa0i+hNbmJ5/ZXDqJ0gg+fabs8TAZOIlvqk1zL+zCKdhsLryiGdxh1rQXpvWxvY/v1nWnLc5j2r4cMhYdaon63/KSWFjT4ONIeuhB6it/6ztMBsA+YBnkvL+MOvSx6H8htPXeCXwJ/tH6TtxrzeRcCSZepDTbmC8rCGRUClSrLC1MMBN8cqqRtoIwAlYNlh2aAdpKqpSDdZdngHySDComQRyTIUQCVOSpU3AODLjOmPfQiZYoiGMIkYGQ8sDAgSyyhzFGFWK4TGlaQXAkyAZBaUTOK9vsH+JhfzFfY/wB52LPOd9t8erCj90Yfnt/SZvxvj9PcmxVjQDyE3eEduk0/s148eI/6R+W03wZBuWG23WcMeyUYYjUw4pC8YtbEH6y38YtRkTtYYoPJjqDzc0RerBR5k1NPn9ruGUgHIG+RG8dL3GyyippecC8b/Ix/H7TcM2wIIPUitoHj9GRToOoNcmFryFiQ/wD+v3/Ke08uP+Fj2rwrt9J5DwfAvm4lMSjcvR8gAdyZ69gy4/gR1f3YCtRHhInXhfx5ufG5pm5UyuqYWnRyZUypQvM95IJMipBySNcokiDcS2uUd4AXECyQzNBloQB0gXSHyPAl5QE45kNMkVJSQMMYAl0SNTC64YVcMbTHChI0wkMMsMEeCSwSFK48UMqQwSWVYA1SSccYVZJWAmUiHM+Hx58eTAHHvKvTYsHsZuGSaPLwSY8uXMfx0QR1B7/pMeS2Tp28HHjytl/41PLMjJwigHQdWRCfKmM5Lm/PWrIuPXk93Wtl8Kg3XXz+k9F4Dg1yYQCPjORq6HxMTEMvsui2ExCj16iwex2N/Wcvt7+PRJ119ea8Hz/MSSWyIqkDUHsb2NwQPKeley2R+JABYmup6GC4f2SW/hVB5BQZ1PIuXrhICitqiyW9NS2ce3B+32F8LBKLKRYq+k4F+B4hvFjx6TfTRT1XW3Brf0nvfOeCXM1MuoDpNensyn4WZflR/WJMpf5Tu48i4LheYIFZkGUXTIQoc2aAXT3+c9P5B71MSjLiCaj4VLeMX69D8gZ0HA8mTH4iS7dixsj5dh9ItzdTVjcD4l7Mv9R1uOX9pkzN1w2DAuPiOI8Wh2JVT3AY+KvWgfvN77Mcs9z70g2HIKn/AE9f5yw5AvEZHcbPqoE9NOlT+/nN2mAIgUXQ8IvqQO/3jjP5s8rnisiZEi5lz0PCo5gWaFaAdYVgeXDQVSywLlpUtMMo0DCYNjIcwLvCKZXg0aCzNBoZQ+DJigyzJA/70QuPJEgkLjEitkry3vIlrlGzQNqmSFVpqceeMpmgPahM1xT3kz3kodGST7yIDJM97Kh1ngOI4cZAAehNfL1gTllsPE6fW/7zPKbMdPFy9eWj8txaXdegViB+s2mTIoFkgATmcfH0+XsdQoXvVCaX2h5uxHuw1A7sQdwo/S+k4249fGa63DzrE2T3SEEiyW/CK/5j2DMNRtht0nnlouLw9SNivXec+OcZMGoBmdNq1MSwsWDZ3Aq5JW76vXeN4xA3xgH5iaHjvaR8WQ6AM2KgSw6qe4vv2P1nlHMOKz8S1MzhF7KdjsDZr4uo6116Tr+TcSMWAYih6Ek9Te357xyJeMddwvtGuWv08vpGuI4oVfUE1X2/qJ5bk5z7vITY0+YO9fadC3MSdKXsSpNi7H1Ebf1nnZZ06bk7ENmZXCjVVHyobj7TY5suo32GwnN+y5DDK5FkuaJG9V5/eb65248f15efPZiSJQiSWlWYTTkmpBSSGEzUIFCkjTLlhKFxAjTKssk5BIOQQAOsBkSNM4gXYSBLJjgykZyOIJmEoARIlywmQHwJkSHGyrcZIrYaplzWfxsuvFQNkDCDJNcM8IuWA+MstriYaEVoDFybgQ0nVAIZglNUkGBoOdZimYkbWmq/9V/2mk57hLHGw3V1sjvtuN7+k6H2p4YtjGQC/dG2/wBh6/v5zmOFy6sWVS2+JjpPUlGG04853r0+O9YFyvmeX3uPE2HR706VctY36WOg2HTfp5zrk9lMxN/4fivfZvhFdCPWTg5YmXAli6A9D9D2nNcZ7HVkOVOYcThbxbHI5dbqwGu6Olf/AJHlEkr0Zyz+Dov+0M5Ck5ESyVI8gRp2qrO3T1nJ+06vjAxcPxIzZnZxoxkNppiCx3OkCt/XbvCv7JYXGnieYcZnTr7vWzK1b9GvvOh5FyfBjGjh8Aw4/MnVlf1djuZbkPTn959RxPD8lcBEyuXbIy2SLodCNt6m443ihjzDUdlG4+RFX+cf9rHXFkxgbMLodOvf8jOI4vimz5tK7tkIX62bv5b/AGkndcOdn49O9jsl8PqqizMT677Tea/39Ihyzhxjxoi9FVQPoI6pnXjenn5zKtcrvcItSbErIciEJEgkQBEyjGH2lSBAVYyhYxoqJUoICpaCdjHSggXQQEWJgmJjzKIJgICRuZDmpEI1+MGE0SA8sMom8Z1AxwiCWRxDqIxVFjCQZaWTJGGmkhVEFiaNLIKgSalgZVngZJBgjkmLklw0V1sEHcHY/Keee0fAPwWQZUN4MtqL/ASb0n06V9fKeg64vzLhFzYmxuLVwQf5EeonPydR18XdKex/He8xBbBP7sSOd8LmZiMa9upIAHrfX8px/Lhk4DN7ssfdkko3+Zdr38/6zszzdWQZFNWL67/b99pxvT1cOTX4eVcRelmx6tvxNZs15UZ0fLUGJLyMC6/brU53JzXxBbFDeyd9j0F+u81nN/aUFW0kdDY/1Uf5CT6tska3/qPzBXyjRZ0WCe2oGvvt+UT9h+WEv75x/sv16mazHibNk1vuoI69Gbqf1P1nd8hxUBtU3yuTI48Zt11GM0o+kwZZYYyRQ79Io4+k14+45+aZyNfxEg8TFAJNTpjlpg8RM/iIuFkFZUNDiJY54npmVIpv30hs8VqUaMNMPxEXfiYBxBFIxNGycTFn4qDypFHQy4aYPFTIiVmS4aY0mSMZjIEPjWTTCuNCI5iEtpl0jTFHSUCGNgSyrGmB4Y0rygWXCxpiwaUYy4SW0QFCDLKDNlw3LnfoKHmdpuOH5WqC9JY+Z2+01Jajm34dho1eH3lhdiSe/QRtuH0gLd/lNrlA951vwCvJfMCK5hZM4+W/j0eHj+uV5zy9cgKsLG/7ucfx3AZsX/jt1HQdwPKj1npfE4Abmn4jhROG49FkrzDI/EFv/Hks9fCKPbqYTHy53OrLart4Ad2NVvO5yYAOgip4Sz0l92fRr+XcDdbUB0HQCvSdXyrh6qK8Jw9Tc8Am8y1I2WJen3jeflwyCxs3n2PzlMSTZ8MOk1wuJyks7aBuVZOlAk3QB3NeUV93O4VaANeYJ8v3vAcTiRtmUHbuKYfWevjNjxcsl6cholSs3XF8qrdDYO9H+s12fh2X4gR+klmIVKyNMKalCYFSsGywpMC7SCjLKFZJeVLCUCdIDJijDNKM0BFsUyHImQLosOiyqtDI0YmrKkKuKQjQ6kRhqgSFVJliGwqWIAFk9pMXQwkuqzd8FybcHIRVatPn6TZpw6JuqAH5DaanFNc7g5e7dFIHmZuuD5TjTdvEauztX0jrggjej16ij9IPj26bhSDdH8XrNySGisQvkor4u0Ry8SDQLiyarzHr5SrYveeEk/MX067dhB48ekUV2BOkE+IkHt5TSB8WqhS48BW6NVrG/wCUTyDePth1WXBAvw72Pl6RTjb94VHioLQFXW4IAJ6ivznLy+P27n118Xk9eqQzmIZVubJ1skVuoBPbbz9REeISeS8bPr1zlL8ax8W8wcP3jQXeMpgsbC/pcmLpFMc2XArKrw3w3sGYICdrY9o9wwxgUHGqyu4Y0R6Abj7TU8fK/jF8nGfpvAO52A6ntNjhyqO4vyIoEd9/7RHhQ5J1LpUWu4CoP9W/X+8fxJuACpdvEbFdeleU78PFJ9efn5bfhnQOt+HsOgv/AJkAP1G438Oxl/dg0CTSjp1I9PWFXEdvEdul0DOziriIqtJF/EKIr1g2QAaT4/QgQ7YztZAH/wAkn0gzYbfYdz1/+dtzKNLxnI1yWcf+Gd6rxITOa43gc2L40NdmHiU/UTvdYUg3Ste5oAgecFlchuu/dL7HoR6TN4yrrztsxi75TPQOJ5dgey2HfzXwm/pVzT8Z7MqVZsJYsv4D5SelNck2YwR4gxvLwxG3Qj7wB4eZUE5zI9+Yx/DyjYIw0I8QZEucEyXDTawqGVUwyVM6YuphA8hakmo0wzwmFsjBV6/kB5mdTwfDLjU6VLOKJbub/ltEOVY1xY9V+LIN/TyE2JbdQtgru9neiNvp/SdJMZNY81kGgeoFbgQmc3tVEAkHt59Ij/EAacandSNYG170TcLhU7kPq2reiVP/ABLotjy6loC+/Qgf3ksrHZT02s1setCx0lBk20i7FaAevr+cLhxtRDULPYeL6mBTxBfGQN+oJJ+ggHBLH4jV0p8IPrf9I66Da2JPkN/uPKa3jMmggpuSdNqB4b7fWURw50hxRC9dFg0elD9ZrsiMjNYDqw1KxsFQetEXX5Rls1ZPAaJA8GnSDXmT1+XrFmbGCTpcE6iSptFYnyG4/OFURVoAg5Q25IDKQDW9/vpJxZqBCYmYC/xb3vYJ7x7EiqASXCP5spA8gPOJ/wACqWzM2TSSQNRJUXd7Ht6+UCrZBpekbH4TpFjTfcivU0YLJg0qAfDtZ8dAirswz42dgCcblRZQ0DuSdNncbAX+kbwqlkV20i26t1NGq6X9oAOH4XCXx5AAzIN1VSF6UC19NhNoXW9QQh7IDlbb6EXt/WMKQAu19Ko+HrX85OfiCjBaC6qAc+Mk96UV+sgFkc7FiTXYoWDG/wAJFeUtxCkEfCSKKkeFgfL1G8X4rNeqlAyKDRK0KBF6STvsblA9oNQCtanUQXA8hYJvzkDPMXPhIPwldR736Dv1I2hU4taXZmo9fOt97i+ViR1CbHXY8GQWK+UsmFrPRlIAanql86J6G5RbFxiMxb4O7Ai9rrc9pXM2pHYHWq7qdgBRo0fkZV+H0PSoQukdDa0T0Mx0QXYFlgzJY3J8/MVZA2kEprIUadI601HwnoQa+8nIx2oBgCNyt1W+nbcdpPvKUVesWK6lB5V/K+8nBlYKPCwN2STS9D9T8pRfDlUDVWk35MF6779PrMx5GFnSNxZI/wAvavOA47ijYQ47B+K30jfYb/eCawCU8JHU2GBXzXz6eneEa3n/AC0MrZUrWu+QD8Q+necoZ2qceLCeIAbZNSeI2TRrpWx3mi5ty8LkYL8J3HpfaTl/axpCZEcbhIN8NTOrhYmZJKTJNMUVofHkmTIBlebHlWDW19Au/SxfbaZMiTstbI8Qw3AtUsAbXqBFn5b/AGh/4vTqYgsGdLF/CCDYvrW9TJk6VE8Hh1PkDCw2+roQw6jb5XHc7AbbhFs2Ou3UfmJEyQH4VCFDKSVNHfsR+cMeIULqbayK6t1/vMmShR+ZIWLaTQF0KFjYb/0iuUDIlKpDM/wihp0fFv32MyZJKMbCmNSRYWiAb8QsdP1lm4NAq+8T4ttV7kk7dO28yZNIo2FMQtUHfuSKvz/fWCz4AWW0NHdSpFHY7EWD3/KZMgUZWXJYQ43FNYYEOKAprO/76xrjVs/FvjKlxuGJJoVtXpMmSVYYXNk+BVGMAr4SQdvsR5H6xHBxr671jfVu17mthsNhJmTNDuAZGOTWoYhdgr6QdQqum/c3CcLy4BRaU34/ECDfcfpJmTUgM3C2STsB0qvSxXlMxcKceoBlry0CwPTymTIxE53KrY8Q6b7N8r7RMUWRGX4qttrvcKCN7oCZMhTeTIcdBiR+BSabeid69BLl6KgDUTvffT0v6TJkDmW44OzHfdwq7/8AjG1H1JHlVQuLhWXMAt/CupS3Y2Pi3s+lVJmSQRzJ21AswRAfGwvXYI6UP9tfKC4/Im4s6gSRYFEL4TVeo7/nImS8kjUvnEBkyCZMnFsqzCZMmSj/2Q==");
            if(i==4)
                visitor.setImage("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");
            if(i==5)
                visitor.setImage("/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMSEhAQEhIVEBAPFRAQEBAVEhUQDw8QFRUWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMsNygtLisBCgoKDg0OFxAPFy0dHR0tLS0tLS0tLSstKysrLS0tLS0tLS0tLS0tLSstLS0tLS0tLS0tLS0rLS0tLS0rLS0tLf/AABEIALcBEwMBIgACEQEDEQH/xAAbAAACAgMBAAAAAAAAAAAAAAADBAECAAUGB//EADoQAAICAQIEAwYEBAUFAQAAAAECABEDEiEEBTFBIlFhBhMycYGRQqGx8BTB0eEjUmJy8QcWM0OSgv/EABgBAQEBAQEAAAAAAAAAAAAAAAABAgME/8QAIREBAQEAAgMBAAIDAAAAAAAAAAERAiEDEjFBIlETYYH/2gAMAwEAAhEDEQA/ACFZiY4wmKWCSSIom0xssrkIECosy6YOHuSwl8eOpd6masLq0cwvFgIbGJlo9jeFDxMXCoplQ0ry2uLqsJpkBNcgZJC4zKutfPyhTK5IZMk1T6xZ2HkAC1/OK+8yuSNLADfUNgfQWdpm1ucHRe8mLlH2mnUWKDW3dSVLD7m/tBNnYdrPbbTkHyrZx9ZPdr/G6EZJPvZp+XczXLa3Tjt0v6dvlHu9TWsWWGDllGyygxmVbDKyk5pU55VsUE2KDV24iCOeQcUj3UJrPeTJdMUL7uUKNKtGWSU0wFGWV0RtklNEBdscXy4psCsBkSBr/dzI0cciEA1QGTNK5MsBc1aSLgWY5gwQfDpNhjWY1pTRKHDDtMWXUxTFw8aTBJSHWQVXCIVccssm41cQEhFUdTuBB6pqeecwbGtBqJ6AScrka48dp/Nxm16TjA8yCSPodonm5kwGwof5moflVmc9ynmYLMciHK3/AKR+En0JIHr22mq5r7RPkyHDjpgLL5v/AEoq/EEA8v8ANW/rsTwtteiSRteb+1LJe/5KDf2qa7l3/UAMdLeEnYZAACD2sd/pU5TnCO+wsjruNz8/Lzrymrwcte+hq9v5TUkztN76en8X7SYmpeIWrNY+Jxg0h9e4hc3EOFFsM6EWGXrp7Ejt9Ntu2wnEJyvI6aSpKkGxV9Lrb51GOAGbDjCaja7oepHXb5EESbDt0xz6iHRqcbq3Y/6W8x29J13LuZDKiMdnCkMD11L+/wA557hb3gD0UfbVpHhJ89M6LkYY67B2BAPYyy9pY7rhzYB8+nyhGWaXlnFmwpJJrpsB9Jtw07R571VWxwZxQ8wrKhb3cr7uMlZQiAELJMuVlGEAOQwVy+SVUQIJg2aGKwZEALMYJmMYZYJlk0wA3Ml6mRq40qYrhsPDR/Fi9IwmP0i0gXD4I4MUnGIYGQLPilNEcYQZSWAarDIssuOGXHAoFk6YYY5cY4QponKe1OVVyDWNiLG+7UR4R5b1c7b3U4f27QHLiB/CNvPe7/STlOm/He3HZxn4nMq4yURdtvDqXobr8PavKdnyX2RFUw2bSz+oH4b8hGvZXlwo5CN2r8p2PC4/oJwr1yOf/wC0sRs6bPW/U+kNg9iEJs/TYTqMQH9IZc9RkL/onwXs5iRa0i+hNbmJ5/ZXDqJ0gg+fabs8TAZOIlvqk1zL+zCKdhsLryiGdxh1rQXpvWxvY/v1nWnLc5j2r4cMhYdaon63/KSWFjT4ONIeuhB6it/6ztMBsA+YBnkvL+MOvSx6H8htPXeCXwJ/tH6TtxrzeRcCSZepDTbmC8rCGRUClSrLC1MMBN8cqqRtoIwAlYNlh2aAdpKqpSDdZdngHySDComQRyTIUQCVOSpU3AODLjOmPfQiZYoiGMIkYGQ8sDAgSyyhzFGFWK4TGlaQXAkyAZBaUTOK9vsH+JhfzFfY/wB52LPOd9t8erCj90Yfnt/SZvxvj9PcmxVjQDyE3eEduk0/s148eI/6R+W03wZBuWG23WcMeyUYYjUw4pC8YtbEH6y38YtRkTtYYoPJjqDzc0RerBR5k1NPn9ruGUgHIG+RG8dL3GyyippecC8b/Ix/H7TcM2wIIPUitoHj9GRToOoNcmFryFiQ/wD+v3/Ke08uP+Fj2rwrt9J5DwfAvm4lMSjcvR8gAdyZ69gy4/gR1f3YCtRHhInXhfx5ufG5pm5UyuqYWnRyZUypQvM95IJMipBySNcokiDcS2uUd4AXECyQzNBloQB0gXSHyPAl5QE45kNMkVJSQMMYAl0SNTC64YVcMbTHChI0wkMMsMEeCSwSFK48UMqQwSWVYA1SSccYVZJWAmUiHM+Hx58eTAHHvKvTYsHsZuGSaPLwSY8uXMfx0QR1B7/pMeS2Tp28HHjytl/41PLMjJwigHQdWRCfKmM5Lm/PWrIuPXk93Wtl8Kg3XXz+k9F4Dg1yYQCPjORq6HxMTEMvsui2ExCj16iwex2N/Wcvt7+PRJ119ea8Hz/MSSWyIqkDUHsb2NwQPKeley2R+JABYmup6GC4f2SW/hVB5BQZ1PIuXrhICitqiyW9NS2ce3B+32F8LBKLKRYq+k4F+B4hvFjx6TfTRT1XW3Brf0nvfOeCXM1MuoDpNensyn4WZflR/WJMpf5Tu48i4LheYIFZkGUXTIQoc2aAXT3+c9P5B71MSjLiCaj4VLeMX69D8gZ0HA8mTH4iS7dixsj5dh9ItzdTVjcD4l7Mv9R1uOX9pkzN1w2DAuPiOI8Wh2JVT3AY+KvWgfvN77Mcs9z70g2HIKn/AE9f5yw5AvEZHcbPqoE9NOlT+/nN2mAIgUXQ8IvqQO/3jjP5s8rnisiZEi5lz0PCo5gWaFaAdYVgeXDQVSywLlpUtMMo0DCYNjIcwLvCKZXg0aCzNBoZQ+DJigyzJA/70QuPJEgkLjEitkry3vIlrlGzQNqmSFVpqceeMpmgPahM1xT3kz3kodGST7yIDJM97Kh1ngOI4cZAAehNfL1gTllsPE6fW/7zPKbMdPFy9eWj8txaXdegViB+s2mTIoFkgATmcfH0+XsdQoXvVCaX2h5uxHuw1A7sQdwo/S+k4249fGa63DzrE2T3SEEiyW/CK/5j2DMNRtht0nnlouLw9SNivXec+OcZMGoBmdNq1MSwsWDZ3Aq5JW76vXeN4xA3xgH5iaHjvaR8WQ6AM2KgSw6qe4vv2P1nlHMOKz8S1MzhF7KdjsDZr4uo6116Tr+TcSMWAYih6Ek9Te357xyJeMddwvtGuWv08vpGuI4oVfUE1X2/qJ5bk5z7vITY0+YO9fadC3MSdKXsSpNi7H1Ebf1nnZZ06bk7ENmZXCjVVHyobj7TY5suo32GwnN+y5DDK5FkuaJG9V5/eb65248f15efPZiSJQiSWlWYTTkmpBSSGEzUIFCkjTLlhKFxAjTKssk5BIOQQAOsBkSNM4gXYSBLJjgykZyOIJmEoARIlywmQHwJkSHGyrcZIrYaplzWfxsuvFQNkDCDJNcM8IuWA+MstriYaEVoDFybgQ0nVAIZglNUkGBoOdZimYkbWmq/9V/2mk57hLHGw3V1sjvtuN7+k6H2p4YtjGQC/dG2/wBh6/v5zmOFy6sWVS2+JjpPUlGG04853r0+O9YFyvmeX3uPE2HR706VctY36WOg2HTfp5zrk9lMxN/4fivfZvhFdCPWTg5YmXAli6A9D9D2nNcZ7HVkOVOYcThbxbHI5dbqwGu6Olf/AJHlEkr0Zyz+Dov+0M5Ck5ESyVI8gRp2qrO3T1nJ+06vjAxcPxIzZnZxoxkNppiCx3OkCt/XbvCv7JYXGnieYcZnTr7vWzK1b9GvvOh5FyfBjGjh8Aw4/MnVlf1djuZbkPTn959RxPD8lcBEyuXbIy2SLodCNt6m443ihjzDUdlG4+RFX+cf9rHXFkxgbMLodOvf8jOI4vimz5tK7tkIX62bv5b/AGkndcOdn49O9jsl8PqqizMT677Tea/39Ihyzhxjxoi9FVQPoI6pnXjenn5zKtcrvcItSbErIciEJEgkQBEyjGH2lSBAVYyhYxoqJUoICpaCdjHSggXQQEWJgmJjzKIJgICRuZDmpEI1+MGE0SA8sMom8Z1AxwiCWRxDqIxVFjCQZaWTJGGmkhVEFiaNLIKgSalgZVngZJBgjkmLklw0V1sEHcHY/Keee0fAPwWQZUN4MtqL/ASb0n06V9fKeg64vzLhFzYmxuLVwQf5EeonPydR18XdKex/He8xBbBP7sSOd8LmZiMa9upIAHrfX8px/Lhk4DN7ssfdkko3+Zdr38/6zszzdWQZFNWL67/b99pxvT1cOTX4eVcRelmx6tvxNZs15UZ0fLUGJLyMC6/brU53JzXxBbFDeyd9j0F+u81nN/aUFW0kdDY/1Uf5CT6tska3/qPzBXyjRZ0WCe2oGvvt+UT9h+WEv75x/sv16mazHibNk1vuoI69Gbqf1P1nd8hxUBtU3yuTI48Zt11GM0o+kwZZYYyRQ79Io4+k14+45+aZyNfxEg8TFAJNTpjlpg8RM/iIuFkFZUNDiJY54npmVIpv30hs8VqUaMNMPxEXfiYBxBFIxNGycTFn4qDypFHQy4aYPFTIiVmS4aY0mSMZjIEPjWTTCuNCI5iEtpl0jTFHSUCGNgSyrGmB4Y0rygWXCxpiwaUYy4SW0QFCDLKDNlw3LnfoKHmdpuOH5WqC9JY+Z2+01Jajm34dho1eH3lhdiSe/QRtuH0gLd/lNrlA951vwCvJfMCK5hZM4+W/j0eHj+uV5zy9cgKsLG/7ucfx3AZsX/jt1HQdwPKj1npfE4Abmn4jhROG49FkrzDI/EFv/Hks9fCKPbqYTHy53OrLart4Ad2NVvO5yYAOgip4Sz0l92fRr+XcDdbUB0HQCvSdXyrh6qK8Jw9Tc8Am8y1I2WJen3jeflwyCxs3n2PzlMSTZ8MOk1wuJyks7aBuVZOlAk3QB3NeUV93O4VaANeYJ8v3vAcTiRtmUHbuKYfWevjNjxcsl6cholSs3XF8qrdDYO9H+s12fh2X4gR+klmIVKyNMKalCYFSsGywpMC7SCjLKFZJeVLCUCdIDJijDNKM0BFsUyHImQLosOiyqtDI0YmrKkKuKQjQ6kRhqgSFVJliGwqWIAFk9pMXQwkuqzd8FybcHIRVatPn6TZpw6JuqAH5DaanFNc7g5e7dFIHmZuuD5TjTdvEauztX0jrggjej16ij9IPj26bhSDdH8XrNySGisQvkor4u0Ry8SDQLiyarzHr5SrYveeEk/MX067dhB48ekUV2BOkE+IkHt5TSB8WqhS48BW6NVrG/wCUTyDePth1WXBAvw72Pl6RTjb94VHioLQFXW4IAJ6ivznLy+P27n118Xk9eqQzmIZVubJ1skVuoBPbbz9REeISeS8bPr1zlL8ax8W8wcP3jQXeMpgsbC/pcmLpFMc2XArKrw3w3sGYICdrY9o9wwxgUHGqyu4Y0R6Abj7TU8fK/jF8nGfpvAO52A6ntNjhyqO4vyIoEd9/7RHhQ5J1LpUWu4CoP9W/X+8fxJuACpdvEbFdeleU78PFJ9efn5bfhnQOt+HsOgv/AJkAP1G438Oxl/dg0CTSjp1I9PWFXEdvEdul0DOziriIqtJF/EKIr1g2QAaT4/QgQ7YztZAH/wAkn0gzYbfYdz1/+dtzKNLxnI1yWcf+Gd6rxITOa43gc2L40NdmHiU/UTvdYUg3Ste5oAgecFlchuu/dL7HoR6TN4yrrztsxi75TPQOJ5dgey2HfzXwm/pVzT8Z7MqVZsJYsv4D5SelNck2YwR4gxvLwxG3Qj7wB4eZUE5zI9+Yx/DyjYIw0I8QZEucEyXDTawqGVUwyVM6YuphA8hakmo0wzwmFsjBV6/kB5mdTwfDLjU6VLOKJbub/ltEOVY1xY9V+LIN/TyE2JbdQtgru9neiNvp/SdJMZNY81kGgeoFbgQmc3tVEAkHt59Ij/EAacandSNYG170TcLhU7kPq2reiVP/ABLotjy6loC+/Qgf3ksrHZT02s1setCx0lBk20i7FaAevr+cLhxtRDULPYeL6mBTxBfGQN+oJJ+ggHBLH4jV0p8IPrf9I66Da2JPkN/uPKa3jMmggpuSdNqB4b7fWURw50hxRC9dFg0elD9ZrsiMjNYDqw1KxsFQetEXX5Rls1ZPAaJA8GnSDXmT1+XrFmbGCTpcE6iSptFYnyG4/OFURVoAg5Q25IDKQDW9/vpJxZqBCYmYC/xb3vYJ7x7EiqASXCP5spA8gPOJ/wACqWzM2TSSQNRJUXd7Ht6+UCrZBpekbH4TpFjTfcivU0YLJg0qAfDtZ8dAirswz42dgCcblRZQ0DuSdNncbAX+kbwqlkV20i26t1NGq6X9oAOH4XCXx5AAzIN1VSF6UC19NhNoXW9QQh7IDlbb6EXt/WMKQAu19Ko+HrX85OfiCjBaC6qAc+Mk96UV+sgFkc7FiTXYoWDG/wAJFeUtxCkEfCSKKkeFgfL1G8X4rNeqlAyKDRK0KBF6STvsblA9oNQCtanUQXA8hYJvzkDPMXPhIPwldR736Dv1I2hU4taXZmo9fOt97i+ViR1CbHXY8GQWK+UsmFrPRlIAanql86J6G5RbFxiMxb4O7Ai9rrc9pXM2pHYHWq7qdgBRo0fkZV+H0PSoQukdDa0T0Mx0QXYFlgzJY3J8/MVZA2kEprIUadI601HwnoQa+8nIx2oBgCNyt1W+nbcdpPvKUVesWK6lB5V/K+8nBlYKPCwN2STS9D9T8pRfDlUDVWk35MF6779PrMx5GFnSNxZI/wAvavOA47ijYQ47B+K30jfYb/eCawCU8JHU2GBXzXz6eneEa3n/AC0MrZUrWu+QD8Q+necoZ2qceLCeIAbZNSeI2TRrpWx3mi5ty8LkYL8J3HpfaTl/axpCZEcbhIN8NTOrhYmZJKTJNMUVofHkmTIBlebHlWDW19Au/SxfbaZMiTstbI8Qw3AtUsAbXqBFn5b/AGh/4vTqYgsGdLF/CCDYvrW9TJk6VE8Hh1PkDCw2+roQw6jb5XHc7AbbhFs2Ou3UfmJEyQH4VCFDKSVNHfsR+cMeIULqbayK6t1/vMmShR+ZIWLaTQF0KFjYb/0iuUDIlKpDM/wihp0fFv32MyZJKMbCmNSRYWiAb8QsdP1lm4NAq+8T4ttV7kk7dO28yZNIo2FMQtUHfuSKvz/fWCz4AWW0NHdSpFHY7EWD3/KZMgUZWXJYQ43FNYYEOKAprO/76xrjVs/FvjKlxuGJJoVtXpMmSVYYXNk+BVGMAr4SQdvsR5H6xHBxr671jfVu17mthsNhJmTNDuAZGOTWoYhdgr6QdQqum/c3CcLy4BRaU34/ECDfcfpJmTUgM3C2STsB0qvSxXlMxcKceoBlry0CwPTymTIxE53KrY8Q6b7N8r7RMUWRGX4qttrvcKCN7oCZMhTeTIcdBiR+BSabeid69BLl6KgDUTvffT0v6TJkDmW44OzHfdwq7/8AjG1H1JHlVQuLhWXMAt/CupS3Y2Pi3s+lVJmSQRzJ21AswRAfGwvXYI6UP9tfKC4/Im4s6gSRYFEL4TVeo7/nImS8kjUvnEBkyCZMnFsqzCZMmSj/2Q==");
            if(i==6)
                visitor.setImage("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");
            if(i==7)
                visitor.setImage("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");
            if(i==8)
                visitor.setImage("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");
            if(i==9)
                visitor.setImage("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");
            if(i==10)
                visitor.setImage("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");

            visitors.add(visitor);
        }
        return visitors;
    }

    public List<String> updateVisitorPhoto(long uniqueId, String photoFilePath, String photoFileName) throws SQLException {
        Log.d("Inside updateVisitorPhoto", "uniqueId: " + uniqueId);
        Log.d("PhotoFileName", photoFileName);
        Log.d("PhotoFilePath", photoFilePath);

        Connection conn = getConnection();
        String spString="{call [dbo].[SP_updateVisitorPhoto](?, ?, ?)}";
        List<String> result = new ArrayList<>();
        result.add(0,"Id : null");
        result.add(1,"SaveStatus: N");
        result.add(2,"ErrorMessage: null");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            // Prepare and execute the stored procedure call
            sp = conn.prepareCall(spString);

            // Set the parameters
            sp.setLong(1, uniqueId);
            sp.setString(2, photoFilePath);
            sp.setString(3, photoFileName);

            // Execute the stored procedure
            rs=sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                String savestatus=rs.getString("SaveStatus");
                String message = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Save status: "+savestatus);
                result.set(2, "Message: " + message);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in updateVisitorPhoto: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> updateVisitorIDProofDetails(long uniqueId, int idProofType, String idProofNumber, String idProofFilePath, String idProofFileName) throws SQLException {
        Log.d("Inside updateVisitorIDProofDetails", "uniqueId: " + uniqueId);
        Log.d("IDProofType", String.valueOf(idProofType));
        Log.d("IDProofNumber", String.valueOf(idProofNumber));
        //Log.d("IDProofFilePath", idProofFilePath);
        //Log.d("IDProofFileName", idProofFileName);

        Connection conn = getConnection();
        String spString = "{call [dbo].[SP_updateVisitorIDProofDetails](?, ?, ?, ?, ?)}";
        List<String> result = new ArrayList<>();
        result.add("Id : null");
        result.add("SaveStatus: N");
        result.add("ErrorMessage: null");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            // Prepare and execute the stored procedure call
            sp = conn.prepareCall(spString);

            // Set the parameters
            sp.setLong(1, uniqueId);
            sp.setInt(2, idProofType);
            sp.setString(3, idProofNumber);
            sp.setString(4, idProofFilePath);
            sp.setString(5, idProofFileName);

            // Execute the stored procedure
            rs = sp.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("ID");
                String saveStatus = rs.getString("SaveStatus");
                String message = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, "Id: " + id);
                result.set(1, "Save status: " + saveStatus);
                result.set(2, "Message: " + message);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in updateVisitorIDProofDetails: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> getIDProofTypeIdByName(String idProofTypeName) throws SQLException {
        Log.d("Inside getIDProofTypeIdByName", "IDProofTypeName: " + idProofTypeName);

        Connection conn = getConnection();
        String spString = "{call [dbo].[SP_getIDProofTypeIdByName](?)}";
        List<String> result = new ArrayList<>();
        result.add("0");
        result.add("SaveStatus: N");
        result.add("ErrorMessage: null");

        CallableStatement sp = null;
        ResultSet rs = null;

        try {
            // Prepare and execute the stored procedure call
            sp = conn.prepareCall(spString);

            // Set the parameter
            sp.setString(1, idProofTypeName);

            // Execute the stored procedure
            rs = sp.executeQuery();

            if (rs.next()) {
                int idProofTypeId = rs.getInt("IDProofTypeId");
                String saveStatus = rs.getString("SaveStatus");
                String message = rs.getString("ErrorMessage");

                // Update the result list
                result.set(0, String.valueOf(idProofTypeId));
                result.set(1, "SaveStatus: " + saveStatus);
                result.set(2, "ErrorMessage: " + message);
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in getIDProofTypeIdByName: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return result;
    }

    public List<String> addNewVisitorEntry(
            String compId, int sbuId, int locationId, int gateId, String mobileNo,
            String visitorName, String visitorPlace, String visitorDesignation, String visitorCompany,
            int visitorTypeId, String purpose, String visitingFaculty, String approverName,
            int visitingAreaId, String refMail, String asset1, String asset2, String asset3,
            String asset4, String asset5, String securityName, String securityId,
            String photoFilePath, String photoFileName, int idProofType, String idProofNo,
            String idProofFilePath, String idProofFileName, String approvalMailFilePath,
            String approvalMailFileName, int userId) throws SQLException {

        Connection conn=getConnection();
        List<String> result = new ArrayList<>();
        result.add("Id : null");

        String spString = "{call [dbo].[SP_addNewVisitorEntry] (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";
        CallableStatement sp=null;
        ResultSet rs = null;
        try
        {
            sp = conn.prepareCall(spString);
            sp.setString(1, compId);
            sp.setInt(2, sbuId);
            sp.setInt(3, locationId);
            sp.setInt(4, gateId);
            sp.setString(5, mobileNo);
            sp.setString(6, visitorName);
            sp.setString(7, visitorPlace);
            sp.setString(8, visitorDesignation);
            sp.setString(9, visitorCompany);
            sp.setInt(10, visitorTypeId);
            sp.setString(11, purpose);
            sp.setString(12, visitingFaculty);
            sp.setString(13, approverName);
            sp.setInt(14, visitingAreaId);
            sp.setString(15, refMail);
            sp.setString(16, asset1);
            sp.setString(17, asset2);
            sp.setString(18, asset3);
            sp.setString(19, asset4);
            sp.setString(20, asset5);
            sp.setString(21, securityName);
            sp.setString(22, securityId);
            sp.setString(23, photoFilePath);
            sp.setString(24, photoFileName);
            sp.setInt(25, idProofType);
            sp.setString(26, idProofNo);
            sp.setString(27, idProofFilePath);
            sp.setString(28, idProofFileName);
            sp.setString(29, approvalMailFilePath);
            sp.setString(30, approvalMailFileName);
            sp.setInt(31,userId);

            rs=sp.executeQuery();
            if (rs.next())
            {
                long id = rs.getLong("ID");
                result.set(0, String.valueOf(id));
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in addNewVisitor: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (sp != null) sp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }
        return result;
    }

    public long getHighestUniqueId()
    {
        // If the mobile number does not exist, generate a new unique ID
        Statement stmt = null;
        ResultSet rs = null;
        long newUniqueId = 0;

        try {
            conn = getConnection();
            String query = "SELECT ISNULL(MAX(CAST(UniqueId AS BIGINT)), 0) + 1 AS NewUniqueId FROM Visitor_Entry";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                newUniqueId = rs.getLong("NewUniqueId");
            }
        } catch (SQLException e) {
            Log.e("DatabaseHelperSQL", "Error in getUniqueId: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelperSQL", "Error closing resources: " + e.getMessage());
            }
        }

        return newUniqueId;
    }
}


