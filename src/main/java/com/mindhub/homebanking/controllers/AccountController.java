package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        List<Account> allAccounts = accountRepository.findAll();

        List<AccountDTO> accountsDTOList = allAccounts.stream()
                                                .map(AccountDTO::new)
                                                .collect(toList());
        return accountsDTOList;
    }

    @Autowired
    private ClientRepository clientRepository;

     @GetMapping("accounts/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id, Authentication authentication){
            Client client = clientRepository.findByEmail(authentication.getName());
            Account account = accountRepository.findById(id).orElse(null);
            if (account == null){
                return new ResponseEntity<>("Cuenta no encontrada",HttpStatus.BAD_GATEWAY);
            }
            if (account.getClientId().equals(client)){
                AccountDTO accountDTO = new AccountDTO(account);
                return new ResponseEntity<>(accountDTO, HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Esta cuenta no le pertenece",HttpStatus.I_AM_A_TEAPOT);
            }




     }

}
