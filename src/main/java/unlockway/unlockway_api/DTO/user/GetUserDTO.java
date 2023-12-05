package unlockway.unlockway_api.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.Biotype;
import unlockway.unlockway_api.enums.Sex;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUserDTO {
    UUID id;
    String firstname;
    String lastname;
    String photo;
    String email;
    double height;
    double weight;
    double imc;
    UserGoalsDTO goals;
    Biotype biotype;
    Sex sex;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
