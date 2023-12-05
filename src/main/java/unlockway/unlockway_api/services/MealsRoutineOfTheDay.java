package unlockway.unlockway_api.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.models.MealsOfTheDay;
import unlockway.unlockway_api.models.RoutineModel;
import unlockway.unlockway_api.models.relationships.RoutineMealModel;
import unlockway.unlockway_api.repositories.MealsOfTHeDayRepository;
import unlockway.unlockway_api.repositories.RoutineMealRepository;
import unlockway.unlockway_api.repositories.RoutineRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MealsRoutineOfTheDay {

    private final RoutineRepository routineRepository;
    private final MealsOfTHeDayRepository mealsOfTHeDayRepository;
    private final RoutineMealRepository routineMealRepository;

    public List<RoutineMealModel> getActualMealRoutines() {
        List<MealsOfTheDay> mealsOfTheDay = mealsOfTHeDayRepository.findAll();
        List<RoutineMealModel> meals = new ArrayList<>();

        try {
            for (MealsOfTheDay mealOfTheDay : mealsOfTheDay) {
                Optional<RoutineMealModel> routineMeal = routineMealRepository.findById(mealOfTheDay.getRoutineMealId());
                routineMeal.ifPresent(meals::add);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        return meals;
    }

    public void addMeal(RoutineMealModel meal) {
        mealsOfTHeDayRepository.save(MealsOfTheDay.builder().routineMealId(meal.getId()).build());
    }

    @Transactional
    public void removeMeal(UUID id) {
        mealsOfTHeDayRepository.deleteByRoutineMealId(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Every midnight
    public void resetList() {
        // Get current time in UTC
        Instant utcNow = Instant.now();

        // Convert to Brazil time zone
        ZoneId brazilTimeZone = ZoneId.of("America/Sao_Paulo");
        LocalDate brazilTime = LocalDate.ofInstant(utcNow, brazilTimeZone);

       var routines = findAllOfTheDay(brazilTime);

       routines.forEach((routine)-> {
           mealsOfTHeDayRepository.saveAll(routine.getRoutineMeal().stream().map(e->MealsOfTheDay.builder().routineMealId(e.getId()).build()).toList());
       });
    }

    private List<RoutineModel> findAllOfTheDay(LocalDate date) {
        var today = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return switch (today) {
            case "Sunday" -> routineRepository.findAllOfSunday();
            case "Monday" -> routineRepository.findAllOfMonday();
            case "Tuesday" -> routineRepository.findAllOfTuesday();
            case "Wednesday" -> routineRepository.findAllOfWednesday();
            case "Thursday" -> routineRepository.findAllOfThursday();
            case "Friday" -> routineRepository.findAllOfFriday();
            default -> routineRepository.findAllOfSaturday();
        };

    }

}
