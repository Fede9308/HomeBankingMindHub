package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();
    Account findByNumberAndClientId(String accountNumber, Client clientId);

    Account findById(Long id);
    void save(Account account);

    boolean existsByNumber(String accountNumber);

    Account findByNumber(String accountNumber);

    boolean existsByNumberAndClientId(String accountNumber, Client client);



}
