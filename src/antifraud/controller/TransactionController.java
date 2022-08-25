package antifraud.controller;

import antifraud.DTO.TransactionDTO;
import antifraud.request.TransactionRequest;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/api/antifraud/transaction")
    public ResponseEntity<TransactionDTO> validateTransaction(@Valid @RequestBody TransactionRequest request) {
        return transactionService.process(request);
    }
}