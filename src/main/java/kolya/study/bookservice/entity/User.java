package kolya.study.bookservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
//@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String activationCode;
    private boolean activated;
    private String profileImage;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "user_roles"
    , joinColumns = @JoinColumn(name = "user_id")
    , inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> authorities = new ArrayList<>();

    public User(Long id, String username, String password, String email, String activationCode, boolean activated, String profileImage, List<Role> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.activationCode = activationCode;
        this.activated = activated;
        this.profileImage = profileImage;
        this.authorities = authorities;
    }

    public User(boolean activated) {
        this.activated = activated;
    }
}
