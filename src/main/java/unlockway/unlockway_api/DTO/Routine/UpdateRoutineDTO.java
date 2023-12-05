package unlockway.unlockway_api.DTO.Routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.DTO.WeekRepetitionsDTO;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoutineDTO {
    private UUID id;
    private String name;
    private Boolean inUsage;
    private List<CreateMealsRoutineDTO> meals;
    private WeekRepetitionsDTO weekRepetitions;
}
