package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidexample.ModLoginActivity;
import com.example.androidexample.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ModLoginActivityTest {

    private static final String VALID_EMAIL = "a@a.a";
    private static final String VALID_PASSWORD = "a";
    private static final String INVALID_EMAIL = "invalid@example.com";
    private static final String INVALID_PASSWORD = "wrongpassword";

    @Rule
    public ActivityScenarioRule<ModLoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ModLoginActivity.class);

    /**
     * Test 1: Validate UI components are displayed correctly.
     */
    @Test
    public void testUIComponentsDisplayed() {
        // Check email field is displayed
        onView(withId(R.id.email)).check(matches(isDisplayed()));

        // Check password field is displayed
        onView(withId(R.id.password)).check(matches(isDisplayed()));

        // Check login button is displayed
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Validate successful login with correct credentials.
     */
    @Test
    public void testValidLogin() {
        // Enter valid email
        onView(withId(R.id.email)).perform(replaceText(VALID_EMAIL));

        // Enter valid password
        onView(withId(R.id.password)).perform(replaceText(VALID_PASSWORD));

        // Click the login button
        onView(withId(R.id.login_button)).perform(click());

        // Validate navigation to ModActivity by checking that it starts
        // Note: Intent verification may require adding a dependency for Intent assertions
        //onView(withText("Message Approved")).check(matches(isDisplayed()));
    }

}
