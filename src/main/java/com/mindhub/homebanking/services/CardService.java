package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import org.springframework.beans.factory.annotation.Autowired;

public interface CardService {


    boolean existsByCvv(Integer cardCvv);
    boolean existsByNumber(String cardNumber);
    void save(Card card);

}
