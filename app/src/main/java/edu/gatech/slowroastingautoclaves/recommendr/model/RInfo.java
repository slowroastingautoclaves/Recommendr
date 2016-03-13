package edu.gatech.slowroastingautoclaves.recommendr.model;

/**
 * Created by Blaze on 3/7/2016.
 */
public interface RInfo {
    /**
     * Connect or initiate data structure
     */
    void start();

    /**
     * Call on app close or hibernate?
     */
    void complete();

    /**
     * Used to register a user in the database
     * @param userName Username wanting to be registered
     * @param password password for new user
     * @param eMail E-Mail of new User
     * @return True if user is registered, else false
     */
    boolean  registerUser(String userName, String password, String eMail);

    /**
     * Checks for user to log them in
     * @param userName persons username that is attempting to log in
     * @param password Password of person trying to log in
     * @return True if they are in database, else false
     */
    boolean logInUser(String userName, String password);

    /**
     * Creates profile for a user
     * @param userName User to create profile for
     * @param major users selected major
     * @return True if profile is added else false.
     */
    boolean createProfile(String userName, String major);

    /**
     * Gets the profile of specific user
     * @param userName username of desired profile
     * @return ResultSet of the users profile
     */
    String getProfile(String userName);

    /**
     * Updates a profile of a specific user
     * @param userName user to update profile for.
     * @param major new major they want to update to.
     * @return True if update was successful, else false
     */
    boolean updateProfile(String userName, String major);
}
