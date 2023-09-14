package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @PostMapping("/clients/current/cards")
        public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                                 @RequestParam CardColor cardColor,
                                                 Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());

        Set<Card> cards = client.getCards();

        if (!cards.stream().filter(card -> card.getType().equals(cardType))
                            .filter(card -> card.getColor().equals(cardColor))
                            .collect(toList()).isEmpty()) {

            return new ResponseEntity<>("Ya posee una tarjeta de este tipo", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.getFirstName()+" "+client.getLastName(),
                cardType, cardColor, cardService.getCardNumber(), cardService.getCardCvv(), LocalDateTime.now().plusYears(5), LocalDateTime.now());

        client.addCard(card);
        cardService.save(card);
        return new ResponseEntity<>("Tarjeta creada con éxito", HttpStatus.CREATED);


    }
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        List<CardDTO> currentCards = client.getCards().stream()
                                                        .map(CardDTO::new)
                                                        .collect(toList());
        return currentCards;
    }

}
