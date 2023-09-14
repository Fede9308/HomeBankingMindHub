package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void emailNotNull() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, allOf(everyItem(hasProperty("email", notNullValue()))));
    }

    @Test
    public void existAccounts (){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }
  /*  @Test
    public void clientAccountMaximum(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, allOf(hasItem(hasProperty("accounts", lessThan(4)))));
    }*/

    @Test
    public void clientAccountMaximum() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasAccountsLessThan(4)));
    }

    // Custom matcher to check the size of accounts for a Client
    private Matcher<Client> hasAccountsLessThan(int maxAccounts) {
        return new TypeSafeMatcher<Client>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Client has fewer than " + maxAccounts + " accounts");
            }

            @Override
            protected boolean matchesSafely(Client client) {
                return client.getAccounts().size() < maxAccounts;
            }
        };
    }
    @Test
    public void clientIdNotNull (){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, allOf(everyItem(hasProperty("clientId", notNullValue()))));
    }

    @Test
    public void existCards (){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void cardCvvCorrect (){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, allOf(everyItem(hasProperty("cvv", greaterThan(99)))));
        assertThat(cards, allOf(everyItem(hasProperty("cvv", lessThan(1000)))));
    }

    @Test
    public void existTransactions (){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test public void descriptionNotNull (){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, allOf(everyItem(hasProperty("description", notNullValue()))));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existHipotecarioLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Hipotecario"))));
    }

    @Test
    public void existClientLoans (){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans, is(not(empty())));
    }

    @Test
    public void clientNotNull (){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans, allOf(everyItem(hasProperty("clientId", notNullValue()))));
    }


}
