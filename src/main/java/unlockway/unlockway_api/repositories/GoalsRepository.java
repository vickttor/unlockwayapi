package unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unlockway.unlockway_api.models.relationships.GoalsModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalsRepository extends JpaRepository<GoalsModel, UUID>  {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_goals WHERE user_id = :userid")
    Optional<GoalsModel> findByUserId(@Param("userid") UUID userId);

}
