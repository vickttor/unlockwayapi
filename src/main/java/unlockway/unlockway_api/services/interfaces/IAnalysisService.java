package unlockway.unlockway_api.services.interfaces;

import unlockway.unlockway_api.DTO.AnalysisDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;

import java.util.UUID;

public interface IAnalysisService {
    AnalysisDTO getAnalysis(UUID userId) throws ResourceNotFoundException;
}
