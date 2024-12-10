package com.cs309.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.androidexample.Exercise;

@RunWith(AndroidJUnit4.class)
public class ExerciseSystemTest {

    /**
     * Test 1: Verify that the Exercise object is initialized correctly.
     */
    @Test
    public void testExerciseInitialization() {
        Exercise exercise = new Exercise(1, "Running", 30, 300);

        // Assert that all fields are initialized correctly.
        assertEquals(1, exercise.getId());
        assertEquals("Running", exercise.getName());
        assertEquals(30, exercise.getTimeSpent());
        assertEquals(300, exercise.getCaloriesBurned());
    }

    /**
     * Test 2: Verify that the setters update the fields correctly.
     */
    @Test
    public void testSetters() {
        Exercise exercise = new Exercise(0, null, 0, 0);

        // Update fields using setters.
        exercise.setId(2);
        exercise.setName("Cycling");
        exercise.setTimeSpent(45);
        exercise.setCaloriesBurned(400);

        // Assert that the fields were updated correctly.
        assertEquals(2, exercise.getId());
        assertEquals("Cycling", exercise.getName());
        assertEquals(45, exercise.getTimeSpent());
        assertEquals(400, exercise.getCaloriesBurned());
    }

    /**
     * Test 3: Verify that the Exercise object is not null when instantiated.
     */
    @Test
    public void testExerciseNotNull() {
        Exercise exercise = new Exercise(3, "Swimming", 60, 500);

        // Assert that the Exercise object is not null.
        assertNotNull(exercise);
    }
}
