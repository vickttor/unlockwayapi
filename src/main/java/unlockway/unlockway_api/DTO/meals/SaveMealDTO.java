package unlockway.unlockway_api.DTO.meals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.DTO.ingredients.CreateIngredientMealDTO;
import unlockway.unlockway_api.enums.MealCategory;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveMealDTO {
    private UUID userId;
    private UUID id;
    private String name;
    private String description;
    private List<CreateIngredientMealDTO> ingredients;
    private MealCategory category;
    private String preparationMethod;
}
