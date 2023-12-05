package unlockway.unlockway_api.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import unlockway.unlockway_api.DTO.ingredients.CreateIngredientMealDTO;
import unlockway.unlockway_api.DTO.ingredients.GetIngredientMealDTO;
import unlockway.unlockway_api.DTO.meals.GetMealDTO;
import unlockway.unlockway_api.DTO.meals.SaveMealDTO;
import unlockway.unlockway_api.azure.services.BlobStorage;
import unlockway.unlockway_api.enums.MealCategory;
import unlockway.unlockway_api.enums.Measure;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.IngredientModel;
import unlockway.unlockway_api.models.MealModel;
import unlockway.unlockway_api.models.UserModel;
import unlockway.unlockway_api.models.relationships.MealIngredientModel;
import unlockway.unlockway_api.repositories.IngredientRepository;
import unlockway.unlockway_api.repositories.MealIngredientsRepository;
import unlockway.unlockway_api.repositories.MealRepository;
import unlockway.unlockway_api.repositories.UserRepository;
import unlockway.unlockway_api.services.interfaces.IMealInterface;

@Service
@AllArgsConstructor
@Builder
public class MealService implements IMealInterface {
    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final MealIngredientsRepository mealIngredientsRepository;

    @Override
    public GetMealDTO findById(UUID id) throws ResourceNotFoundException {
        MealModel meal = mealRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Refeição não encontrada"));

        List<MealIngredientModel> mealIngredients = mealIngredientsRepository.findByMealId(id);

        return InnerJoinMealWithIngredients(meal, mealIngredients);
    }

    @Override
    public List<GetMealDTO> findByUserId(UUID userId) {

        var meals = mealRepository.findByUserModelId(userId).stream().toList();

        return meals.stream().map((meal)-> {
            List<MealIngredientModel> mealIngredients = mealIngredientsRepository.findByMealId(meal.getId());
            return InnerJoinMealWithIngredients(meal, mealIngredients);
        }).toList();
    }

    @Override
    public List<GetMealDTO> findByName(UUID userId, String name) {

        var meals = mealRepository.findByName(userId, name).stream().toList();

        return meals.stream().map((meal)-> {
            List<MealIngredientModel> mealIngredients = mealIngredientsRepository.findByMealId(meal.getId());
            return InnerJoinMealWithIngredients(meal, mealIngredients);
        }).toList();
    }

    @Override
    public List<GetMealDTO> findByCategory(UUID userId, MealCategory mealCategory) {

        var meals = mealRepository.findByCategory(userId, mealCategory).stream().toList();

        return meals.stream().map((meal)-> {
            List<MealIngredientModel> mealIngredients = mealIngredientsRepository.findByMealId(meal.getId());
            return InnerJoinMealWithIngredients(meal, mealIngredients);
        }).toList();
    }

    @Override
    @Transactional
    public GetMealDTO createMeal(SaveMealDTO saveMealDTO, Optional<MultipartFile> photo) throws ResourceNotFoundException {
        MealModel mealToBeSaved = new MealModel();

        // Setting properties
        mealToBeSaved.setName(saveMealDTO.getName());
        mealToBeSaved.setDescription(saveMealDTO.getDescription());
        mealToBeSaved.setCategory(saveMealDTO.getCategory());
        mealToBeSaved.setPreparationMethod(saveMealDTO.getPreparationMethod());
        mealToBeSaved.setCreatedAt(LocalDateTime.now());
        mealToBeSaved.setUpdatedAt(LocalDateTime.now());

        // Finding  user
        Optional<UserModel> user = userRepository.findById(saveMealDTO.getUserId());

        if(user.isPresent()) {
            mealToBeSaved.setUserModel(user.get());
        }else{
            throw new ResourceNotFoundException("Usuário referenciado na refeição não existe");
        }

        // Calculating Total Calories
        double totalCalories = getTotalCalories(saveMealDTO);

        mealToBeSaved.setTotalCalories(totalCalories);
        MealModel mealSaved;

        //  Implement Azure Communication to save the Blob File (Meal Photo)
        if(photo != null && photo.isPresent()) {
            // Maximum photo size: 1048576 bytes
            MealModel mealWithPhotoToBeCreated = StoreMealPhotoIntoAzureBlobStorage(photo.get(), mealToBeSaved);
            mealSaved  = mealRepository.save(mealWithPhotoToBeCreated);
        }else{
            deleteMealPhotoFromAzureAndMealModel(mealToBeSaved);

            mealToBeSaved.setPhoto(null);

            mealSaved = mealRepository.save(mealToBeSaved);
        }

        var mealIngredientsCreated = ApplyMealIngredients(saveMealDTO, mealSaved);

        return InnerJoinMealWithIngredients(mealSaved, mealIngredientsCreated);
    }

