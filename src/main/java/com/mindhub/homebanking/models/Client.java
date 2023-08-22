package com.mindhub.homebanking.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Client {

    //El tipo Long representa un numero entero, pero con mas espacio de almacenamiento
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator( name = "native", strategy = "native")
    private Long id;
    private String dni;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToMany(mappedBy="clientId", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<Account>();
    @OneToMany(mappedBy="clientId", fetch=FetchType.EAGER)
    private Set<ClientLoan> loans = new HashSet<>();

    @OneToMany(mappedBy="clientId", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client(){

    }

    public Client(String dni, String firstName, String lastName, String email, String password) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account){
        account.setClientId(this);
        accounts.add(account);
    }

    public Set<ClientLoan> getLoans(){return loans;}

    public void addLoans(ClientLoan clientLoan){
        clientLoan.setClientId(this);
        loans.add(clientLoan);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void addCard(Card card){
        card.setClientId(this);
        cards.add(card);
    }
}
