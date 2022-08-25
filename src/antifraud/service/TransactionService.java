package antifraud.service;

import antifraud.DTO.TransactionDTO;
import antifraud.repository.CardRepository;
import antifraud.repository.IpRepository;
import antifraud.repository.UserRepository;
import antifraud.request.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private IpRepository ipRepository;

    public ResponseEntity<TransactionDTO> process(TransactionRequest request) {
        List<String> errors = checkForErrors(request);
        String info = generateInfo(errors);
        String result = generateResult(request.getAmount(), errors);
        if (result.equals("MANUAL_PROCESSING")) info = "amount";

        TransactionDTO dto = new TransactionDTO(result, info);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private List<String> checkForErrors(TransactionRequest request) {
        List<String> errors = new ArrayList<>();
        long amount = request.getAmount();
        if (amount <= 0 || amount > 1500) {
            errors.add("amount");
        }
        if (cardRepository.existsByNumber(request.getNumber())) {
            errors.add("card-number");
        }
        if (ipRepository.existsByIp(request.getIp())) {
            errors.add("ip");
        }

        return errors;
    }

    private String generateInfo(List errors) {
        String result;
        if (errors.isEmpty()) result = "none";
        else {
            StringBuilder infoSB = new StringBuilder();
            errors.forEach(s -> infoSB.append(s + ", "));
            result = infoSB.deleteCharAt(infoSB.length() - 2).toString().trim();
        }
        return result;
    }

    private String generateResult(long amount, List errors) {
        String result = "";
        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be larger than 0");
        } else if (amount > 1500 || errors.contains("card-number") || errors.contains("ip")) {
            result = "PROHIBITED";
        } else if (amount > 200) {
            result = "MANUAL_PROCESSING";
        } else {
            result = "ALLOWED";
        }
        return result;
    }
}
