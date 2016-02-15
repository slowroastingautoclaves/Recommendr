package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;


import android.util.Log;

import java.sql.*;

/**
 * Facilitates connection to database
 */
public class DBdriver {

		
    private Connection con;		
    private final String DBNAME="SlowRoastingAuto";		
    private final String PASSWORD="cs2340team58";
    Statement statement;

    /**
     * Creates a driver to for connections to database
     */
    public DBdriver() {
        Log.i("DBdriver", "Creating Driver");
        con = null;
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();		
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommendr",
                    DBNAME,		
                    PASSWORD);		

            if(!con.isClosed()) {
                Log.i("DBdriver", "Successfully connected to MySQL server using TCP/IP...");
            }		
		
        } catch(Exception e) {		
            e.printStackTrace();
            Log.e("DBdriver", "Exception while creating connection: " + e.getMessage());
        }		
    }

    /**
     *  Closes the connection to the database
     */
    public void closeConnection() {		
        try{		
            con.close();
            if(con.isClosed()) {
                Log.i("DBdriver", "Connection successfully closed.");
            }		
            else {
                Log.e("DBdriver", "Failed to close connection.");
            }		
        } catch(Exception e) {		
            System.err.println("Exception while closing connection: ");
            Log.e("DBdriver", e.getMessage());
        }		
    }

    /**
     * sends query to database
     * @param query
     * @return ResultSet Detailed object of data from query
     */
    public ResultSet sendQuery(String query) {		
        ResultSet results;		
        try{		
            Statement st = con.createStatement();		
            results = st.executeQuery(query);		
        } catch(Exception e) {
            Log.e("DBdriver", "Exception while executing Query: " + e.getStackTrace());
            results = null;		
        }		
        return results;		
    }

    /**
     * Sends a query to update server
     * @param query
     * @return int number of rows affected
     */
    public int sendUpdate(String query) {		
        int results;		
        try{		
            Statement st = con.createStatement();		
            results = st.executeUpdate(query);		
        } catch(Exception e) {
            Log.e("DBdriver", "Exception while executing Query: " + e.getMessage());
            results = -1;		
        }		
        return results;		
    }		
		
 } 
