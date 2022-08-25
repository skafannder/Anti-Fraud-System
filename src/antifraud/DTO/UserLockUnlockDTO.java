package antifraud.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserLockUnlockDTO {

    private String status;

    public UserLockUnlockDTO(String status) {
        this.status = status;
    }
}
