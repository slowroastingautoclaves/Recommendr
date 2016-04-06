package edu.gatech.slowroastingautoclaves.recommendr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

import static org.junit.Assert.*;

/**
 * Created by Joshua Jibilian on 4/5/16.
 */
public class GetTheRatingTest {
    RatingList ratingList;
    Rating rate1;
    Rating rate2;
    Rating rate3;
    Rating rate4;
    @Before
    public void makeRatings(){

        ratingList = RatingList.getInstance();
        User user1 = new User("User 1", "User 1", "User 1");
        rate1 = new Rating("Movie 1", user1, 60);

        User user2 = new User("User 1", "User 1", "User 1");
        rate2 = new Rating("Movie 1", user2, 70);
        ratingList.addRating(rate1);
        ratingList.addRating(rate2);

        rate3 = new Rating("Movie 2", user1, -1);
        ratingList.addRating(rate3);

        rate4 = new Rating("Movie 3", user1, 101);
        ratingList.addRating(rate4);
    }

    @Test
    public void testGetRating1(){
        assertEquals("Average User Rating: 65.0", ratingList.getRating("Movie 1"));


    }
    @Test
    public void testGetRatingBelow100(){
        assertEquals("Average User Rating: 0.0", ratingList.getRating("Movie 2"));
    }
    @Test
    public void testGetRatingAbove100(){
        assertEquals("Average User Rating: 100.0", ratingList.getRating("Movie 3"));
    }

    @Test
    public void testGetRatingNonExsist(){
        assertEquals("Average User Rating: 0.0", ratingList.getRating("Movie whatever"));
    }

    @After
    public void after(){
        ratingList.removeRating(rate1);
        ratingList.removeRating(rate2);
        ratingList.removeRating(rate3);
        ratingList.removeRating(rate4);
    }
}