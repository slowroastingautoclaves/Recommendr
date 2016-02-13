package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;


import java.sql.*;

public class DBdriver {

		
    private Connection con;		
    private final String DBNAME="SlowRoastingAuto";		
    private final String PASSWORD="cs2340team58";
		
    public DBdriver() {
		
        System.out.println("Driver created. Lets Connect!");		
		
        con = null;		
		
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();		
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/recommendr",
                    DBNAME,		
                    PASSWORD);		

            if(!con.isClosed()) {		
                System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");		
            }		
		
        } catch(Exception e) {		
            e.printStackTrace();		
            System.err.println("Exception while creating connection: " + e.getMessage());		
        }		
    }		
		
    public void closeConnection() {		
        try{		
            con.close();
            SSHDriver.CloseSSHConnection();
            if(con.isClosed()) {		
                System.out.println("Connection successfully closed.");		
            }		
            else {		
                System.out.println("Failed to close connection.");		
            }		
        } catch(Exception e) {		
            System.err.println("Exception while closing connection: ");		
            e.printStackTrace();		
        }		
    }		
		
    public ResultSet sendQuery(String query) {		
        ResultSet results;		
        try{		
            Statement st = con.createStatement();		
            results = st.executeQuery(query);		
        } catch(Exception e) {		
            System.err.println("Exception while executing Query: " + e.getStackTrace());		
            results = null;		
        }		
        return results;		
    }		
		
    public int sendUpdate(String query) {		
        int results;		
        try{		
            Statement st = con.createStatement();		
            results = st.executeUpdate(query);		
        } catch(Exception e) {		
            System.err.println("Exception while executing Query: " + e.getMessage());		
            results = -1;		
        }		
        return results;		
    }		
		
 } 
