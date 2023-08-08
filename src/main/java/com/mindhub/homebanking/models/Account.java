package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;

    private Double balance;

    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client clientId;


    public Account(){

    }

    public Account(String number, Double balance, LocalDate creationDate, Client clientId){

        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.clientId = clientId;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }
}
