package CyDine;

import CyDine.Fitness.Fitness;
import CyDine.Fitness.FitnessRepository;
import CyDine.FoodItems.FoodItems;
import CyDine.FoodItems.FoodItemsRepository;
import CyDine.Water.Water;
import CyDine.Water.WaterRepository;
import CyDine.SleepTracking.SleepEntry;
import CyDine.SleepTracking.SleepRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AkhilSystemTest {

    @Mock
    private FitnessRepository fitnessRepository;

    @Mock
    private FoodItemsRepository foodItemsRepository;

    @Mock
    private WaterRepository waterRepository;

    @Mock
    private SleepRepository sleepRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFitness() {
        Fitness fitness = new Fitness();
        fitness.setName("Running");
        fitness.setTime(30);
        fitness.setCalories(300);
        fitness.setUserId(1);

        when(fitnessRepository.save(any(Fitness.class))).thenReturn(fitness);

        Fitness savedFitness = fitnessRepository.save(fitness);

        assertNotNull(savedFitness);
        assertEquals("Running", savedFitness.getName());
        assertEquals(30, savedFitness.getTime());
        assertEquals(300, savedFitness.getCalories());
        assertEquals(1, savedFitness.getUserId());
    }

    @Test
    void testCreateFoodItem() {
        FoodItems foodItem = new FoodItems();
        foodItem.setName("Apple");
        foodItem.setCalories(95);
        foodItem.setUserId(1);

        when(foodItemsRepository.save(any(FoodItems.class))).thenReturn(foodItem);

        FoodItems savedFoodItem = foodItemsRepository.save(foodItem);

        assertNotNull(savedFoodItem);
        assertEquals("Apple", savedFoodItem.getName());
        assertEquals(95, savedFoodItem.getCalories());
        assertEquals(1, savedFoodItem.getUserId());
    }

    @Test
    void testTrackWaterIntake() {
        Water water = new Water();
        water.setUserId(1);
        water.setGoal(2000);
        water.setTotal(500);
        water.setDate(new Date());

        when(waterRepository.save(any(Water.class))).thenReturn(water);

        Water savedWater = waterRepository.save(water);

        assertNotNull(savedWater);
        assertEquals(1, savedWater.getUserId());
        assertEquals(2000, savedWater.getGoal());
        assertEquals(500, savedWater.getTotal());
    }

    @Test
    void testCreateSleepEntry() {
        SleepEntry sleepEntry = new SleepEntry();
        sleepEntry.setUserId(1);
        sleepEntry.setHoursSlept(7.5);

        when(sleepRepository.save(any(SleepEntry.class))).thenReturn(sleepEntry);

        SleepEntry savedSleepEntry = sleepRepository.save(sleepEntry);

        assertNotNull(savedSleepEntry);
        assertEquals(1, savedSleepEntry.getUserId());
        assertEquals(7.5, savedSleepEntry.getHoursSlept());
    }
}