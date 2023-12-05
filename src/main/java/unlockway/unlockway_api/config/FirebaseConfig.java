package unlockway.unlockway_api.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import unlockway.unlockway_api.firebase.FirebaseInitializer;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FirebaseInitializer firebaseInitializer;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        firebaseInitializer.initialize();
    }
}