package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.androidexample.AdminLoginActivity;
import com.example.androidexample.R;

@RunWith(AndroidJUnit4.class)
public class AdminLoginActivityTest {

    @Rule
    public ActivityScenarioRule<AdminLoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminLoginActivity.class);

    /**
     * Test 1: Verify that all login screen UI components are displayed.
     */
    @Test
    public void testUIComponentsAreDisplayed() {
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify login with correct credentials navigates to AdminActivity.
     */
    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.email)).perform(typeText("admin@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("admin123"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Verify that AdminActivity is displayed
        // Assuming AdminActivity has a unique view with ID active_users_count
        onView(withId(R.id.active_users_count)).check(matches(isDisplayed()));
    }
}
