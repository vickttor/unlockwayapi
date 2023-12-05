package unlockway.unlockway_api.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import unlockway.unlockway_api.DTO.SendNotificationPayloadDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseCloudMessagingService {
    public void sendNotification(String deviceToken, SendNotificationPayloadDTO data) {
        System.out.println("\nENTROU NO MÉTODO\n");

        try {
            Map<String, String> payload = new HashMap<>(); // In nowdays is empty but we can put here everything we want.

            String placeholder = "https://unlockways3.blob.core.windows.net/placeholders/unlockway-logo.png";

            Notification notification = Notification.builder()
                    .setTitle(data.getTitle())
                    .setBody(data.getBody())
                    .setImage(data.getImage() == null ? placeholder : data.getImage())
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .putAllData(payload)
                    .setToken(deviceToken)
                    .build();

            System.out.println("\nENVIOU NOTIFICAÇÃO\n");
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}