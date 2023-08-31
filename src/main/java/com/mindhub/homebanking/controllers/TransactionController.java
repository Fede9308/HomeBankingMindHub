package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        List<Transaction> allTransactions = transactionRepository.findAll();

        List<TransactionDTO> transactionDTOList = allTransactions.stream()
                .map(TransactionDTO::new)
                .collect(toList());
        return transactionDTOList;
    }

    @GetMapping("transactions/{id}")
    public ResponseEntity<Object> getTransactionById(@PathVariable Long id, Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null){

            return new ResponseEntity<>("Transacción no encontrada", HttpStatus.BAD_GATEWAY);
        }

        Account account = transaction.getAccountId();

        if(account.getClientId().equals(client)){
            TransactionDTO transactionDTO = new TransactionDTO(transaction);
            return new ResponseEntity<>(transactionDTO,HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>("Esta transacción no le pertenece",HttpStatus.I_AM_A_TEAPOT);
        }

    }

    @Autowired
    ClientRepository clientRepository;
     @Autowired
    AccountRepository accountRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> makeTransaction(@RequestParam String description,
                                                  @RequestParam Double amount,
                                                  @RequestParam String fromAccountNumber,
                                                  @RequestParam String toAccountNumber,
                                                  Authentication authentication){

        if (description.isBlank()){
            return new ResponseEntity<>("Falta descripción", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0.0){
            return new ResponseEntity<>("El monto debe ser mayo a 0", HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals("VIN")){
            return new ResponseEntity<>("El número de la cuenta de origen no fue ingresado", HttpStatus.FORBIDDEN);
        }
        if (toAccountNumber.equals("VIN")){
            return new ResponseEntity<>("El número de la cuenta de destino no fue ingresado", HttpStatus.FORBIDDEN);
        }

        Client debitClient = clientRepository.findByEmail(authentication.getName());
        Client creditClient = accountRepository.findByNumber(toAccountNumber).getClientId();

        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("La ceunta de origen y de destino deben ser distintas", HttpStatus.FORBIDDEN);
        }

        if (accountRepository.findByNumber(fromAccountNumber) == null){
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumberAndClientId(fromAccountNumber, debitClient) == null){
            return new ResponseEntity<>("La cuenta de origen no pertenece al usuario autenticado", HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(toAccountNumber) == null){
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        if (amount > accountRepository.findByNumberAndClientId(fromAccountNumber, debitClient).getBalance()){
            return new ResponseEntity<>("Saldo insufuciente",HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,amount,description, LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());

        Account debitAccount = accountRepository.findByNumber(fromAccountNumber);
        debitAccount.addTransaction(debitTransaction);
        transactionRepository.save(debitTransaction);
        Double debitAccountBalance = debitAccount.getBalance();
        debitAccount.setBalance(debitAccountBalance - amount);

        Account creditAccount = accountRepository.findByNumber(toAccountNumber);
        creditAccount.addTransaction(creditTransaction);
        transactionRepository.save(creditTransaction);
        Double creditAccountBalance = creditAccount.getBalance();
        creditAccount.setBalance(creditAccountBalance + amount);

        return new ResponseEntity<>("Transacción realizada", HttpStatus.ACCEPTED);

    }



}
