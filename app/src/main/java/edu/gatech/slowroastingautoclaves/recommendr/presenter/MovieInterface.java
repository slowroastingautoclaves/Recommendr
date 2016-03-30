package edu.gatech.slowroastingautoclaves.recommendr.presenter;

import java.util.List;

import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

/**
 * Created by daernwyn on 3/28/16.
 */
public interface MovieInterface {
    /**
     * gets movie with specified title and year
     * @param identifier is combined string of title and year (Movie.toString())
     * @return the corresponding Movie
     */
    Movie getMovie(String identifier);

    /**
     * adds a movie to the set of movies that have been rated (currently in RatingList)
     * @param m is Movie to add
     */
    void addMovie(Movie m);

    /**
     * adds a rating to the list of ratings
     * @param r is Rating to add
     */
    void addRating(Rating r);

    /**
     * is rating to remove from the list of ratings
     * @param r is Rating to remove
     */
    void removeRating(Rating r);

    /**
     * returns String of rating in the form
     * @param identifier is movie title + year identifier
     * @return the String that shows the rating (see RatingList.java for format)
     */
    String getMovieRating(String identifier);

    /**
     * returns an ArrayList<Movie> based on the filter and filter parameter
     * (see RatingList.java) for implementation
     * @param filter is filter to use ("null" or "major")
     * @param parameter is filter parameter ("null" or "<MAJOR>")
     * @return the List<Movie> of Movie objects with ratings set
     */
    List<Movie> getTopMovies(String filter, String parameter);
}