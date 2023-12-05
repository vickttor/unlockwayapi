package unlockway.unlockway_api.DTO.Routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.MealCategory;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMealsRoutineDTO {
	private UUID id;
    private UUID mealId;
    private LocalTime notifyAt;
    private String photo;
    private String name;
    private String description;
    private MealCategory category;
    private double totalCalories;
}
