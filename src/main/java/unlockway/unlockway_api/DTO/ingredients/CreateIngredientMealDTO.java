package unlockway.unlockway_api.DTO.ingredients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.Measure;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientMealDTO {
    private UUID id;
    private double amount;
}
