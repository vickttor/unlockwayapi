package unlockway.unlockway_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalysisDTO {
    private int meals;
    private int routines;
    private int notifications;
    private List<Double> weekCalories;
}
