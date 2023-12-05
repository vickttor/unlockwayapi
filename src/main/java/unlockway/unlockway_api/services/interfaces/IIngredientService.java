package unlockway.unlockway_api.services.interfaces;

import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.IngredientModel;

import java.util.List;
import java.util.UUID;

public interface IIngredientService {
  List<IngredientModel> findAll();
  List<IngredientModel> findByName(String name);
  IngredientModel findById(UUID id) throws ResourceNotFoundException;
  IngredientModel createFood(IngredientModel ingredient);
}