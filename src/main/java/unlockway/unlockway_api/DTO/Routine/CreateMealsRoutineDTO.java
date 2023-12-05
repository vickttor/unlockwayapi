package unlockway.unlockway_api.DTO.Routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMealsRoutineDTO {
    private UUID idMeal;
    private LocalTime notifyAt;
}
