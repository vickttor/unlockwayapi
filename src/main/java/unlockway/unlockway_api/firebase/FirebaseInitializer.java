package unlockway.unlockway_api.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitializer {

    public void initialize() throws IOException {
        try {
            // Load the resource using ClassPathResource
            Resource resource = new ClassPathResource("unlockway_firebase.json");

            // Get the File object from the resource
            File file = resource.getFile();

            FileInputStream serviceAccount = new FileInputStream(file);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }catch(Exception e) {
            System.out.println(e);
        }

    }
}
