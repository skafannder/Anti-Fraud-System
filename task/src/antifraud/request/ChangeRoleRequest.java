package antifraud.request;


import antifraud.model.UserRole;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChangeRoleRequest {

    @NotEmpty
    private final String username;

    @NotEmpty
    private final String role;

    @JsonCreator
    public ChangeRoleRequest(
            @JsonProperty("username") String username,
            @JsonProperty("role") String role) {

        this.username = username;
        this.role = role;
    }
}