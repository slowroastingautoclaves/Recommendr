package edu.gatech.slowroastingautoclaves.recommendr.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user that has a username, an email, a password, and a condition.
 */
public class User implements Serializable {
    private String username, email, password, major, description;
    private Condition condition;
    private boolean adminStatus;
    //private Condition condition;
    private ArrayList<Rating> userRatings;

    // constructor for creating new user

    /**
     * Constructor for a User object.
     * @param username is username.
     * @param email is email.
     * @param password is password.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRatings = new ArrayList<>();
        this.adminStatus = false;
        this.condition = Condition.UNLOCKED;
    }

    /**
     * Constructor for a User object.
     * @param username is username.
     * @param email is email.
     * @param password is password.
     * @param condition is condition (banned, locked, unlocked).
     */
    public User(String username, String email, String password, Condition condition) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.condition = condition;
        this.userRatings = new ArrayList<>();
        this.adminStatus = false;
    }

    /**
     * Constructor for a User object.
     * @param username is username.
     * @param email is email.
     * @param password is password.
     * @param condition is condition (banned, locked, unlocked).
     * @param adminStatus is admin status.
     */
    public User(String username, String email, String password, Condition condition, boolean adminStatus) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.condition = condition;
        this.userRatings = new ArrayList<>();
        this.adminStatus = adminStatus;
    }

    /**
     * Gets this user's username.
     * @return the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets this user's email.
     * @return the email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets this user's condition
     * @return the condition
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * sets the user's condition
     * @param condition is the condition to set.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     * check admin status of user
     * @return the admin status of the user
     */
    public boolean getAdminStatus() {
        return adminStatus;
    }

    /**
     * change the admin status of the user
     * @param adminStatus new admin status
     */
    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }
    /**
     * Gets this user's password.
     * @return the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets this user's major.
     * @param major is the new major that will be set.
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * Gets this user's major.
     * @return the major.
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * Sets this user's description.
     * @param description is the new description that will be set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets this user's description.
     * @return the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Adds a rating.
     * @param r is rating to add.
     */
    public void addRating(Rating r) {
        this.userRatings.add(r);
    }

    /**
     * Removes a rating.
     * @param r is rating to remove.
     */
    public void removeRating(Rating r) {
        this.userRatings.remove(r);
    }

    /**
     * Gets all the ratings for this user.
     * @return an ArrayList<Rating> object.
     */
    public ArrayList<Rating> getRatings() {
        return this.userRatings;
    }

    /**
     * Gets this user's rating for a movie.
     * @param identifier is movie identifier.
     * @return a String representing this User's rating.
     */
    public String getRating(String identifier) {
        for (Rating r : this.userRatings) {
            if (r.getIdentifier().equals(identifier)) {
                return "Your Rating: " + Double.toString(r.getRating());
            }
        }
        return "Your Rating: No rating found";
    }

    /**
     * Gets the user's condition
     * @return the condition of the user
     */
//    public Condition getCondition() {
//        return this.condition;
//    }

    /*
     * Set condition for the user
     */
//    public void setCondition(Condition condition) {
//        this.condition = condition;
//    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        //Users are equal if they have the same email.
        User compare = (User) o;
        return compare.getEmail().equals(this.getEmail());
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }
}
