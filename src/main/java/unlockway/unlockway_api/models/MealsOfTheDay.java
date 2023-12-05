package unlockway.unlockway_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_meals_of_the_day")
public class MealsOfTheDay {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "routine_meal_id")
    private UUID routineMealId;
}
