package antifraud.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter // Maybe not needed
public class SignupRequest {

    @NotEmpty
    private final String name;

    @NotEmpty
    private final String username;

    @NotEmpty
    private final String password;

    @JsonCreator
    public SignupRequest(
            @JsonProperty("name") String name,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password) {

        this.name = name;
        this.username = username;
        this.password = password;
    }
}