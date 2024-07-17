package com.example.vmsv1.db;

public class DefaultValues {
    final static String connectionUrl = "jdbc:jtds:sqlserver://SQL6032.site4now.net:1433/db_aaabe8_vms;user=db_aaabe8_vms_admin;password=password123;";
    final static String CompID = "MTL";



    public String getCompanyID(){
        return CompID;
    }

    public String getConnectionUrl(){
        return connectionUrl;
    }
}
