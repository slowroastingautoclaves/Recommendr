package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;

import android.provider.ContactsContract;
import android.util.Log;

import java.sql.ResultSet;
import java.util.concurrent.Executor;

/**
 * Created by Joshua Jibilian on 2/14/2016.
 *
 * Uses DBdriver class and SSHDriver class to facilitate connections to the Database and
 * querries to the database.
 */
public class DatabaseComs implements Executor{
    Thread thread;
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
    public DatabaseComs() {
        db = new DBdriver();
    }

    /**
     * Connects to server hosting database.
     */
    public void connectToServer(){
        if (sshTunnel == null){
            sshTunnel = new SSHDriver();
            execute(sshTunnel);

        }else if (!sshTunnel.isConnected()) {
            execute(sshTunnel);
        }
    }

    /**
     * Connects to database, tunnel must be open first
     */
    private void dbStartConnect(){
        execute(db);
    }
    private void dbConnect(){
        dbStartConnect();
        try {
            thread.join();
        } catch (Exception e){
            Log.e("DatabaseComs", e.getMessage());
        }
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
    public synchronized boolean registerUser(String userName, String password, String eMail){

        db.setQuery(String.format("INSERT INTO users VALUES ('%s', '%s', '%s');",
                userName, eMail, password), 1);
        dbConnect();
        //results = db.sendUpdate();
        closeDBComs();
        if (db.getIntResult() == 1){
            return true;
        }
        return false;
    }

    /**
     * Checks for user to log them in
     * @param userName persons username that is attempting to log in
     * @param password Password of person trying to log in
     * @return True if they are in database, else false
     */
    public boolean logInUser(String userName, String password){

        db.setQuery(String.format("SELECT Count(UName)FROM users WHERE UName = '%s' AND Password = '%s';", userName, password), 0);
        dbConnect();
        //results = db.sendUpdate();

        ResultSet r  = db.getResultSet();
        String y = "HAHAHAHa   ";
        /*try {
            y="";
            y = r.getNString(1);
        }catch (Exception e){
            Log.e("DatabaseComs", "FML");
        }*/
        Log.d("DatabaseComs", "" + db.getFetchedSize() + " " + y);
        ResultSet result = db.getResultSet();
        try {
            result.next();
            if (result.getInt(1) == 1){
                closeDBComs();
                return true;
            }
            closeDBComs();
            return false;
        } catch (Exception e){
            closeDBComs();
            Log.e("DatabaseComs", "Error with result Set" + e.getMessage() + " \n" + Log.getStackTraceString(new Exception()));

        }
        return false;

    }

    /**
     * Creates profile for a user
     * @param userName User to create profile for
     * @param major users selected major
     * @return True if profile is added else false.
     */
    public boolean createProfile(String userName, String major){

        db.setQuery(String.format("INSERT INTO profile VALUES('%s','%s');", userName, major), 1);
        dbConnect();
        //results = db.sendUpdate();
        closeDBComs();
        if (db.getIntResult() == 1){
            return true;
        }
        return false;
    }

    /**
     * Gets the profile of specific user
     * @param userName username of desired profile
     * @return ResultSet of the users profile
     */
    public String getProfile(String userName){
        String toReturn = "";
        ResultSet re;
        db.setQuery(String.format("SELECT UName,major FROM profile WHERE UName = '%s';",
                userName), 0);
        dbConnect();
        re = db.getResultSet();
        try {
            if(re.next()){
                toReturn = re.getString("major");
            }

        } catch (Exception e) {
            Log.e("DBCOMS", " " + e.getMessage() +Log.getStackTraceString(new Exception()));
        }

        closeDBComs();
        return toReturn;
    }

    /**
     * Updates a profile of a specific user
     * @param userName user to update profile for.
     * @param major new major they want to update to.
     * @return True if update was successful, else false
     */
    public boolean updateProfile(String userName, String major){
        db.setQuery(String.format("UPDATE profile " +
                "SET major = '%s' " +
                "WHERE UName = '%s';", major,userName), 1);
        dbConnect();
        //results = db.sendUpdate();
        closeDBComs();
        if (db.getIntResult() == 1){

            return true;
        }
        return false;
    }

    /**\
     * Starts a command on new thread
     * @param command Command to start on new thread
     */
    @Override
    public void execute(Runnable command) {
        thread = new Thread(command);
        thread.start();
    }
}
