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
import androidx.test.filters.LargeTest;

import com.example.androidexample.LoginActivity;
import com.example.androidexample.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AsraySystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    // Test Case 1: Navigate from Login to Signup
    @Test
    public void testNavigateToSignUp() {
        // Click on the "Sign Up" text view in LoginActivity
        onView(withId(R.id.sign_up_redirect)).perform(click());

        // Verify that the SignUpActivity is displayed
        onView(withId(R.id.first_name)).check(matches(isDisplayed()));
    }

    // Test Case 2: Empty Fields Validation in SignUpActivity
    @Test
    public void testEmptyFieldsValidationInSignUp() {
        // Navigate to SignUpActivity
        onView(withId(R.id.sign_up_redirect)).perform(click());

        // Click on the "Sign Up" button without entering any data
        onView(withId(R.id.signup_button)).perform(click());

        // Verify that the first name, email, and password fields remain displayed
        onView(withId(R.id.first_name)).check(matches(isDisplayed()));
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    // Test Case 3: Invalid Email Validation in SignUpActivity
    @Test
    public void testInvalidEmailValidationInSignUp() {
        // Navigate to SignUpActivity
        onView(withId(R.id.sign_up_redirect)).perform(click());

        // Enter invalid email
        onView(withId(R.id.email)).perform(typeText("invalidemail.com"), closeSoftKeyboard());

        // Enter valid data in other fields
        onView(withId(R.id.first_name)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.last_name)).perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("password123"), closeSoftKeyboard());

        // Click on the "Sign Up" button
        onView(withId(R.id.signup_button)).perform(click());

        // Verify that the email field remains displayed due to validation failure
        onView(withId(R.id.email)).check(matches(isDisplayed()));
    }

    // Test Case 4: Password Mismatch Validation in SignUpActivity
    @Test
    public void testPasswordMismatchValidationInSignUp() {
        // Navigate to SignUpActivity
        onView(withId(R.id.sign_up_redirect)).perform(click());

        // Enter mismatching passwords
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("password456"), closeSoftKeyboard());

        // Enter valid data in other fields
        onView(withId(R.id.first_name)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.last_name)).perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("john.doe@example.com"), closeSoftKeyboard());

        // Click on the "Sign Up" button
        onView(withId(R.id.signup_button)).perform(click());

        // Verify that the password and confirm password fields remain displayed
        onView(withId(R.id.password)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password)).check(matches(isDisplayed()));
    }

    // Test Case 5: Successful Signup Navigation
    @Test
    public void testSuccessfulSignupNavigation() {
        // Navigate to SignUpActivity
        onView(withId(R.id.sign_up_redirect)).perform(click());

        // Enter valid data in all fields
        onView(withId(R.id.first_name)).perform(typeText("Jane"), closeSoftKeyboard());
        onView(withId(R.id.last_name)).perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("jane.doe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("password123"), closeSoftKeyboard());

        // Click on the "Sign Up" button
        onView(withId(R.id.signup_button)).perform(click());

    }
}
