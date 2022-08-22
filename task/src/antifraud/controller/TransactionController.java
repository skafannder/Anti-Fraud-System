package antifraud.controller;

import antifraud.request.TransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TransactionController {

    @PostMapping(value = "/api/antifraud/transaction")
    public ResponseEntity<?> validateTransaction(@RequestBody TransactionRequest transaction) {

        Long amount = transaction.getAmount();
        Map<String, String> response = new HashMap<>();
        String result;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);


        if (amount == null || amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (amount > 1500) {
            result = "PROHIBITED";
        } else if (amount > 200) {
            result = "MANUAL_PROCESSING";
        } else {
            result = "ALLOWED";
        }

        response.put("result", result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}