package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.androidexample.AdminActivity;
import com.example.androidexample.R;

@RunWith(AndroidJUnit4.class)
public class AdminActivitySystemTest {

    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdminActivity.class);

    /**
     * Test 1: Verify that all buttons and the active user count TextView are displayed.
     */
    @Test
    public void testUIComponentsAreDisplayed() {
        onView(withId(R.id.user_management_button)).check(matches(isDisplayed()));
        onView(withId(R.id.dining_hall_data_button)).check(matches(isDisplayed()));
        onView(withId(R.id.active_users_count)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify navigation to User Management Activity.
     */
    @Test
    public void testUserManagementNavigation() {
        onView(withId(R.id.user_management_button)).perform(click());
        // Assuming the UserManagementActivity has a TextView with ID user_management_title
        onView(withId(R.id.user_management_title)).check(matches(isDisplayed()));
    }

    /**
     * Test 3: Verify navigation to Dining Hall Data Activity.
     */

}
