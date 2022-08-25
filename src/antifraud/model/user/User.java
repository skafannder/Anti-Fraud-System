package antifraud.model.user;

import antifraud.request.SignupRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotEmpty
    private String name;

    @Column
    @NotEmpty
    private String username;

    @Column
    @NotEmpty
    private String password;

    @Column
    @NotNull
    private UserRole role;

    @Column
    @NotNull
    private boolean isAccountNonLocked;


    public User(String name, String username, String password, UserRole role, boolean isAccountNonLocked) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public User(SignupRequest request, UserRole role, boolean isAccountNonLocked) {
        this.name = request.getName();
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.role = role;
        this.isAccountNonLocked = isAccountNonLocked;
    }
}