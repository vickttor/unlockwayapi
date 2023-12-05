package unlockway.unlockway_api.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import unlockway.unlockway_api.DTO.Routine.CreateRoutineDTO;
import unlockway.unlockway_api.DTO.Routine.GetRoutineDTO;
import unlockway.unlockway_api.DTO.Routine.UpdateRoutineDTO;
import unlockway.unlockway_api.services.impl.RoutinesService;

@RestController
@RequestMapping("/api/v1/routines")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
public class RoutinesController {

    private final RoutinesService routinesService;

    @GetMapping("/userId")
    public ResponseEntity<?> findRoutinesById(@RequestParam("id") UUID id) {
        try {
            List<GetRoutineDTO> response = routinesService.getRoutinesByUserId(id);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/findByName")
    public  ResponseEntity<?> findByCategory(@RequestParam("userId") UUID userId, @RequestParam("name") String name){
        try {
            List<GetRoutineDTO> routines = routinesService.findByName(userId, name);
            return ResponseEntity.status(HttpStatus.OK).body(routines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/inusage")
    public  ResponseEntity<?> findRoutineInUsage(@RequestParam("userId") UUID userId){
        try {
            GetRoutineDTO routine = routinesService.getRoutineInUsageByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(routine);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> UpdateRoutine(@RequestBody UpdateRoutineDTO updateDTO) {
        try {
            routinesService.updateRoutine(updateDTO);

            return ResponseEntity.status(HttpStatus.OK).body("Rotina atualizada!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/use")
    public ResponseEntity<String> UpdateRoutine(@RequestParam(name = "user") UUID userId, @RequestParam("routine") UUID id) {
        try {
            routinesService.routineInUsage(userId, id);

            return ResponseEntity.status(HttpStatus.OK).body("Nova rotina selecionada!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("")
    public ResponseEntity<?> CreateRoutine(@RequestBody CreateRoutineDTO routineCreate) {
        try {
            GetRoutineDTO response = routinesService.createRoutines(routineCreate);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteMeal(@PathVariable UUID id) {
        try {
            routinesService.deleteRoutine(id);

            return ResponseEntity.status(HttpStatus.OK).body("Rotina deletada");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
