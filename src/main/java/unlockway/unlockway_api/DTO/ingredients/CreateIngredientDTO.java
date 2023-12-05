package unlockway.unlockway_api.DTO.ingredients;

import lombok.Data;
import unlockway.unlockway_api.enums.Measure;

@Data
public class CreateIngredientDTO {
    private String id;
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