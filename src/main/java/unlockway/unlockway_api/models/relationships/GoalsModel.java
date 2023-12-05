package unlockway.unlockway_api.models.relationships;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unlockway.unlockway_api.models.UserModel;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_goals")
public class GoalsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "goals")
    private UserModel userModel;

    @Column(name = "gain_muscular_mass")
    private boolean gainMuscularMass;
    @Column(name = "maintain_health")
    private boolean maintainHealth;
    @Column(name = "lose_weight")
    private boolean loseWeight;
}
