package edu.gatech.slowroastingautoclaves.recommendr.model;

/**
 * Represents a rating for a movie that tracks the user and major that made the rating.
 */
public class Rating {
    private String identifier, user, major;
    private double rating;

    public Rating(String identifier, String user, String major, double rating) {
        this.identifier = identifier;
        this.user = user;
        this.major = major;
        this.rating = rating;
    }

    /**
     * Getter for movie identifier.
     * @return the identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Getter for user major.
     * @return the major
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * Getter for movie rating.
     * @return the rating
     */
    public double getRating() {
        return this.rating;
    }

    /**
     * Getter for user identifier.
     * @return the user identifier
     */
    public String getUser() {
        return this.user;
    }

    @Override
    public boolean equals(Object o) {
        Rating compare = (Rating) o;
        if (compare.getUser().equals(this.user)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.user.hashCode();
    }
}
