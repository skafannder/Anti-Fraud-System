package antifraud.service;


import antifraud.DTO.CardDTO;
import antifraud.DTO.CardRemovedDTO;
import antifraud.model.Card;
import antifraud.repository.CardRepository;

import antifraud.request.CardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Component
@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    public ResponseEntity<CardDTO> register(CardRequest request) {
        String number = request.getNumber();
        if (!isCardValid(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid number");
        }
        if (cardRepository.existsByNumber(number)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Number already in database");
        }
        Card cardEntity = new Card();
        cardEntity.setNumber(number);
        cardRepository.save(cardEntity);
        return new ResponseEntity<>(new CardDTO(cardEntity.getId(), number), HttpStatus.OK);
    }

    public ResponseEntity<CardRemovedDTO> remove(String number) {
        if (!isCardValid(number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid number");
        }
        if (!cardRepository.existsByNumber(number)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Number doesn't exist");
        }
        Card cardEntity = cardRepository.findByNumber(number).get();
        cardRepository.delete(cardEntity);
        return new ResponseEntity<>(new CardRemovedDTO(String.format("Card %s successfully removed!", number)), HttpStatus.OK);
    }

    public ResponseEntity<List<CardDTO>> findAll() {
        ArrayList returnList= new ArrayList();
        var sortedCardsFromRepository = cardRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        sortedCardsFromRepository.forEach(s -> returnList.add(s));
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    public boolean isCardValid(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
