package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

/**
 * A singleton list of users used to store user data locally so that all activities have access.
 */
public class RatingList {
    private static RatingList ourInstance = null;

    private ArrayList<Rating> ratings;

    private RatingList() {
        ratings = new ArrayList<>();
    }

    public static RatingList getInstance() {
        if (ourInstance == null) {
            ourInstance = new RatingList();
        }
        return ourInstance;
    }


    public ArrayList<Rating> getRatings() {
        return this.ratings;
    }

    /**
     * Adds rating to this list of ratings.
     * @param m is rating to be added.
     */
    public void addRating(Rating m) {
        this.ratings.add(m);
    }

    /**
     * Removes rating from this list of ratings.
     * @param m is rating to be removed.
     */
    public void removeRating(Rating m) {
        this.ratings.remove(m);
    }

    /**
     * Gets average rating for a movie from all user ratings.
     * @param identifier the movie identifier.
     * @return the average rating
     */
    public String getRating(String identifier) {
        double rating = 0;
        int counter = 0;
        for (Rating r : this.ratings) {
            if (r.getIdentifier().equals(identifier)) {
                counter++;
                rating = ((r.getRating()) + rating) / counter;
            }
        }
        return "Average User Rating: " + Double.toString(rating);
    }

    /**
     * Gets average rating for a movie from ratings from users of a specified major.
     * @param identifier the movie identifier.
     * @param major the major.
     * @return the average rating
     */
    public String getRatingByMajor(String identifier, String major) {
        double rating = 0;
        int counter = 0;
        for (Rating r : this.ratings) {
            if (r.getIdentifier().equals(identifier) && r.getMajor().equals(major)) {
                counter++;
                rating = ((r.getRating()) + rating) / counter;
            }
        }
        return "User Rating: " + Double.toString(rating);
    }
}