    @Override
    @Transactional
    public GetMealDTO updateMeal(SaveMealDTO saveMealDTO, Optional<MultipartFile> photo) throws ResourceNotFoundException {
        Optional<MealModel> meal = mealRepository.findById(saveMealDTO.getId());

        if(meal.isEmpty()) throw new ResourceNotFoundException("Essa refeição não existe ainda");
        
        MealModel mealToBeUpdated = meal.get();

        // Setting properties
        mealToBeUpdated.setName(saveMealDTO.getName());
        mealToBeUpdated.setDescription(saveMealDTO.getDescription());
        mealToBeUpdated.setCategory(saveMealDTO.getCategory());
        mealToBeUpdated.setPreparationMethod(saveMealDTO.getPreparationMethod());
        mealToBeUpdated.setUpdatedAt(LocalDateTime.now());

        // Calculating Total Calories
        double totalCalories = getTotalCalories(saveMealDTO);

        mealToBeUpdated.setTotalCalories(totalCalories);

        MealModel mealSaved;

        //  Implement Azure Communication to save the Blob File (Meal Photo)
        if(photo != null && photo.isPresent()) {
            // Maximum photo size: 1048576 bytes
            MealModel mealWithPhotoToBeCreated = StoreMealPhotoIntoAzureBlobStorage(photo.get(), mealToBeUpdated);
            mealSaved  = mealRepository.save(mealWithPhotoToBeCreated);
        }else{
            mealSaved = mealRepository.save(mealToBeUpdated);
        }

        var mealIngredientsCreated = ApplyMealIngredients(saveMealDTO, mealSaved);

        return InnerJoinMealWithIngredients(mealSaved, mealIngredientsCreated);
    }

    @Override
    @Transactional
    public String deleteMeal(UUID id) throws ResourceNotFoundException {
        MealModel mealToBeDeleted = mealRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Essa refeição não existe e portanto não pode ser deletada"));
        deleteMealPhotoFromAzureAndMealModel(mealToBeDeleted);

        mealIngredientsRepository.deleteAllByMealId(id);
        mealRepository.deleteById(id);
        return "Refeição deletada";
    }

    // Helpers

    public static double calculateCalories(double amount, Measure measure, double calories) {
        if (measure == Measure.AMOUNT) {
            // Se MEASURE for igual a "quantity", retorna calories vezes o valor de amount
            return calories * amount;
        } else {
            // Se MEASURE for diferente, calcula 1% de calories e multiplica pelo amount
            double percent = 0.01 * calories;
            return percent * amount;
        }
    }

    private double getTotalCalories(SaveMealDTO saveMealDTO) throws ResourceNotFoundException {
        double totalCalories = 0;

        for (CreateIngredientMealDTO ingredientDTO : saveMealDTO.getIngredients()) {
                IngredientModel ingredient = ingredientRepository.findById(ingredientDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Ingrediente não encontrado"));
            double calculateTotalCalories = calculateCalories(ingredientDTO.getAmount(), ingredient.getMeasure(), ingredient.getCalories());
            totalCalories += calculateTotalCalories;
            }

        return totalCalories;
    }

    private MealModel StoreMealPhotoIntoAzureBlobStorage(MultipartFile photo, MealModel mealToBeSaved) {
       deleteMealPhotoFromAzureAndMealModel(mealToBeSaved);
       String containerName = "meals";

       String filename = BlobStorage.storePhotoIntoAzureBlobStorage(photo, containerName);
       mealToBeSaved.setPhoto("https://unlockways3.blob.core.windows.net/" + containerName + "/" + filename);

       return mealToBeSaved;
    }

    private void deleteMealPhotoFromAzureAndMealModel(MealModel meal) {
        var oldPhoto = meal.getPhoto();

        if(oldPhoto != null) {
            oldPhoto = oldPhoto.substring("https://unlockways3.blob.core.windows.net/meals/".length());
            BlobStorage.removePhotoFromAzureBlobStorage(oldPhoto, "meals");
        }
    }



    private static GetMealDTO InnerJoinMealWithIngredients(MealModel mealSaved, List<MealIngredientModel> mealIngredientsCreated) {
        GetMealDTO meal = new GetMealDTO();

        meal.setId(mealSaved.getId());
        meal.setName(mealSaved.getName());
        meal.setPhoto(mealSaved.getPhoto());
        meal.setDescription(mealSaved.getDescription());
        meal.setIngredients(mealIngredientsCreated.stream().map((e)-> {
            return GetIngredientMealDTO.builder()
                    .name(e.getIngredient().getName())
                    .measure(e.getIngredient().getMeasure())
                    .amount(e.getAmount())
                    .id(e.getIngredient().getId()).build();
        }).toList());

        meal.setCategory(mealSaved.getCategory());
        meal.setPreparationMethod(mealSaved.getPreparationMethod());
        meal.setTotalCalories(mealSaved.getTotalCalories());
        meal.setCreatedAt(mealSaved.getCreatedAt());
        meal.setUpdatedAt(mealSaved.getUpdatedAt());

        return meal;
    }

    private List<MealIngredientModel> ApplyMealIngredients(SaveMealDTO saveMealDTO, MealModel mealSaved) throws ResourceNotFoundException {

        mealIngredientsRepository.deleteAllByMealId(mealSaved.getId());

        List<MealIngredientModel> mealIngredients = new ArrayList<>();

        for (CreateIngredientMealDTO ingredientDTO : saveMealDTO.getIngredients()) {
            IngredientModel ingredient = ingredientRepository.findById(ingredientDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Ingrediente não encontrado"));

            MealIngredientModel mealIngredientModel = new MealIngredientModel();

            mealIngredientModel.setAmount(ingredientDTO.getAmount());
            mealIngredientModel.setIngredient(ingredient);
            mealIngredientModel.setMealId(mealSaved.getId());

            mealIngredients.add(mealIngredientModel);

        }

        return mealIngredientsRepository.saveAll(mealIngredients);
    }
}
