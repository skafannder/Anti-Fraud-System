package antifraud.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class IPrequest {

    @Getter
    @Setter
    @NotEmpty
    private String ip;
}
