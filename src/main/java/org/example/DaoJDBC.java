package org.example;
import org.example.utils.ConfProperties;
import org.example.utils.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DaoJDBC {
    private static final String host = ConfProperties.getProperty("host");
    private static final String DBName = ConfProperties.getProperty("DBName");
    private static final String url = "jdbc:mysql://" + host + ":3306/" + DBName;
    private static final String user = ConfProperties.getProperty("user");
    private static final String password = ConfProperties.getProperty("password");

    private static Connection con = null;
    private static Statement stmt = null;
    private static PreparedStatement preparedStmt = null;
    private static ResultSet rs = null;

    public static Connection connectToDB() {
        Log.info("Connect to DB " + url + " by user " + user);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            Log.info("Connection to DB successful!");
        } catch (ClassNotFoundException e) {
            Log.error(e.getMessage());
        } catch (SQLException sqlEx) {
            Log.error("Connection to DB failed!\n" + sqlEx.getMessage());
        }

        return con;
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                Log.info("Connection to DB closed successfully");
            } catch (SQLException se) {
                Log.error("Connection to DB was not closed. Reason:\n" + se.getMessage());
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
                Log.info("Statement closed successfully");
            } catch (SQLException se) {
                Log.error("Statement was not closed. Reason:\n" + se.getMessage());
            }
        }

        if (rs != null) {
            try {
                rs.close();
                Log.info("ResultSet closed successfully");
            } catch (SQLException se) {
                Log.error("ResultSet was not closed. Reason:\n" + se.getMessage());
            }
        }
    }

    public static void createTable(String query) {
        try {
            stmt = connectToDB().createStatement();
            Log.info("Send request to DB: " + query);
            stmt.executeUpdate(query);
            Log.info("Table was created successfully");
        } catch (SQLException se) {
            Log.error("Table was not created. Reason:\n" + se.getMessage());
        }
    }

    public static void dropTable(String tableName) {
        String query = "DROP TABLE " + tableName;
        try {
            stmt = connectToDB().prepareStatement(query);
            Log.info("Send request to DB: " + query);
            stmt.executeUpdate(query);
            Log.info("Table was deleted successfully");
        } catch (SQLException se) {
            Log.error("Table was not deleted. Reason:\n" + se.getMessage());
        }
    }

    public static ResultSet selectFromTable(String query) {
        try {
            stmt = connectToDB().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Log.info("Send request to DB: " + query);
            rs = stmt.executeQuery(query);
            rs.next();
        } catch (SQLException se) {
            Log.error(se.getMessage());
        }
        return rs;
    }

    public static ResultSet selectFromTable(String query, String param) {
        try {
            preparedStmt = connectToDB().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStmt.setString(1, param);
            Log.info("Send request to DB: " + query);
            preparedStmt.execute();
            rs = preparedStmt.getResultSet();
            rs.next();
        } catch (SQLException se) {
            Log.error(se.getMessage());
        }
        return rs;
    }

    public static void insertIntoTable(String query) {
        try {
            stmt = connectToDB().createStatement();
            Log.info("Send request to DB: " + query);
            stmt.executeUpdate(query);
            Log.info("New data was inserted into table successfully");
        } catch (SQLException se) {
            Log.error("New data was not inserted into table. Reason:\n" + se.getMessage());
        }
    }

    public static void updateInTable(String query) {
        try {
            stmt = connectToDB().createStatement();
            Log.info("Send request to DB: " + query);
            stmt.executeUpdate(query);
            Log.info("Data in table was updated successfully");
        } catch (SQLException se) {
            Log.error("Data in table was not updated. Reason:\n" + se.getMessage());
        }
    }

    public static void deleteFromTable(String query) {
        try {
            Log.info("Send request to DB: " + query);
            stmt = connectToDB().createStatement();
            stmt.executeUpdate(query);
            Log.info("Data from table was deleted successfully");
        } catch (SQLException se) {
            Log.error("Data from table was not deleted. Reason:\n" + se.getMessage());
        }
    }

    public static boolean isTableNameExist(String tableName){
        boolean result = false;
        List<String> nameTables = new ArrayList<>();
        try {
            Log.info("Send request to DB: Show tables");
            ResultSet rs = stmt.executeQuery("Show tables");
            while(rs.next()) {
                nameTables.add(rs.getString(1));
            }

        } catch (SQLException se) {
            Log.error("Reason:\n" + se.getMessage());
        }
        result = nameTables.contains(tableName);
        return result;
    }

    public static boolean isEmptyTable(String tableName){
        boolean result = false;
        try {
            Log.info("Send request to DB: Show tables");
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            if(!rs.next()) {
                result = true;
            }
        } catch (SQLException se) {
            Log.error("Reason:\n" + se.getMessage());
        }
        return result;
    }

}
