package unlockway.unlockway_api.models.relationships;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import unlockway.unlockway_api.models.IngredientModel;

import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Table(name = "tb_meal_ingredients")
public class MealIngredientModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "meal_id")
    private UUID mealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private IngredientModel ingredient;

    @ColumnDefault(value = "0")
    private double amount;
}
