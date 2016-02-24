package edu.gatech.slowroastingautoclaves.recommendr.dummy;

import java.io.Serializable;

/**
 * Created by daernwyn on 2/23/16.
 */
public class Movie implements Serializable {
    String title, year, description, rating;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setRating(String rating) {
        if (rating.length() > 0) {
            this.rating = "Rating: " + rating;
        }
        this.rating = "No rating found.";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getYear() {
        return this.year;
    }

    public String getRating() {
        return this.rating;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return (getTitle() + " (" + getYear() + ")");
    }
}
