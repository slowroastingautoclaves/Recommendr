package edu.gatech.slowroastingautoclaves.recommendr.model;

/**
 * Created by Blaze on 3/7/2016.
 */
public interface RInfo {

    void start();
    void complete();
    boolean  registerUser(String userName, String password, String eMail);
    boolean logInUser(String userName, String password);
    boolean createProfile(String userName, String major);
    String getProfile(String userName);
    boolean updateProfile(String userName, String major);
}
