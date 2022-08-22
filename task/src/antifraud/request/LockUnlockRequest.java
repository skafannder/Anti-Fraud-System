package antifraud.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LockUnlockRequest {

    @NotEmpty
    private final String username;

    @NotEmpty
    private final String operation;

    @JsonCreator
    public LockUnlockRequest(
            @JsonProperty("username") String username,
            @JsonProperty("operation") String operation) {

        this.username = username;
        this.operation = operation;
    }
}