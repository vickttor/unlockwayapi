package unlockway.unlockway_api.models.relationships;

import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.models.MealModel;
import unlockway.unlockway_api.models.RoutineModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_routine_meals")
public class RoutineMealModel {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "meal_id")
	private MealModel meal;
	
	@ManyToOne
	@JoinColumn(name = "routine_id")
	private RoutineModel routine;

	private LocalTime notifyAt;
}
