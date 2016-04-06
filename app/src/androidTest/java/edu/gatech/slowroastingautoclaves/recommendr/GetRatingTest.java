package edu.gatech.slowroastingautoclaves.recommendr;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;

import static org.junit.Assert.*;

/**
 * Created by Joshua Jibilian on 4/5/16.
 */
public class GetRatingTest {
    RatingList ratingList;
    @Before
    public void makeRatings(){
        ratingList = RatingList.getInstance();
        User user1 = new User("User 1", "User 1", "User 1");
        Rating rate1 = new Rating("Movie 1", user1, 60);

        User user2 = new User("User 1", "User 1", "User 1");
        Rating rate2 = new Rating("Movie 1", user2, 70);
        ratingList.addRating(rate1);
        ratingList.addRating(rate2);
    }

    @Test
    public void testGetRating(){
        assertEquals(ratingList.getRating("Movie 1"), 75);
    }
}