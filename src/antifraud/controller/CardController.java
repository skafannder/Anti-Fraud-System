package antifraud.controller;

import antifraud.DTO.CardDTO;
import antifraud.DTO.CardRemovedDTO;
import antifraud.request.CardRequest;
import antifraud.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping(value = "/api/antifraud/stolencard")
    public ResponseEntity<CardDTO> registerCard(@Valid @RequestBody CardRequest request) {
        return cardService.register(request);
    }

    @DeleteMapping(value = "/api/antifraud/stolencard/{number}")
    public ResponseEntity<CardRemovedDTO> removeCard(@Valid @PathVariable String number) {
        return cardService.remove(number);
    }

    @GetMapping(value = "/api/antifraud/stolencard")
    public ResponseEntity<List<CardDTO>> getCards() {
        return cardService.findAll();
    }

}
