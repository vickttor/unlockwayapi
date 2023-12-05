package unlockway.unlockway_api.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.DTO.Routine.*;
import unlockway.unlockway_api.DTO.WeekRepetitionsDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.MealModel;
import unlockway.unlockway_api.models.RoutineModel;
import unlockway.unlockway_api.models.UserModel;
import unlockway.unlockway_api.models.relationships.RoutineMealModel;
import unlockway.unlockway_api.repositories.MealRepository;
import unlockway.unlockway_api.repositories.RoutineMealRepository;
import unlockway.unlockway_api.repositories.RoutineRepository;
import unlockway.unlockway_api.services.interfaces.IRoutinesService;
import unlockway.unlockway_api.services.MealsRoutineOfTheDay;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoutinesService implements IRoutinesService {

    private final RoutineMealRepository routineMealRepository;
    private final MealRepository mealRepository;
    private final RoutineRepository repository;
    private final MealsRoutineOfTheDay mealsRoutineOfTheDayService;

    @Override
    public List<GetRoutineDTO> getRoutinesByUserId(UUID id) {
        List<RoutineModel> routines = repository.findAllByUserId(id);
        return generateRoutineResponse(routines);
    }

    @Override
    public List<GetRoutineDTO> findByName(UUID userId, String name) {
        List<RoutineModel> routines = repository.findByName(userId, name);
        return generateRoutineResponse(routines);
    }

    private List<GetRoutineDTO> generateRoutineResponse(List<RoutineModel> routines) {

        List<GetRoutineDTO> responseToReturn = new ArrayList<>();

        for (var routine : routines) {
            List<GetMealsRoutineDTO> getMealsRoutineDTOList = new ArrayList<>();

            var weekRepetitions = new WeekRepetitionsDTO();

            weekRepetitions.setMonday(routine.isMonday());
            weekRepetitions.setTuesday(routine.isTuesday());
            weekRepetitions.setWednesday(routine.isWednesday());
            weekRepetitions.setThursday(routine.isThursday());
            weekRepetitions.setFriday(routine.isFriday());
            weekRepetitions.setSaturday(routine.isSaturday());
            weekRepetitions.setSunday(routine.isSunday());

            List<RoutineMealModel> mealsReferencyRoutine = routineMealRepository.findAllByRoutineId(routine.getId());

            double totalCalories = 0.0;

            for (RoutineMealModel routineMealCurrent : mealsReferencyRoutine) {
                totalCalories += routineMealCurrent.getMeal().getTotalCalories();
            }

            for (RoutineMealModel mealToAdd : mealsReferencyRoutine) {
                getMealsRoutineDTOList.add(GetMealsRoutineDTO.builder()
                    .id(mealToAdd.getId())
                    .mealId(mealToAdd.getMeal().getId())
                    .notifyAt(mealToAdd.getNotifyAt())
                    .photo(mealToAdd.getMeal().getPhoto())
                    .name(mealToAdd.getMeal().getName())
                    .description(mealToAdd.getMeal().getDescription())
                    .category(mealToAdd.getMeal().getCategory())
                    .totalCalories(mealToAdd.getMeal().getTotalCalories())
                    .build());
            }

            GetRoutineDTO routineToBeReturned = new GetRoutineDTO();

            routineToBeReturned.setId(routine.getId());
            routineToBeReturned.setName(routine.getName());
            routineToBeReturned.setInUsage(routine.isInUsage());
            routineToBeReturned.setMeals(getMealsRoutineDTOList);
            routineToBeReturned.setWeekRepetitions(weekRepetitions);
            routineToBeReturned.setTotalCaloriesInTheDay(totalCalories);
            routineToBeReturned.setCreatedAt(routine.getCreatedAt());
            routineToBeReturned.setUpdatedAt(routine.getUpdatedAt());

            responseToReturn.add(routineToBeReturned);
        }

        return responseToReturn;
    }

    @Override
    @Transactional
    public GetRoutineDTO createRoutines(CreateRoutineDTO createRoutineDTO) throws ResourceNotFoundException {

        RoutineModel routine = new RoutineModel();

        routine.setName(createRoutineDTO.getName());
        routine.setInUsage(createRoutineDTO.getInUsage());
        routine.setMonday(createRoutineDTO.getWeekRepetitions().getMonday());
        routine.setTuesday(createRoutineDTO.getWeekRepetitions().getTuesday());
        routine.setWednesday(createRoutineDTO.getWeekRepetitions().getWednesday());
        routine.setThursday(createRoutineDTO.getWeekRepetitions().getThursday());
        routine.setFriday(createRoutineDTO.getWeekRepetitions().getFriday());
        routine.setSaturday(createRoutineDTO.getWeekRepetitions().getSaturday());
        routine.setSunday(createRoutineDTO.getWeekRepetitions().getSunday());
        routine.setCreatedAt(LocalDateTime.now());
        routine.setUpdatedAt(LocalDateTime.now());

        List<RoutineMealModel> routineMealModelList = new ArrayList<>();

        UserModel userModel = new UserModel();

        for (CreateMealsRoutineDTO item : createRoutineDTO.getMeals()) {

            RoutineMealModel routineMeal = new RoutineMealModel();

            Optional<MealModel> mealFound = mealRepository.findById(item.getIdMeal());

            if (mealFound.isEmpty()) {
                throw new ResourceNotFoundException("Refeição não encontrada");
            }else{
                var meal = mealFound.get();

                routineMeal.setRoutine(routine);
                routineMeal.setNotifyAt(item.getNotifyAt());
                routineMeal.setMeal(meal);

                if (userModel.getId() == null) userModel = meal.getUserModel();

                routineMealModelList.add(routineMeal);
            }

        }

        routine.setUserModel(userModel);

        RoutineModel createdRoutine = repository.save(routine);

        List<RoutineMealModel> createdRoutineMeals = routineMealRepository.saveAll(routineMealModelList);

        ConditionallyInsertRoutineMealsIntoNotificationsList(createdRoutine, createdRoutineMeals);

        return GetRoutineDTO.builder()
                .id(createdRoutine.getId())
                .name(createRoutineDTO.getName())
                .inUsage( createRoutineDTO.getInUsage())
                .meals(getGetMealsRoutineDTO(createdRoutineMeals))
                .weekRepetitions(createRoutineDTO.getWeekRepetitions())
                .totalCaloriesInTheDay(getGetMealsRoutineDTO(createdRoutineMeals).stream().map(GetMealsRoutineDTO::getTotalCalories).reduce(0.0, Double::sum))
                .inUsage(false)
                .createdAt(createdRoutine.getCreatedAt())
                .updatedAt(createdRoutine.getUpdatedAt())
                .build();

    }

    public void routineInUsage(UUID userId, UUID id) throws ResourceNotFoundException {
        List<RoutineModel> routines = repository.findAllByUserId(userId);
        Optional<RoutineModel> routineToBeUpdated = repository.findById(id);

        if (routineToBeUpdated.isEmpty()) {
            throw new ResourceNotFoundException("Rotina não encontrada");
        }

        for (RoutineModel routine : routines) {
            if (!routineToBeUpdated.get().getId().equals(routine.getId())) {
                if (areDaysMatching(routineToBeUpdated.get(), routine)) {
                    routine.setInUsage(false);
                    repository.save(routine);
                }
            }
        }

        routineToBeUpdated.get().setInUsage(true);
        var routine = repository.save(routineToBeUpdated.get());

        for(var meal : routine.getRoutineMeal()) {
            mealsRoutineOfTheDayService.removeMeal(meal.getId());
        }

        ConditionallyInsertRoutineMealsIntoNotificationsList(routine, routine.getRoutineMeal());
    }

    private boolean areDaysMatching(RoutineModel updatedRoutine, RoutineModel existingRoutine) {
        return (updatedRoutine.isSunday() && existingRoutine.isSunday()) ||
                (updatedRoutine.isMonday() && existingRoutine.isMonday()) ||
                (updatedRoutine.isTuesday() && existingRoutine.isTuesday()) ||
                (updatedRoutine.isWednesday() && existingRoutine.isWednesday()) ||
                (updatedRoutine.isThursday() && existingRoutine.isThursday()) ||
                (updatedRoutine.isFriday() && existingRoutine.isFriday()) ||
                (updatedRoutine.isSaturday() && existingRoutine.isSaturday());
    }

    @Override
    public GetRoutineDTO getRoutineInUsageByUserId(UUID userId) throws  ResourceNotFoundException{
        List<RoutineModel> routines = repository.findAllByUserId(userId);

        var getRoutinesDTO = generateRoutineResponse(routines);

        // Get current time in UTC
        Instant utcNow = Instant.now();

        // Convert to Brazil time zone
        ZoneId brazilTimeZone = ZoneId.of("America/Sao_Paulo");
        LocalDateTime brazilTime = LocalDateTime.ofInstant(utcNow, brazilTimeZone);

        var today = brazilTime.getDayOfWeek().name();

        var getRoutinesDTOFiltered = getRoutinesDTO.stream().filter(GetRoutineDTO::isInUsage).filter((routine)-> {
            return switch (today) {
                case "MONDAY" -> routine.getWeekRepetitions().getMonday();
                case "TUESDAY" -> routine.getWeekRepetitions().getTuesday();
                case "WEDNESDAY" -> routine.getWeekRepetitions().getWednesday();
                case "THURSDAY" -> routine.getWeekRepetitions().getThursday();
                case "FRIDAY" -> routine.getWeekRepetitions().getFriday();
                case "SATURDAY" -> routine.getWeekRepetitions().getSaturday();
                default -> routine.getWeekRepetitions().getSunday();
            };
        }).toList();

        if(getRoutinesDTOFiltered.size() != 1) {
            throw new ResourceNotFoundException("Não há rotinas em uso");
        }

        return getRoutinesDTOFiltered.get(0);
    }

    private static List<GetMealsRoutineDTO> getGetMealsRoutineDTO(List<RoutineMealModel> createdRoutineMeals) {

        List<GetMealsRoutineDTO> getMealsRoutineDTOS = new ArrayList<>();

        for(var routineMeal  : createdRoutineMeals) {
            GetMealsRoutineDTO mealRoutine = new GetMealsRoutineDTO();

            mealRoutine.setId(routineMeal.getId());
            mealRoutine.setMealId(routineMeal.getMeal().getId());
            mealRoutine.setName(routineMeal.getMeal().getName());
            mealRoutine.setDescription(routineMeal.getMeal().getDescription());
            mealRoutine.setCategory(routineMeal.getMeal().getCategory());
            mealRoutine.setTotalCalories(routineMeal.getMeal().getTotalCalories());
            mealRoutine.setNotifyAt(routineMeal.getNotifyAt());
            mealRoutine.setPhoto(routineMeal.getMeal().getPhoto());

            getMealsRoutineDTOS.add(mealRoutine);
        }

        return getMealsRoutineDTOS;
    }

    @Override
    @Transactional
    public void updateRoutine(UpdateRoutineDTO updateRoutineDTO) throws ResourceNotFoundException {
        Optional<RoutineModel> routineToBeUpdated = repository.findById(updateRoutineDTO.getId());

        if (routineToBeUpdated.isEmpty())
            throw new ResourceNotFoundException("Rotina com id: " + updateRoutineDTO.getId() + " não encontrada");

        var routine = routineToBeUpdated.get();

        routine.setName(updateRoutineDTO.getName());
        routine.setInUsage(updateRoutineDTO.getInUsage());
        routine.setMonday(updateRoutineDTO.getWeekRepetitions().getMonday());
        routine.setTuesday(updateRoutineDTO.getWeekRepetitions().getTuesday());
        routine.setWednesday(updateRoutineDTO.getWeekRepetitions().getWednesday());
        routine.setThursday(updateRoutineDTO.getWeekRepetitions().getThursday());
        routine.setFriday(updateRoutineDTO.getWeekRepetitions().getFriday());
        routine.setSaturday(updateRoutineDTO.getWeekRepetitions().getSaturday());
        routine.setSunday(updateRoutineDTO.getWeekRepetitions().getSunday());
        routine.setUpdatedAt(LocalDateTime.now());

        List<RoutineMealModel> routineMealModelList = new ArrayList<>();

        for(var meal : routine.getRoutineMeal()) {
            mealsRoutineOfTheDayService.removeMeal(meal.getId());
        }

        repository.deleteAllRoutineMealsByRoutineId(routine.getId());

        for (CreateMealsRoutineDTO mealRoutine : updateRoutineDTO.getMeals()) {

            RoutineMealModel routineMeal = new RoutineMealModel();
            Optional<MealModel> meal = mealRepository.findById(mealRoutine.getIdMeal());

            if (meal.isEmpty())
                throw new ResourceNotFoundException("Refeição com id: " + mealRoutine.getIdMeal() + " não encontrada");

            routineMeal.setRoutine(routine);
            routineMeal.setMeal(meal.get());
            routineMeal.setNotifyAt(mealRoutine.getNotifyAt());
        
            routineMealModelList.add(routineMeal);
        }

        repository.save(routine);

        List<RoutineMealModel> createdRoutineMeals = routineMealRepository.saveAll(routineMealModelList);

        ConditionallyInsertRoutineMealsIntoNotificationsList(routine, createdRoutineMeals);
    }

    private void ConditionallyInsertRoutineMealsIntoNotificationsList(RoutineModel routine, List<RoutineMealModel> createdRoutineMeals) {
        boolean routineOccursToday = false;

        // Get current time in UTC
        Instant utcNow = Instant.now();

        // Convert to Brazil time zone
        ZoneId brazilTimeZone = ZoneId.of("America/Sao_Paulo");

        var today = LocalDate.now(brazilTimeZone).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        if(today.equals("Monday") && routine.isMonday()) routineOccursToday = true;
        else if(today.equals("Tuesday") && routine.isTuesday()) routineOccursToday = true;
        else if(today.equals("Wednesday") && routine.isWednesday()) routineOccursToday = true;
        else if(today.equals("Thursday") && routine.isThursday()) routineOccursToday = true;
        else if(today.equals("Friday") && routine.isFriday()) routineOccursToday = true;
        else if(today.equals("Saturday") && routine.isSaturday()) routineOccursToday = true;
        else if(today.equals("Sunday") && routine.isSunday()) routineOccursToday = true;

        if(routineOccursToday) {
            for(var routineMeal : createdRoutineMeals) {
                // Get the current time
                LocalTime currentBrazilTime = LocalTime.ofInstant(utcNow, brazilTimeZone);

                if(Duration.between(currentBrazilTime, routineMeal.getNotifyAt()).toMinutes() > 0) {
                    try {
                        mealsRoutineOfTheDayService.addMeal(routineMeal);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoutine(UUID id) throws ResourceNotFoundException {
        var routine = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Essa rotina não existe e portanto não pode ser deletada"));

        for(var meal : routine.getRoutineMeal()) {
            mealsRoutineOfTheDayService.removeMeal(meal.getId());
        }

        repository.deleteAllRoutineMealsByRoutineId(id);
        repository.deleteById(id);

    }
}
