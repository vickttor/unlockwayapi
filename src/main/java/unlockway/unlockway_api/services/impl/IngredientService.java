package  unlockway.unlockway_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.exceptions.ResourceNotFoundException;
import unlockway.unlockway_api.models.IngredientModel;
import unlockway.unlockway_api.repositories.IngredientRepository;
import unlockway.unlockway_api.services.interfaces.IIngredientService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientService implements IIngredientService {

    private final IngredientRepository repository;

    @Override
    public List<IngredientModel> findAll(){
        return repository.findAll();
    }

    @Override
    public List<IngredientModel> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public IngredientModel findById(UUID id) throws ResourceNotFoundException {
        return repository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Ingrediente não encontrado!"));
    }

    @Override
    public IngredientModel createFood(IngredientModel food) {
        return repository.save(food);
    }

}
