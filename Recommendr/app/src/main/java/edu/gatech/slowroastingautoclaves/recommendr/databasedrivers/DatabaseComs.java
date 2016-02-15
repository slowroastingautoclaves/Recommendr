package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;

/**
 * Created by Joshua Jibilian on 2/14/2016.
 *
 * Uses DBdriver class and SSHDriver class to facilitate connections to the Database and
 * querries to the database.
 */
public class DatabaseComs {
    /**
     * Driver to connect to database.
     */
    private static DBdriver db;
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
        db = new DBdriver();
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
     * Used to register a user in the database
     * @param userName Username wanting to be registered
     * @param password password for new user
     * @param eMail E-Mail of new User
     * @return True if user is registered, else false
     */
    public boolean registerUser(String userName, String password, String eMail){
        int results;
        dbConnect();
        results = db.sendUpdate(String.format("INSERT INTO users VALUES ('%s', '%s', '%s');",
                userName, eMail, password ));
        closeDBComs();
        if (results == 1){
            return true;
        }
        return false;
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
