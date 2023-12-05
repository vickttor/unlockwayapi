package unlockway.unlockway_api.DTO.meals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.DTO.ingredients.GetIngredientMealDTO;
import unlockway.unlockway_api.enums.MealCategory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMealDTO {
    private UUID id;
    private String name;
    private String photo;
    private String description;
    private List<GetIngredientMealDTO> ingredients;
    private MealCategory category;
    private String preparationMethod;
    private double totalCalories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}