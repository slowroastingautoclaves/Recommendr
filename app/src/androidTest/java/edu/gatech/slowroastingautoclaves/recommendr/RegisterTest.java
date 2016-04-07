package edu.gatech.slowroastingautoclaves.recommendr;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import org.junit.Before;
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
public class RegisterTest {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

//    TextView mUsernameView;
//    TextView mEmailView;
//    TextView mPasswordView;
//
//    @Before
//    public void startUp() {
//        mUsernameView = (TextView) mActivityRule.getActivity().findViewById(R.id.username);
//        mEmailView = (TextView) mActivityRule.getActivity().findViewById(R.id.email);
//        mPasswordView = (TextView) mActivityRule.getActivity().findViewById(R.id.password);
//    }

    @Test
    public void allEmpty() {
        onView(withId(R.id.username)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("a"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());

        String errorUsername = "This field is required";
        onView(withId(R.id.username)).toString().matches(errorUsername);
    }

    @Test
    public void onlyUsername() {
        onView(withId(R.id.username)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.email)).toString().matches(errorUsername);
    }

    @Test
    public void usernameEmpty() {
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.username)).toString().matches(errorUsername);
    }

    @Test
    public void emailEmpty() {
        onView(withId(R.id.username)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.email)).toString().matches(errorUsername);
    }

    @Test
    public void onlyEmail() {
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("samplesample@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.username)).toString().matches(errorUsername);
    }

    @Test
    public void emailMissingChar() {
        onView(withId(R.id.username)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("fdsafdsa.fdsa"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.email)).toString().matches(errorUsername);
    }

    @Test
    public void passwordEmpty() {
        onView(withId(R.id.username)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.password)).toString().matches(errorUsername);
    }

    @Test
    public void onlyPassword() {
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("samplesample"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());


        String errorUsername = "This field is required";
        onView(withId(R.id.username)).toString().matches(errorUsername);
    }

}