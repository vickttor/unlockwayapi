package unlockway.unlockway_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import unlockway.unlockway_api.enums.Biotype;
import unlockway.unlockway_api.enums.Role;
import unlockway.unlockway_api.enums.Sex;
import unlockway.unlockway_api.models.relationships.GoalsModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String password;
    @Column(columnDefinition = "ENUM('MALE', 'FEMALE')")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String photo;

    private String firstname;
    private String lastname;

    private double height;
    private double weight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goals_id", referencedColumnName = "id")
    private GoalsModel goals;

    @Column(columnDefinition = "ENUM('ECTOMORPH', 'ENDOMORPH', 'MESOMORPH')")
    @Enumerated(EnumType.STRING)
    private Biotype biotype;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public double generateImc() {
        return this.weight / (this.height * this.height);
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    private String deviceToken;

    // Implementations

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
