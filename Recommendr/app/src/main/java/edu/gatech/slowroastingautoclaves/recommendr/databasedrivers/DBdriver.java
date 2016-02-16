package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;


import android.app.Activity;
import android.util.Log;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.sql.*;

/**
 * Facilitates connection to database
 */
public class DBdriver implements Runnable {


    private static Connection con;
    private final String DBNAME="SlowRoastingAuto";
    private final String PASSWORD="cs2340team58";
    private String query;
    private int result;
    private ResultSet resultSet;


    /**
     * Creates a driver to for connections to database
     */
    public void connectDBdriver(){
        Log.i("DBdriver", "Creating Driver");
        con = null;
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/recommendr",
                    DBNAME,
                    PASSWORD);
            Log.i("DBdriver", "Hello");
            if(!con.isClosed()) {
                Log.i("DBdriver", "Successfully connected to MySQL server using TCP/IP...");

            }

            Statement statement = con.createStatement();
            if (statement.execute(query)){
                resultSet = statement.getResultSet();
            } else {
                result = statement.getUpdateCount();
            }

            query = null;


        } catch (Exception e) {
            Log.e("DBdriver", "Error thrown while connecting or during quarry" + e.getMessage()
                    + Log.getStackTraceString(new Exception()));
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
    public int getIntResult() {
        return result;
    }
    public void setQuery(String query){
        this.query = query;

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
    public void commit(){
        try {
            con.commit();
        } catch (Exception e) {
            Log.e("DBDriver", e.getMessage());
        }
    }
    public boolean isConnected(){
        try {
            return !con.isClosed();
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void run() {

        connectDBdriver();

    }

   /*//**
     * sends query to database
     * @param query
     * @return ResultSet Detailed object of data from query
     *//*
    public ResultSet sendQuery(String query) {
        ResultSet results;
        try{
            Statement st = con.createStatement();
            results = st.executeQuery(query);
        } catch(Exception e) {
            Log.e("DBdriver", "Exception while executing Query: " + Log.getStackTraceString(new Exception()));
            results = null;
        }
        return results;
    }

    //**
     * Sends a query to update server
     * @param query
     * @return int number of rows affected
     *//*
    public int sendUpdate(String query) {
        int results;
        try{
            Statement st = con.createStatement();
            st.execute(query);
            results = st.executeUpdate(query);
        } catch(Exception e) {
            Log.e("DBdriver", "Exception while executing Query: " + Log.getStackTraceString(new Exception()));
            results = -1;
        }
        return results;
    }*/

}
