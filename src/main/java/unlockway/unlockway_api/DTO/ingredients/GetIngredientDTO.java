package unlockway.unlockway_api.DTO.ingredients;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.Measure;

@NoArgsConstructor
@Data
public class GetIngredientDTO {
    private UUID id;
    private String name;
    private String photo;
    private String description;
    private double calories;
    private double proteins;
    private double water;
    private String minerals;
    private String vitamins;
    private Measure measure;
    private double fats;
}
