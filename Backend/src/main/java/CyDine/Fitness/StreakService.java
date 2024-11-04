package CyDine.Fitness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Service
public class StreakService {

    @Autowired
    private FitnessRepository fitnessRepository;

    public int calculateStreak(int userId) {
        List<Fitness> userActivities = fitnessRepository.findAll().stream()
                .filter(f -> f.getUserId() == userId)
                .sorted(Comparator.comparing(Fitness::getDate).reversed())
                .toList();

        if (userActivities.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate currentDate = LocalDate.now();
        LocalDate lastActivityDate = null;

        for (Fitness activity : userActivities) {
            LocalDate activityDate = activity.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            if (lastActivityDate == null) {
                // First activity
                if (activityDate.isEqual(currentDate) || activityDate.isEqual(currentDate.minusDays(1))) {
                    streak = 1;
                    lastActivityDate = activityDate;
                } else {
                    // The most recent activity is too old to count
                    return 0;
                }
            } else if (activityDate.isEqual(lastActivityDate.minusDays(1))) {
                // Consecutive day
                streak++;
                lastActivityDate = activityDate;
            } else if (activityDate.isBefore(lastActivityDate.minusDays(1))) {
                // Gap in the streak, stop counting
                break;
            }
            // If the date is the same as lastActivityDate, continue to the next activity
        }

        return streak;
    }
}
