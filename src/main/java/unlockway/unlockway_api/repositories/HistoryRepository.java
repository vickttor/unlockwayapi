package unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unlockway.unlockway_api.models.HistoryModel;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository  extends JpaRepository<HistoryModel, UUID>  {
  @Query(nativeQuery = true, value = "SELECT * FROM tb_history WHERE user_id = :userid")
  List<HistoryModel> findByIDUser(@Param("userid") UUID userId);

  @Query(nativeQuery = true, value = "SELECT * FROM tb_history WHERE routine_id = :routineid AND routine_meal_id = :routinemealid")
  List<HistoryModel> findByRoutineAndMeal(@Param("routineid") UUID routineId, @Param("routinemealid") UUID routineMealId);
}
