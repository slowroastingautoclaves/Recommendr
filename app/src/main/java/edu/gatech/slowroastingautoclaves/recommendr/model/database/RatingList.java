package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public static final String RATINGS = "ratings.bin";
    public static final String MOVIES = "movies.bin";

    private static RatingList ourInstance = null;

    private ArrayList<Rating> ratings;

    private Set<Movie> movies;

    private RatingList() {
        ratings = null;
        movies = null;
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

    /**
     * Gets the top movies sorted by user ratings (ratings from the Recommendr system only).
     * @param filter is filter to use
     * @param parameter is filter parameter
     * @return an ArrayList<Movie> that is sorted by ratings
     */
    public ArrayList<Movie> getTopMovies(String filter, String parameter) {
        if (filter.equals("null")) {
            ArrayList<Movie> out = new ArrayList<>();
            for (Movie m : this.movies) {
                double rating = 0;
                int counter = 0;
                for (Rating r : this.ratings) {
                    if (r.getIdentifier().equals(m.toString())) {
                        counter++;
                        rating = ((r.getRating()) + rating) / counter;
                    }
                }
                m.setUserScore(rating);
                out.add(m);
            }
            if (out.size() == 0) {
                return null;
            }
            Collections.sort(out);
            return out;
        } else if (filter.equals("major")) {
            ArrayList<Movie> out = new ArrayList<>();
            for (Movie m : this.movies) {
                double rating = 0;
                boolean valid = false;
                int counter = 0;
                for (Rating r : this.ratings) {
                    if (r.getIdentifier().equals(m.toString()) && r.getMajor().equals(parameter)) {
                        valid = true;
                        counter++;
                        rating = ((r.getRating()) + rating) / counter;
                    }
                }
                m.setUserScore(rating);
                if (valid) {
                    out.add(m);
                }
            }
            if (out.size() == 0) {
                return null;
            }
            Collections.sort(out);
            return out;
        }
        return null;
    }

    public boolean loadRatings(File ratings, File movies) {
        boolean success = true;
        try {
            /*
              To read, we must use the ObjectInputStream since we want to read our model in with
              a single read.
             */
            // assuming we saved our top level object, we read it back in with one line of code.
            if (this.ratings == null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(ratings));
                this.ratings = (ArrayList<Rating>) in.readObject();
                in.close();
            }
            if (this.movies == null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(movies));
                this.movies = (Set<Movie>) in.readObject();
                in.close();
            }
        } catch (IOException e) {
            Log.e("RatingList", "Error reading an entry from binary file");
            success = false;
        } catch (ClassNotFoundException e) {
            Log.e("RatingList", "Error casting a class from the binary file");
            success = false;
        }

        return success;
    }

    public boolean saveRatings(File ratings, File movies) {
        boolean success = true;
        try {
            /*
               For binary, we use Serialization, so everything we write has to implement
               the Serializable interface.  Fortunately all the collection classes and APi classes
               that we might use are already Serializable.  You just have to make sure your
               classes implement Serializable.

               We have to use an ObjectOutputStream to write objects.

               One thing to be careful of:  You cannot serialize static data.
             */


            ObjectOutputStream ratingsOut = new ObjectOutputStream(new FileOutputStream(ratings));
            // We basically can save our entire data model with one write, since this will follow
            // all the links and pointers to save everything.  Just save the top level object.
            ratingsOut.writeObject(this.ratings);
            ratingsOut.close();

            ObjectOutputStream moviesOut = new ObjectOutputStream(new FileOutputStream(movies));
            // We basically can save our entire data model with one write, since this will follow
            // all the links and pointers to save everything.  Just save the top level object.
            moviesOut.writeObject(this.movies);
            moviesOut.close();

        } catch (IOException e) {
            Log.e("RatingList", "Error writing an entry from binary file");
            success = false;
        }
        return success;
    }
}
