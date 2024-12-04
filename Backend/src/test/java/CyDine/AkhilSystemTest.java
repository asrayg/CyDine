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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    void testGetFitnessByUserId() {
        Fitness fitness1 = new Fitness();
        fitness1.setName("Running");
        fitness1.setUserId(1);

        Fitness fitness2 = new Fitness();
        fitness2.setName("Swimming");
        fitness2.setUserId(1);

        List<Fitness> fitnessList = Arrays.asList(fitness1, fitness2);

        when(fitnessRepository.findAll()).thenReturn(fitnessList);

        List<Fitness> result = fitnessRepository.findAll();
        List<Fitness> userFitness = result.stream()
                .filter(f -> f.getUserId() == 1)
                .toList();

        assertEquals(2, userFitness.size());
        assertTrue(userFitness.stream().anyMatch(f -> f.getName().equals("Running")));
        assertTrue(userFitness.stream().anyMatch(f -> f.getName().equals("Swimming")));
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
    void testGetFoodItemById() {
        FoodItems foodItem = new FoodItems();
        foodItem.setId(1);
        foodItem.setName("Banana");
        foodItem.setCalories(105);

        when(foodItemsRepository.findById(1)).thenReturn(foodItem);

        FoodItems retrievedFoodItem = foodItemsRepository.findById(1);

        assertNotNull(retrievedFoodItem);
        assertEquals("Banana", retrievedFoodItem.getName());
        assertEquals(105, retrievedFoodItem.getCalories());
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
    void testUpdateWaterIntake() {
        Water existingWater = new Water();
        existingWater.setId(1);
        existingWater.setUserId(1);
        existingWater.setGoal(2000);
        existingWater.setTotal(500);

        when(waterRepository.findById(1)).thenReturn(existingWater);
        when(waterRepository.save(any(Water.class))).thenReturn(existingWater);

        Water waterToUpdate = waterRepository.findById(1);
        waterToUpdate.setTotal(750);

        Water updatedWater = waterRepository.save(waterToUpdate);

        assertNotNull(updatedWater);
        assertEquals(750, updatedWater.getTotal());
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

    @Test
    void testGetSleepEntriesByUserId() {
        SleepEntry entry1 = new SleepEntry();
        entry1.setUserId(1);
        entry1.setHoursSlept(7.0);

        SleepEntry entry2 = new SleepEntry();
        entry2.setUserId(1);
        entry2.setHoursSlept(8.0);

        List<SleepEntry> sleepEntries = Arrays.asList(entry1, entry2);

        when(sleepRepository.findAll()).thenReturn(sleepEntries);

        List<SleepEntry> result = sleepRepository.findAll();
        List<SleepEntry> userSleepEntries = result.stream()
                .filter(s -> s.getUserId() == 1)
                .toList();

        assertEquals(2, userSleepEntries.size());
        assertTrue(userSleepEntries.stream().anyMatch(s -> s.getHoursSlept() == 7.0));
        assertTrue(userSleepEntries.stream().anyMatch(s -> s.getHoursSlept() == 8.0));
    }
}