package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;

/**
 * Created by Joshua Jibilian on 2/14/2016.
 *
 * Uses DBDriver class and SSHDriver class to facilitate connections to the Database and
 * querries to the database.
 */
public class DatabaseComs {
    /**
     * Driver to connect to database.
     */
    private static DBDriver db;
    /**
     * Driver to connect to server via SSH
     */
    private static SSHDriver sshTunnel;

    /**
     * Connects to server via SSH tunnel
     */
    public void connectToServer(){
        sshTunnel = new SSHDriver();
        sshTunnel.connectViaSSH();
    }

    /**
     * Connects to database, tunnel must be open first
     */
    private void dbConnect(){
        db = new DBDriver();
    }

    /**
     * Closes connection to database.
     */
    private void closeDBComs()
    {
        db.closeConnection();
    }

    /**
     * Closes SSH tunnel
     */
    public void closeSSHComs(){
        sshTunnel.closeSSHConnection();
    }

    /**
     * Closes connection to database and SSH Tunnel in proper order.
     */
    public void closeAll(){
        db.closeConnection();
        sshTunnel.closeSSHConnection();
    }

    /**
     * Checks for user to log them in
     * @param userName persons username that is attempting to log in
     * @param password Password of person trying to log in
     * @return returns true if they are in database, else false
     */
    public boolean logInUser(String userName, String password){
        int results;
        dbConnect();
        results = db.sendUpdate(String.format("SELECT * FROM recommendr WHERE UName = '%s' AND password = '%s';", userName, password ));
        closeDBComs();
        if (results == 1){
            return true;
        }
        return false;
    }
}
