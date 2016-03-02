package edu.gatech.slowroastingautoclaves.recommendr.model;

import java.io.Serializable;

import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.UserList;

/**
 * Represents a Movie object.
 */
public class Movie implements Serializable {
    String title, year, description, rating, userRating;

    /**
     * Sets Movie title.
     * @param title is title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets Movie release year.
     * @param year is year to set.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Sets Movie rating.
     * @param rating is rating to set.
     */
    public void setRating(String rating) {
        if (rating.length() > 0) {
            this.rating = "Rating: " + rating;
        }
        this.rating = "No rating found.";
    }

    public void getUserRating() {
        RatingList.getInstance().getRating(this.toString());
    }

    /**
     * Sets Movie description/synopsis.
     * @param description is description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets Movie title.
     * @return the title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets Move release year.
     * @return the year.
     */
    public String getYear() {
        return this.year;
    }

    /**
     * Gets Movie rating.
     * @return the rating.
     */
    public String getRating() {
        return this.rating;
    }

    /**
     * Gets Movie description/synopsis.
     * @return the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets Movie identifier as title and year.
     * @return a string that identifies the Movie.
     */
    public String toString() {
        return (getTitle() + " (" + getYear() + ")");
    }
}
