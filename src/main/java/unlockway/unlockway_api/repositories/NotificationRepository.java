package unlockway.unlockway_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unlockway.unlockway_api.models.NotificationModel;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM tb_notifications WHERE user_id = :userId")
    List<NotificationModel> findByUserId(UUID userId);
}
