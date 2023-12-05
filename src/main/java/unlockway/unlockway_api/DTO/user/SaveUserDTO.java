package unlockway.unlockway_api.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.enums.Biotype;
import unlockway.unlockway_api.enums.Sex;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SaveUserDTO {
    String firstname;
    String lastname;
    String email;
    String password;
    Double height;
    Double weight;
    UserGoalsDTO goals;
    Biotype biotype;
    Sex sex;
    String deviceToken;
}
