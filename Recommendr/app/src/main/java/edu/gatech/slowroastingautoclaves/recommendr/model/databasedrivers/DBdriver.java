package edu.gatech.slowroastingautoclaves.recommendr.model.databasedrivers;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


import java.sql.*;
//TODO: volley
/**
 * Facilitates connection to database
 */
public class DBdriver implements Runnable {


    private Connection con;
    private final String DBNAME="SlowRoastingAuto";
    private final String PASSWORD="cs2340team58";
    private String query;
    private static int result;
    private int fetchedSize;
    private ResultSet resultSet;
    private Statement statement;
    private int op;


    /**
     * Creates a driver to for connections to database
     */
    public void connectDBdriver(int opt){
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


            try{
                if (opt == 0) {

                    resultSet = sendQuery(query);
                }
            } catch (Exception e){

            }
            try{
                if (opt == 1) {
                    result = sendUpdate(query);
                }
            } catch (Exception e){

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
    public int getFetchedSize() {return fetchedSize;
    }
    public void setQuery(String query, int op){
        this.query = query;
        this.op = op;

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

        connectDBdriver(op);

    }

    /**
     * sends query to database
     * @param query
     * @return ResultSet Detailed object of data from query**/

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

    /**
     * Sends a query to update server
     * @param query
     * @return int number of rows affected**/

    public int sendUpdate(String query) {
        int results;
        try{
            Statement st = con.createStatement();
            results = st.executeUpdate(query);
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("DBdriver", "Exception while executing Query: " + Log.getStackTraceString(new Exception()));
            results = -1;
        }
        return results;
    }

}
