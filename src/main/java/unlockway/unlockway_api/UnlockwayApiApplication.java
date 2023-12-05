package unlockway.unlockway_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UnlockwayApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UnlockwayApiApplication.class, args);
	}
}
