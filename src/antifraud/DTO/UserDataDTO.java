package antifraud.DTO;

import antifraud.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDataDTO {

    private Long id;
    private final String name;
    private final String username;
    private final String role;


    public static UserDataDTO create(User user) {
        return new UserDataDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole().name()

        );
    }
}