package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.androidexample.MainActivity;
import com.example.androidexample.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test 1: Verify that the "Sign Up" button is displayed and clickable.
     */
    @Test
    public void testSignupButtonIsDisplayedAndClickable() {
        // Verify that the signup button is displayed
        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));

        // Click the signup button
        onView(withId(R.id.signup_button)).perform(click());

        // Verify that the SignUpActivity is launched (check for a unique element in SignUpActivity)
        //onView(withId(R.id.sign_up_form)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify that the "Login" button is displayed and clickable.
     */
    @Test
    public void testLoginButtonIsDisplayedAndClickable() {
        // Verify that the login button is displayed
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));

        // Click the login button
        onView(withId(R.id.login_button)).perform(click());

        // Verify that the UserModAdminActivity is launched (check for a unique element in UserModAdminActivity)
        //onView(withId(R.id.user_mod_admin_main)).check(matches(isDisplayed()));
    }
}
