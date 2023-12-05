package  unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unlockway.unlockway_api.models.IngredientModel;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientModel, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_ingredients WHERE name LIKE %:name%")
    List<IngredientModel> findByName(@Param("name") String name);
}
