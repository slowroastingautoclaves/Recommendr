package edu.gatech.slowroastingautoclaves.recommendr.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a Movie object.
 */
public class Movie implements Serializable, Comparable<Movie> {
    private String title;
    private String year;
    private String description;
    private String rating;
    private double userScore;

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

    /**
     * Sets Movie description/synopsis.
     * @param description is description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the user score (used by {@code RatingList}
     * @param score is score to set
     */
    public void setUserScore(double score) {
        this.userScore = score;
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
     * Gets user score for this movie as set by {@code RatingList}.
     * @return the score
     */
    public double getScore() {
        return this.userScore;
    }

    /**
     * Gets Movie identifier as title and year.
     * @return a string that identifies the Movie.
     */
    public String toString() {
        return (getTitle() + " (" + getYear() + ")");
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Movie)) {
            return false;
        }
        final Movie compare = (Movie) o;
        return compare.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public int compareTo(@NonNull Movie m) {
        if (m.getScore() - this.getScore() == 0.0) {
            return this.getTitle().compareTo(m.getTitle());
        }
        return (int)m.getScore() - (int)this.getScore();
    }
}
