package unlockway.unlockway_api.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unlockway.unlockway_api.DTO.GetNotificationsDTO;
import unlockway.unlockway_api.models.NotificationModel;
import unlockway.unlockway_api.services.impl.NotificationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/findByUserId")
    public ResponseEntity<?> findNotificationsByUserId(@RequestParam("id") UUID id){
        try {
            List<GetNotificationsDTO> notifications = notificationService.findAllNotificationsByUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable("id") UUID id){
        try {
            notificationService.markNotificationAsRead(id);
            return ResponseEntity.status(HttpStatus.OK).body("Read");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
