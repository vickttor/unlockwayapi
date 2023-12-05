package unlockway.unlockway_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import unlockway.unlockway_api.DTO.history.GetHistoryDTO;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.services.impl.HistoryService;

@RestController
@RequestMapping("/api/v1/history")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HistoryController {

    private final HistoryService service;
    
    @GetMapping("/{idUser}")
    public ResponseEntity<?> findByIDUser(@PathVariable UUID idUser){
        try {
            List<GetHistoryDTO> histories = service.findByUserId(idUser);

            return ResponseEntity.status(HttpStatus.OK).body(histories);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/ingested")
    public ResponseEntity<?> findByIDUser(@RequestParam("routine") UUID routineId, @RequestParam("meal") UUID mealId){
        try {
            service.toggleMealAsIngested(routineId, mealId);
            return ResponseEntity.status(HttpStatus.OK).body("Refeição Ingerida");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
