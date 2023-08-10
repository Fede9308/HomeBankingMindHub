package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;
    private String number;
    private Double balance;
    private LocalDate creationDate;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account){

        id = account.getId();
        number = account.getNumber();
        balance = account.getBalance();
        creationDate = account.getCreationDate();
        transactions = account.getTransactions()
                                .stream()
                                .map(transaction -> new TransactionDTO(transaction))
                                .collect(toSet());

    }
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
