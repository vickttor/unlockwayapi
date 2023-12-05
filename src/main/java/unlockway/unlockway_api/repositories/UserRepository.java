package  unlockway.unlockway_api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import unlockway.unlockway_api.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Query(nativeQuery = true, value = "SELECT * FROM tb_users WHERE email = :email")
    Optional<UserModel> findByEmail(String email);


    @Query(nativeQuery = true, value = "Select data, meals, routines, notifications, weekCalories")
    UserModel getUserAnalysis(UUID id);
}
