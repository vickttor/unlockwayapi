package  unlockway.unlockway_api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlockway.unlockway_api.models.RoutineModel;

@Repository
public interface RoutineRepository extends JpaRepository<RoutineModel, UUID> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE user_id = :userId")
    List<RoutineModel> findAllByUserId(UUID userId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tb_routine_meals trm WHERE trm.routine_id = :routineid")
    void deleteAllRoutineMealsByRoutineId(@Param("routineid") UUID routineId);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE user_id = :userId AND name LIKE %:name%")
    List<RoutineModel> findByName(UUID userId, @Param("name") String name);


    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE monday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfMonday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE tuesday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfTuesday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE wednesday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfWednesday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE thursday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfThursday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE friday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfFriday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE saturday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfSaturday();

    @Query(nativeQuery = true, value = "SELECT * FROM tb_routines WHERE sunday = 1 AND in_usage = 1")
    List<RoutineModel> findAllOfSunday();

}
