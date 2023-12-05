package unlockway.unlockway_api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlockway.unlockway_api.enums.MealCategory;
import unlockway.unlockway_api.models.MealModel;

@Repository
public interface MealRepository extends JpaRepository<MealModel, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM tb_meals WHERE user_id = :userId")
    List<MealModel> findByUserModelId(UUID userId);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_meals WHERE user_id = :userId AND category = :#{#category.name()}")
    List<MealModel> findByCategory(UUID userId, @Param("category") MealCategory mealCategory);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_meals WHERE user_id = :userId AND name LIKE %:name%")
    List<MealModel> findByName(UUID userId, @Param("name") String name);
}
