package edu.gatech.slowroastingautoclaves.recommendr.presenter;

import java.util.List;

import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

/**
 * Created by daernwyn on 3/28/16.
 */
public interface MovieInterface {
    Movie getMovie(String identifier);
    void addMovie(Movie m);
    void addRating(Rating r);
    void removeRating(Rating r);
    String getMovieRating(String identifier);
    List<Movie> getTopMovies(String filter, String parameter);
}