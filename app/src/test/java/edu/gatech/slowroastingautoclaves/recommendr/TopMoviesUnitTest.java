package edu.gatech.slowroastingautoclaves.recommendr;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.model.Movie;
import edu.gatech.slowroastingautoclaves.recommendr.model.Rating;
import edu.gatech.slowroastingautoclaves.recommendr.model.User;
import edu.gatech.slowroastingautoclaves.recommendr.model.database.RatingList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for movie recommendation method.
 */
public class TopMoviesUnitTest {
    private RatingList rl;
    private ArrayList<Movie> noFilterExpected;
    private ArrayList<Movie> csFilterExpected;
    private ArrayList<Movie> eeFilterExpected;

    @Before
    public void setUp() {
        rl = RatingList.getInstance();
        Movie m1 = new Movie();
        m1.setTitle("Lion King");
        Movie m2 = new Movie();
        m2.setTitle("Lion King 2");
        Movie m3 = new Movie();
        m3.setTitle("Toy Story");
        Movie m4 = new Movie();
        m4.setTitle("Avatar");
        Movie m5 = new Movie();
        m5.setTitle("The Wind Rises");
        Movie m6 = new Movie();
        m6.setTitle("Only Yesterday");
        //movie with no rating
        Movie m7 = new Movie();
        m7.setTitle("Not a rated movie");

        rl.addMovie(m1);
        rl.addMovie(m2);
        rl.addMovie(m3);
        rl.addMovie(m4);
        rl.addMovie(m5);
        rl.addMovie(m6);
        rl.addMovie(m7);

        User u1 = new User("1", "1", "1");
        u1.setMajor("CS");
        User u2 = new User("2", "2", "2");
        u2.setMajor("CS");
        User u3 = new User("3", "3", "3");
        u3.setMajor("EE");
        User u4 = new User("4", "4", "4");
        u4.setMajor("EE");
        User u5 = new User("5", "5", "5");
        u5.setMajor("DM");
        //no major
        User u6 = new User("6", "6", "6");

        Rating r1 = new Rating("Lion King", u1, 100.0);
        Rating r2 = new Rating("Lion King 2", u1, 80.0);
        Rating r3 = new Rating("Lion King", u2, 95.0);
        Rating r4 = new Rating("Toy Story", u3, 100.0);
        Rating r5 = new Rating("Lion King", u3, 97.0);
        Rating r6 = new Rating("Avatar", u1, 0.0);
        Rating r7 = new Rating("Avatar", u2, 10.0);
        Rating r8 = new Rating("Only Yesterday", u5, 100.0);
        Rating r9 = new Rating("The Wind Rises", u4, 100.0);
        Rating r10 = new Rating("Lion King", u6, 100.0);

        rl.addRating(r1);
        rl.addRating(r2);
        rl.addRating(r3);
        rl.addRating(r4);
        rl.addRating(r5);
        rl.addRating(r6);
        rl.addRating(r7);
        rl.addRating(r8);
        rl.addRating(r9);
        rl.addRating(r10);

        noFilterExpected = new ArrayList<>();
        noFilterExpected.add(m6);
        noFilterExpected.add(m5);
        noFilterExpected.add(m3);
        noFilterExpected.add(m1);
        noFilterExpected.add(m2);
        noFilterExpected.add(m4);

        csFilterExpected = new ArrayList<>();
        csFilterExpected.add(m1);
        csFilterExpected.add(m2);
        csFilterExpected.add(m4);

        eeFilterExpected = new ArrayList<>();
        eeFilterExpected.add(m5);
        eeFilterExpected.add(m3);
        eeFilterExpected.add(m1);
    }

    @Test
    public void testNoFilterTopMovies() {
        assertEquals(noFilterExpected, rl.getTopMovies("null", "null"));
    }

    @Test
    public void testNoFilterIgnoreParameter() {
        assertEquals(noFilterExpected, rl.getTopMovies("null", "major"));
    }

    @Test
    public void testNullParameter() {
        assertEquals(noFilterExpected, rl.getTopMovies("null", null));
    }

    @Test
    public void testMajorFilter() {
        assertEquals(csFilterExpected, rl.getTopMovies("major", "CS"));
    }

    @Test
    public void testOtherMajorFilter() {
        assertEquals(eeFilterExpected, rl.getTopMovies("major", "EE"));
    }

    @Test
    public void testNoMatchingMovies() {
        assertNull(rl.getTopMovies("major", "ME"));
    }

    @Test
    public void testNullParameterWithFilter() {
        assertNull(rl.getTopMovies("major", null));
    }

    @Test
    public void testFakeFilter() {
        assertNull(rl.getTopMovies("bogus", null));
    }

    @Test
    public void testNoRatingsButMovies() {
        rl.getRatings().clear();
        assertNull(rl.getTopMovies("null", "null"));
    }

    @Test
    public void testNoMoviesButRatings() {
        rl.getMovies().clear();
        assertEquals(noFilterExpected, rl.getTopMovies("null", "null"));
    }

    @Test
    public void testNoMoviesNoRatings() {
        rl.getMovies().clear();
        rl.getRatings().clear();
        assertNull(rl.getTopMovies("null", "null"));
    }

}
