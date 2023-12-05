package unlockway.unlockway_api.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unlockway.unlockway_api.services.impl.AnalysisService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("")
    public ResponseEntity<?> getAnalysis(@RequestParam("userId") UUID userId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(analysisService.getAnalysis(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
