package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

/**
 * A singleton list of users used to store user data locally so that all activities have access.
 */
public class RatingList {
    private static RatingList ourInstance = null;

    private ArrayList<Rating> ratings;

    private Set<Movie> movies;

    private RatingList() {
        ratings = new ArrayList<>();
        movies = new HashSet<>();
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

    public void addMovie(Movie m) {
        this.movies.add(m);
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
        for (Rating r : this.getInstance().getRatings()) {
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
        return "Average User Rating: " + Double.toString(rating);
    }

    public ArrayList<Movie> getTopMovies(String filter, String parameter) {
        if (filter.equals("null")) {
            ArrayList<Movie> out = new ArrayList<>();
            for (Movie m : this.movies) {
                double rating = 0;
                int counter = 0;
                for (Rating r : this.ratings) {
                    if (r.getIdentifier().equals(m)) {
                        counter++;
                        rating = ((r.getRating()) + rating) / counter;
                    }
                }
                m.setUserScore(rating);
                out.add(m);
            }
            Collections.sort(out);
            return out;
        } else if (filter.equals("major")) {
            ArrayList<Movie> out = new ArrayList<>();
            for (Movie m : this.movies) {
                double rating = 0;
                int counter = 0;
                for (Rating r : this.ratings) {
                    if (r.getIdentifier().equals(m.toString()) && r.getMajor().equals(parameter)) {
                        counter++;
                        rating = ((r.getRating()) + rating) / counter;
                    }
                }
                m.setUserScore(rating);
                out.add(m);
            }
            Collections.sort(out);
            return out;
        }
        return null;
    }


}
