package edu.gatech.slowroastingautoclaves.recommendr.model;

import java.util.ArrayList;

/**
 * Represents a user that has a username, an email, and a password.
 */
public class User {
    private String username, email, password, major, description;
    private ArrayList<Rating> userRatings;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRatings = new ArrayList<>();
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

    public void addRating(Rating r) {
        this.userRatings.add(r);
    }

    public void removeRating(Rating r) {
        this.userRatings.remove(r);
    }

    public ArrayList<Rating> getRatings() {
        return this.userRatings;
    }

    public String getRating(String identifier) {
        for (Rating r : this.userRatings) {
            if (r.getIdentifier().equals(identifier)) {
                return "Your Rating: " + Double.toString(r.getRating());
            }
        }
        return "Your Rating: No rating found";
    }

    @Override
    public boolean equals(Object o) {
        //Users are equal if they have the same email.
        User compare = (User) o;
        if (compare.getEmail().equals(this.getEmail())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }
}
