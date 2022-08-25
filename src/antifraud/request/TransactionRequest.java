package antifraud.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class TransactionRequest {
    @NotNull
    private Long amount;
    @NotEmpty
    private String ip;
    @NotEmpty
    private String number;
}