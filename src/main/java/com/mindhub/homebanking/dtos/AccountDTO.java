package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;
    private String number;
    private Double balance;
    private LocalDate date;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO(Account account){

        id = account.getId();
        number = account.getNumber();
        balance = account.getBalance();
        date = account.getDate();
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

    public LocalDate getDate() {
        return date;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
