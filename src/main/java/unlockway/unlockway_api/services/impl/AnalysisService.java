package unlockway.unlockway_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.DTO.AnalysisDTO;
import unlockway.unlockway_api.DTO.history.GetHistoryDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.services.interfaces.IAnalysisService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalysisService implements IAnalysisService {

    private final RoutinesService routinesService;
    private final MealService mealService;
    private final HistoryService historyService;
    private final NotificationService notificationService;

    @Override
    public AnalysisDTO getAnalysis(UUID userId) throws ResourceNotFoundException  {

        LocalDate startOfWeek = getStartOfWeek();

        var routines = routinesService.getRoutinesByUserId(userId);
        var meals = mealService.findByUserId(userId);
        var notifications = notificationService.findAllNotificationsByUser(userId);

        List<GetHistoryDTO> histories = historyService.findByUserId(userId);

        histories.sort(Comparator.comparing(GetHistoryDTO::getDate));

        List<Double> totalCaloriesOfWeek = Arrays.asList(new Double[7]);
        Collections.fill(totalCaloriesOfWeek, null);

        histories.forEach((history)-> {
            LocalDate historyDate = history.getDate();

            if (historyDate.isEqual(startOfWeek) || historyDate.isAfter(startOfWeek)) {
                var daysBetween = startOfWeek.datesUntil(historyDate).toList().size();
                totalCaloriesOfWeek.set(daysBetween, history.getTotalCaloriesInTheDay());
            }
        });

        return AnalysisDTO.builder()
                .routines(routines.size())
                .meals(meals.size())
                .notifications(notifications.size())
                .weekCalories(totalCaloriesOfWeek)
                .build();
    }

    private static LocalDate getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date startOfWeek = calendar.getTime();

        return LocalDate.ofInstant(startOfWeek.toInstant(), ZoneId.systemDefault());
    }
}
