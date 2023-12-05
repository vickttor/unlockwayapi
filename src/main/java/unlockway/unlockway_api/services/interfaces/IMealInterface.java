package unlockway.unlockway_api.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import unlockway.unlockway_api.DTO.meals.GetMealDTO;
import unlockway.unlockway_api.DTO.meals.SaveMealDTO;
import unlockway.unlockway_api.enums.MealCategory;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMealInterface {
    GetMealDTO findById(UUID userId)  throws ResourceNotFoundException;
    List<GetMealDTO> findByUserId(UUID userId);
    List<GetMealDTO> findByCategory(UUID userId, MealCategory mealCategory);
    List<GetMealDTO> findByName(UUID userId, String name);
    GetMealDTO createMeal(SaveMealDTO saveMealDTO, Optional<MultipartFile> photo) throws ResourceNotFoundException;
    GetMealDTO updateMeal(SaveMealDTO saveMealDTO, Optional<MultipartFile> photo) throws ResourceNotFoundException;
    String deleteMeal(UUID id) throws ResourceNotFoundException;
}
