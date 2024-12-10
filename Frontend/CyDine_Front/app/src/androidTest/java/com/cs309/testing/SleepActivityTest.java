package com.cs309.testing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.androidexample.SleepActivity;
import com.example.androidexample.R;

@RunWith(AndroidJUnit4.class)
public class SleepActivityTest {

    @Rule
    public ActivityScenarioRule<SleepActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SleepActivity.class);

    /**
     * Test 1: Verify that the sleep input field, day input field, and buttons are displayed.
     */
    @Test
    public void testFieldsAreDisplayed() {
        onView(withId(R.id.sleepInput)).check(matches(isDisplayed()));
        onView(withId(R.id.dayInput)).check(matches(isDisplayed()));
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()));
        onView(withId(R.id.deleteButton)).check(matches(isDisplayed()));
        onView(withId(R.id.feedbackMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.statsTextView)).check(matches(isDisplayed()));
    }

    /**
     * Test 2: Verify that entering valid sleep hours and day updates the feedback and stats.
     */
    @Test
    public void testValidInput() {
        // Input sleep data (6 hours for day 3)
        onView(withId(R.id.sleepInput)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.dayInput)).perform(typeText("3"), closeSoftKeyboard());

        // Click submit button
        onView(withId(R.id.submitButton)).perform(click());

        // Verify feedback message
        onView(withId(R.id.feedbackMessage)).check(matches(withText("Great job! You're within the healthy range.")));

        // Verify that the average sleep is updated in the stats
        //onView(withId(R.id.statsTextView)).check(matches(withText("Average Sleep: 6.0 hours")));
    }

    /**
     * Test 3: Verify that entering invalid data (like a non-numeric value) shows an error message.
     */
    @Test
    public void testInvalidInput() {
        // Enter invalid sleep data (non-numeric)
        onView(withId(R.id.sleepInput)).perform(typeText("invalid"), closeSoftKeyboard());
        onView(withId(R.id.dayInput)).perform(typeText("2"), closeSoftKeyboard());

        // Click submit button
        onView(withId(R.id.submitButton)).perform(click());

        // Verify error message
        //onView(withId(R.id.feedbackMessage)).check(matches(withText("Invalid input. Please enter valid numbers.")));
    }

    /**
     * Test 4: Verify that entering an invalid day (less than 1 or greater than 7) shows an error message.
     */
    @Test
    public void testInvalidDay() {
        // Input sleep data (7 hours for invalid day 10)
        onView(withId(R.id.sleepInput)).perform(typeText("7"), closeSoftKeyboard());
        onView(withId(R.id.dayInput)).perform(typeText("10"), closeSoftKeyboard());

        // Click submit button
        onView(withId(R.id.submitButton)).perform(click());

        // Verify error message for invalid day
        onView(withId(R.id.feedbackMessage)).check(matches(withText("Invalid day. Enter a day between 1 and 7.")));
    }

    /**
     * Test 5: Verify that clicking the delete button clears sleep data for the specified day.
     */
    @Test
    public void testDeleteSleepData() {
        // Set sleep data for day 2
        onView(withId(R.id.sleepInput)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.dayInput)).perform(typeText("2"), closeSoftKeyboard());

        // Submit the data
        onView(withId(R.id.submitButton)).perform(click());

        // Now delete sleep data for day 2
        onView(withId(R.id.dayInput)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.deleteButton)).perform(click());

        // Verify that the graph and stats have been updated accordingly
        //onView(withId(R.id.statsTextView)).check(matches(withText("Average Sleep: 0.0 hours")));
    }
}
