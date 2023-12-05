package unlockway.unlockway_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unlockway.unlockway_api.DTO.meals.GetMealDTO;
import unlockway.unlockway_api.DTO.meals.SaveMealDTO;
import unlockway.unlockway_api.enums.MealCategory;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.services.impl.MealService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
public class MealController {

    private final MealService mealService;

    @GetMapping("/findById")
    public  ResponseEntity<?> findById(@RequestParam("id") UUID id){
        try {
            GetMealDTO meal = mealService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(meal);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/findByUserId")
    public  ResponseEntity<List<GetMealDTO>> findByUserId(@RequestParam("id") UUID userId){
        List<GetMealDTO> meals = mealService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(meals);
    }

    @GetMapping("/findByCategory")
    public  ResponseEntity<List<GetMealDTO>> findByCategory(@RequestParam("userId") UUID userId, @RequestParam("category") MealCategory mealCategory){
        List<GetMealDTO> meals = mealService.findByCategory(userId, mealCategory);
        return ResponseEntity.status(HttpStatus.OK).body(meals);
    }

    @GetMapping("/findByName")
    public  ResponseEntity<List<GetMealDTO>> findByName(@RequestParam("userId") UUID userId, @RequestParam("name") String name){
        List<GetMealDTO> meals = mealService.findByName(userId, name);
        return ResponseEntity.status(HttpStatus.OK).body(meals);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createMeal(@RequestParam("payload") String createMealDTOAsJson, @RequestParam(value = "photo", required = false) Optional<MultipartFile> photo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SaveMealDTO saveMealDTO = objectMapper.readValue(createMealDTOAsJson, SaveMealDTO.class);

            GetMealDTO createdMeal = mealService.createMeal(saveMealDTO, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMeal);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "")
    public ResponseEntity<?> updateMeal(@RequestParam("payload") String updateMealDTOAsJson, @RequestParam("photo") Optional<MultipartFile> photo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SaveMealDTO saveMealDTO = objectMapper.readValue(updateMealDTOAsJson, SaveMealDTO.class);

            GetMealDTO updatedMeal = mealService.updateMeal(saveMealDTO, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedMeal);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteMeal(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(mealService.deleteMeal(id));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
