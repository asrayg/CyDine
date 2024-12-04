package CyDine;

import CyDine.Fitness.Fitness;
import CyDine.FoodItems.FoodItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AkhilSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Add any setup code here
    }

    @Test
    void testCreateAndRetrieveFoodItem() throws Exception {
        FoodItems foodItem = new FoodItems();
        foodItem.setName("Test Food");
        foodItem.setCalories(200);
        foodItem.setUserId(1);

        // Create food item
        String foodItemJson = objectMapper.writeValueAsString(foodItem);
        String responseContent = mockMvc.perform(post("/FoodItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(foodItemJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int foodItemId = Integer.parseInt(responseContent);

        // Retrieve created food item
        mockMvc.perform(get("/FoodItem/{id}", foodItemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Food"))
                .andExpect(jsonPath("$.calories").value(200))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void testUpdateFoodItem() throws Exception {
        // First, create a food item
        FoodItems foodItem = new FoodItems();
        foodItem.setName("Original Food");
        foodItem.setCalories(100);
        foodItem.setUserId(1);

        String foodItemJson = objectMapper.writeValueAsString(foodItem);
        String responseContent = mockMvc.perform(post("/FoodItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(foodItemJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int foodItemId = Integer.parseInt(responseContent);

        // Now update the food item
        foodItem.setName("Updated Food");
        foodItem.setCalories(150);

        mockMvc.perform(put("/FoodItem/{id}", foodItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Food"))
                .andExpect(jsonPath("$.calories").value(150));
    }

    @Test
    void testCreateAndRetrieveFitnessRecord() throws Exception {
        Fitness fitness = new Fitness();
        fitness.setName("Running");
        fitness.setTime(30);
        fitness.setCalories(300);
        fitness.setUserId(1);

        String fitnessJson = objectMapper.writeValueAsString(fitness);

        // Create fitness record
        mockMvc.perform(post("/fitness")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fitnessJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activity").value("Running"))
                .andExpect(jsonPath("$.duration").value(30))
                .andExpect(jsonPath("$.caloriesBurned").value(300));

        // Retrieve user's fitness records
        mockMvc.perform(get("/users/{userId}/fitness", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activity").value("Running"))
                .andExpect(jsonPath("$[0].duration").value(30))
                .andExpect(jsonPath("$[0].caloriesBurned").value(300));
    }

    @Test
    void testUserStreakCalculation() throws Exception {
        // Create multiple fitness records for a user on consecutive days
        for (int i = 0; i < 3; i++) {
            Fitness fitness = new Fitness();
            fitness.setName("Daily Exercise");
            fitness.setTime(30);
            fitness.setCalories(200);
            fitness.setUserId(1);
            // Set date to consecutive days
            fitness.setDate(Date.from(java.time.LocalDate.now().minusDays(i).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            String fitnessJson = objectMapper.writeValueAsString(fitness);

            mockMvc.perform(post("/fitness")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(fitnessJson))
                    .andExpect(status().isOk());
        }

        // Check the user's streak
        mockMvc.perform(get("/users/{userId}/streak", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}
