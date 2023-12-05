package unlockway.unlockway_api.DTO.history.meals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.MealCategory;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetHistoryMealDTO {

    private UUID id;
    private UUID idMeal;
    private UUID idRoutineMeal;
    private boolean ingested;
    private String name;
    private String photo;
    private String description;
    private MealCategory category;
    private double totalCalories;

}
