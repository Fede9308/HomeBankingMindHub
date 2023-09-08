package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository  extends JpaRepository<Account, Long> {
    boolean existsByNumber(String accountNumber);



    Account findByNumber(String accountNumber);

    boolean existsByNumberAndClientId(String accountNumber, Client clientId);

    Account findByNumberAndClientId(String accountNumber, Client clientId);

    Account findByIdAndClientId(Long id, Client clientId);
}
