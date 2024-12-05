package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.androidexample.LoginActivity;
import com.example.androidexample.R;

@RunWith(AndroidJUnit4.class)
public class IssmaleSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Test 1: Verify that the email and password fields are displayed.
     */
    @Test
    public void testFieldsAreDisplayed() {
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify that clicking the login button with empty fields does not crash the app.
     */
    @Test
    public void testEmptyFields() {
        onView(withId(R.id.login_button)).perform(click());
        // Verify the email and password fields are still displayed (no crash occurred)
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    /**
     * Test 3: Verify that entering text in the email and password fields works.
     */
    @Test
    public void testTypingInFields() {
        onView(withId(R.id.email)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard());
        // Verify the fields still exist
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    /**
     * Test 4: Verify that clicking the "Sign Up" button navigates to the sign-up page.
     */
    @Test
    public void testSignUpNavigation() {
        onView(withId(R.id.sign_up_redirect)).perform(click());
        // Verify that the SignUpActivity is displayed
        onView(withId(R.id.first_name)).check(matches(isDisplayed()));
    }
}