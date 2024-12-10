package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.androidexample.HomeScreenActivity;
import com.example.androidexample.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> activityScenarioRule =
            new ActivityScenarioRule<>(HomeScreenActivity.class);

    /**
     * Test 1: Verify that the toolbar is displayed.
     */
    @Test
    public void testToolbarIsDisplayed() {
        // Verify that the toolbar is visible on the screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify that clicking a menu item opens the navigation drawer.
     */
    @Test
    public void testOpenNavigationDrawer() {
        // Click the toolbar or drawer toggle button
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Verify that the navigation drawer is displayed
        onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));
    }


    /**
     * Test 5: Verify fallback when an invalid menu item is clicked.
     */
    @Test
    public void testInvalidMenuSelection() {
        // Simulate a menu selection with an invalid ID
        // Note: This requires modifying the activity to expose a method for testing invalid cases.
        // This is just an example of handling invalid cases programmatically, not through UI.
    }
}
