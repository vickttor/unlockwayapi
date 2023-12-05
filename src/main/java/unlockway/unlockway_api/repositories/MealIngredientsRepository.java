package unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unlockway.unlockway_api.models.relationships.MealIngredientModel;

import java.util.List;
import java.util.UUID;

@Repository
public interface MealIngredientsRepository extends JpaRepository<MealIngredientModel, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM tb_meal_ingredients WHERE meal_id = :mealId")
    List<MealIngredientModel> findByMealId(UUID mealId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_meal_ingredients tmi WHERE tmi.meal_id = :mealid")
    void deleteAllByMealId(@Param("mealid") UUID mealId);
}
