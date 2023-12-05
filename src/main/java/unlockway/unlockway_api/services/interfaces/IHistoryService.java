package unlockway.unlockway_api.services.interfaces;

import java.util.List;
import java.util.UUID;

import unlockway.unlockway_api.DTO.history.GetHistoryDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;

public interface IHistoryService {
  List<GetHistoryDTO> findByUserId(UUID userId) throws ResourceNotFoundException;
  void toggleMealAsIngested(UUID routineId, UUID mealId) throws  ResourceNotFoundException;
}
