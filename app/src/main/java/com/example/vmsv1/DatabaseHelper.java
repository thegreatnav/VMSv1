package com.example.vmsv1;

import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHelper extends AppCompatActivity {

    // Use 10.0.2.2 instead of localhost for Android emulator
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=master";
    private static final String USER = "sa";
    private static final String PASSWORD = "vms";

    static ArrayList<ArrayList<String>> result = new ArrayList<>();

    public static ArrayList<ArrayList<String>> connect() {
        // Allow network operation on main thread for demonstration purposes
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            String query = "SELECT [UserId], [Username], [Password], [FullName] FROM [master].[dbo].[User_Master] WHERE UserId=1";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String result_userid = String.valueOf(resultSet.getInt("UserId"));
                String result_username = resultSet.getString("Username");
                String result_password = resultSet.getString("Password");
                ArrayList<String> row = new ArrayList<>();
                row.add(result_userid);
                row.add(result_username);
                row.add(result_password);
                result.add(row);
            }
            Log.i("DatabaseHelper", "Connected to the database");

        } catch (ClassNotFoundException e) {
            Log.e("DatabaseHelper", "SQLServerDriver class not found", e);
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "SQLException occurred", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e("DatabaseHelper", "Error closing resources", e);
            }
        }
        return result;
    }
}
