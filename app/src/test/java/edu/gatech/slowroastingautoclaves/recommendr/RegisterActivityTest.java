package edu.gatech.slowroastingautoclaves.recommendr;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.support.test.runner.AndroidJUnit4;

import android.widget.TextView;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.slowroastingautoclaves.recommendr.activity.RegisterActivity;

import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Christian Girala on 4/6/2016.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    public void test() {}

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

//    TextView mUsernameView = (TextView) withId(R.id.username);
//    TextView mEmailView = (TextView) withId(R.id.email);
//    TextView mPasswordView = (TextView) withId(R.id.password);



    @Test
    public void allNull() {
//        TextView mUsernameView = (TextView) findViewById(R.id.username);
        onView(withId(R.id.username)).perform(typeText(null), closeSoftKeyboard());
//        onView(withId(R.id.email)).perform(typeText(null), closeSoftKeyboard());
//        onView(withId(R.id.password)).perform(typeText(null), closeSoftKeyboard());
//
//        String errorUsername = "This field is required";
//        assertEquals(errorUsername, mUsernameView.getError().toString());*/
    }


}