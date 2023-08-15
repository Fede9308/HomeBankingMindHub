package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {

    private Long id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts = new HashSet<>();

    private Set<ClientLoanDTO> loans = new HashSet<>();

    private Set<CardDTO> cards = new HashSet<>();

    //"ClientDTO::new" === currentClient -> new ClientDTO(currentClient)
    public ClientDTO(Client client){
        this.id = client.getId();
        this.dni = client.getDni();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts()
                            .stream()
                            .map(account -> new AccountDTO(account))
                            .collect(toSet());
        this.loans = client.getLoans()
                            .stream()
                            .map(ClientLoanDTO::new)
                            .collect(toSet());
        this.cards = client.getCards()
                            .stream()
                            .map(CardDTO::new)
                            .collect(toSet());


    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
