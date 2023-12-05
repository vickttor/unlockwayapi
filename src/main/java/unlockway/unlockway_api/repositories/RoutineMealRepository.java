package unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unlockway.unlockway_api.models.relationships.RoutineMealModel;

import java.util.List;
import java.util.UUID;

public interface RoutineMealRepository extends JpaRepository<RoutineMealModel, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tb_routine_meals WHERE routine_id = :routineId AND meal_id = :mealId"
    )
    List<RoutineMealModel> findAllByRoutineIdAndMealId(UUID routineId, UUID mealId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tb_routine_meals WHERE routine_id = :routineId"
    )
    List<RoutineMealModel> findAllByRoutineId(UUID routineId);
}
