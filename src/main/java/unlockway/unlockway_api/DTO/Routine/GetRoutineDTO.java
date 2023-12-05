package unlockway.unlockway_api.DTO.Routine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.DTO.WeekRepetitionsDTO;
import unlockway.unlockway_api.models.RoutineModel;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRoutineDTO {
	private UUID id;
	private String name;
	private boolean inUsage;
	private List<GetMealsRoutineDTO> meals;
	private WeekRepetitionsDTO weekRepetitions;
	private double totalCaloriesInTheDay;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

    public GetRoutineDTO(Optional<RoutineModel> routineUpdate) {
    }
}
