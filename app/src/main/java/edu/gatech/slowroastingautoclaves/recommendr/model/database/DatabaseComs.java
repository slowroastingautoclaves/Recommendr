package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import android.content.pm.ResolveInfo;
import android.util.Log;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import edu.gatech.slowroastingautoclaves.recommendr.model.Condition;
import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Movies;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.presenter.MovieInterface;

import edu.gatech.slowroastingautoclaves.recommendr.presenter.UserInterface;

import edu.gatech.slowroastingautoclaves.recommendr.model.database.databasedrivers.DBdriver;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.databasedrivers.SSHDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Joshua Jibilian on 2/14/2016.
 *
 * Uses DBdriver class and SSHDriver class to facilitate connections to the Database and
 * querries to the database.
 */
public class DatabaseComs implements Executor, MovieInterface, UserInterface{
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
    public void start() {
        if (sshTunnel == null){
            sshTunnel = new SSHDriver();
            execute(sshTunnel);

        } else if (!sshTunnel.isConnected()) {
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
    private void closeSSHComs(){
        sshTunnel.closeSSHConnection();
    }

    public void complete() {
        closeSSHComs();
    }

    /**
     * Closes connection to database and SSH Tunnel in proper order.
     */
    private void closeAll(){
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
                "WHERE UName = '%s';", major, userName), 1);
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

    @Override
    public List<User> getUsers() {
        start();
        String querryS = String.format("SELECT * FROM ((SELECT ev.UName, ev.Email, " +
                "ev.password, ev.major, pen.type FROM((SELECT peep.UName, peep.Email, " +
                "peep.Password, p.major FROM (SELECT * FROM users) as peep LEFT JOIN profile as " +
                "p ON p.UName = peep.UName) as ev LEFT JOIN penalties as pen ON pen.UName = " +
                "ev.UName)) UNION (SELECT UName, Email, pass as password, null as major, 'A' as " +
                "type FROM ADMINS)) as big;");
        db.setQuery(querryS,0);
        dbConnect();
        ResultSet results = db.getResultSet();
        User toAdd = null;
        String uName;
        String email;
        String psswrd;
        String major;
        Boolean isAdmin = false;
        Condition con = Condition.UNLOCKED;
        ArrayList<User> toReturn = new ArrayList<>();
        try {
            while(results.next()) {
                uName = results.getString(1);
                email = results.getString(2);
                psswrd = results.getString(3);
                major = results.getString(4);
                String penalties = results.getString(5);
                //System.out.println(uName + " " + email + " " + psswrd + " " + major + " " +penalties);
                if(penalties == null) {
                    con = Condition.UNLOCKED;
                } else if (penalties.equals("A")){
                    isAdmin = true;
                    con = Condition.UNLOCKED;
                } else if(penalties.equals("L")){
                    con = Condition.LOCKED;
                } else {
                    con = Condition.BANNED;
                }
                toAdd= new User(uName,email,psswrd,con, isAdmin);
                toReturn.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Movie getMovie(String identifier) {
        Pattern p;
        p = Pattern.compile("(.+)(?:[ ][\\(])([0-9]+)");
        Matcher m = p.matcher(identifier);
        m.find();
        String movie = m.group(1);
        String year = m.group(2);

        Movie toReturn = new Movie();

        String querryS = String.format("SELECT description, " +
                "(SELECT AVG(rating) FROM MovieRatings " +
                "WHERE Movie = '%s' " +
                "and MovieYear = '%s') as rating " +
                "FROM RatedMovies WHERE Movie = '%s' " +
                "and MovieYear = '%s';", movie, year, movie, year);

        db.setQuery(querryS, 0);
        dbConnect();

        ResultSet results = db.getResultSet();
        try {
            if(results.next()){
                toReturn.setYear(year);
                toReturn.setTitle(movie);
                toReturn.setDescription(results.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBComs();

        return toReturn;
    }

    @Override
    public void addMovie(Movie m) {
        start();

        String updateS = String. format("INSERT INTO RatedMovies"
                + " VALUES ('%s', '%s');", m.getTitle(), m.getYear());
        db.setQuery(updateS, 1);
        dbConnect();

        closeAll();

    }

    @Override
    public void addRating(Rating r) {
        start();

        String updateS = String.format("INSERT INTO MovieRatings VALUES('%s', '%s', %.2f );", r.getIdentifier(),
                r.getUserName(), r.getRating());

        System.out.println(updateS);

        db.setQuery(updateS, 1);
        dbConnect();
        closeAll();
    }

    @Override
    public void removeRating(Rating r) {
        start();

        String updateS = String.format("DELETE FROM MovieRatings WHERE Movie = '%s' AND ratedBy = '%s';", r.getIdentifier(),
                r.getUserName());
        db.setQuery(updateS,1);
        dbConnect();
        closeAll();

    }

    @Override
    public String getMovieRating(String identifier) {
        start();

        String updateS = String.format("SELECT AVG(rating) FROM MovieRatings WHERE Movie = '%s';", identifier);

        db.setQuery(updateS, 0);

        dbConnect();

        ResultSet results = db.getResultSet();
        String toReturn = "";
        try {
            if(results.next()) {
                toReturn = Float.toString(results.getFloat(1));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;


    }

    @Override
    public List<Movie> getTopMovies(String filter, String parameter) {
        start();
        String querryS = "";
        if (filter.equals("null")){
            querryS = "select Movie, AVG(rating) as 'average' from " +
                    "MovieRatings GROUP BY Movie ORDER BY average DESC;";
            ;
        } else {
            querryS = String.format("SELECT z.Movie, avg(z.rating) as average," +
                    " z.description, z.MovieYear FROM ((SELECT * FROM profile " +
                    "WHERE major = '%s') as y inner join " +
                    "(SELECT MovieRatings.Movie, MovieRatings.RatedBy, MovieRatings.rating, " +
                    "RatedMovies.description, RatedMovies.MovieYear FROM RatedMovies " +
                    "INNER JOIN MovieRatings ON MovieRatings.Movie = RatedMovies.Movie)" +
                    "as z on y.UName = z.ratedBy) GROUP BY z.Movie ORDER BY average;", parameter);
        }

        db.setQuery(querryS, 0);
        dbConnect();
        Movies.clear();
        Movie toAdd;
        ArrayList<Movie> toReturn = new ArrayList<>();
        ResultSet results = db.getResultSet();
        try {
            while (results.next()){
                toAdd = new Movie();
                toAdd.setTitle(results.getString(1));
                System.out.println(Float.toString(results.getFloat(2)));
                toAdd.setRating(Float.toString(results.getFloat(2)));
                toAdd.setDescription(results.getString(3));
                toAdd.setYear(results.getString(4));
                toReturn.add(toAdd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public User getUser(String identifier) {
        start();
        String querryS = String.format("SELECT * FROM ((SELECT ev.UName, ev.Email, " +
                "ev.password, ev.major, pen.type FROM((SELECT peep.UName, peep.Email, " +
                "peep.Password, p.major FROM (SELECT * FROM users) as peep LEFT JOIN profile as " +
                "p ON p.UName = peep.UName) as ev LEFT JOIN penalties as pen ON pen.UName = " +
                "ev.UName)) UNION (SELECT UName, Email, pass as password, null as major, 'A' as " +
                "type FROM ADMINS)) as big WHERE UName = '%s';", identifier);
        db.setQuery(querryS,0);
        dbConnect();
        ResultSet results = db.getResultSet();
        User toReturn = null;
        String uName;
        String email;
        String psswrd;
        String major;
        Boolean isAdmin = false;
        Condition con = Condition.UNLOCKED;

        try {
            if(results.next()) {
                uName = results.getString(1);
                email = results.getString(2);
                psswrd = results.getString(3);
                major = results.getString(4);
                String penalties = results.getString(5);
                System.out.println(uName + " " + email + " " + psswrd + " " + major + " " +penalties);
                if(penalties == null) {
                    con = Condition.UNLOCKED;
                } else if (penalties.equals("A")){
                    isAdmin = true;
                } else if(penalties.equals("L")){
                    con = Condition.LOCKED;
                } else {
                    con = Condition.BANNED;
                }
                toReturn= new User(uName,email,psswrd,con, isAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;

    }

    @Override
    public void addUser(User u) {
        start();
        String querryS = String.format("INSERT INTO users VALUES ('%s', '%s', '%s')", u.getUsername(),
                u.getEmail(), u.getPassword());

        System.out.println(querryS);

        db.setQuery(querryS, 1);
        dbConnect();
        closeDBComs();

    }

    @Override
    public void removeUser(String identifier) {
        start();
        String querryS = String.format("DELETE FROM users WHERE UName = '%s';", identifier);
        db.setQuery(querryS, 1);
        dbConnect();
        closeDBComs();
    }

    @Override
    public void lock(String identifier) {
        User badPerson = getUser(identifier);
        if (badPerson.getCondition() != Condition.BANNED) {
            start();
            String querryS = String.format("INSERT INTO penalties VALUES ('%s', 'L');", identifier);
            db.setQuery(querryS, 1);
            dbConnect();
            closeDBComs();
        }
    }

    @Override
    public void ban(String identifier) {
        start();
        String querryS = String.format("INSERT INTO penalties VALUES ('%s', 'B');", identifier);
        db.setQuery(querryS, 1);
        dbConnect();
        int result = db.getIntResult();
        closeDBComs();
        if (result == 0){
             querryS = String.format("UPDATE penalties SET type = 'B' WHERE UName = '%s';", identifier);
            db.setQuery(querryS,1);
            dbConnect();
            closeDBComs();
        }
    }

    @Override
    public void unlock(String identifier) {
        User badPerson = getUser(identifier);
        if (badPerson.getCondition() != Condition.BANNED) {
        unban(identifier);

        }

    }

    @Override
    public void unban(String identifier) {
            start();
            String querryS = String.format("DELETE FROM penalties WHERE UName = '%s';", identifier);
            db.setQuery(querryS, 1);
            dbConnect();
            closeDBComs();
    }
}
