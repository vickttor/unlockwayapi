package unlockway.unlockway_api.services.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import unlockway.unlockway_api.DTO.Routine.CreateRoutineDTO;
import unlockway.unlockway_api.DTO.Routine.GetRoutineDTO;
import unlockway.unlockway_api.DTO.Routine.UpdateRoutineDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.RoutineModel;

public interface IRoutinesService {
	List<GetRoutineDTO> getRoutinesByUserId(UUID id);
    List<GetRoutineDTO> findByName(UUID userId, String name);
    GetRoutineDTO createRoutines(CreateRoutineDTO routineCreate) throws ResourceNotFoundException;
    void updateRoutine(UpdateRoutineDTO updateDTO) throws ResourceNotFoundException;
    void deleteRoutine(UUID id) throws ResourceNotFoundException;

    void routineInUsage(UUID userId, UUID id) throws ResourceNotFoundException;
    GetRoutineDTO getRoutineInUsageByUserId(UUID userId) throws ResourceNotFoundException;
}
