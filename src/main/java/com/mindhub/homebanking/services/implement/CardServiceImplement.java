package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Override
    public boolean existsByCvv(Integer cardCvv) {
      return   cardRepository.existsByCvv(cardCvv);
    }

    @Override
    public boolean existsByNumber(String cardNumber){
        return cardRepository.existsByNumber(cardNumber);
    }

    @Override
    public void save(Card card){
        cardRepository.save(card);
    }




}
