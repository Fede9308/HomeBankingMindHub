package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
//import com.mindhub.homebanking.repositories.AccountRepository;
//import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){

        return accountService.getAccountsDTO();
    }

    @Autowired
    private ClientService clientService;

     @GetMapping("accounts/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable Long id, Authentication authentication){
            Client client = clientService.findByEmail(authentication.getName());
            Account account = accountService.findById(id);
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
     @PostMapping("/clients/current/accounts")
     public ResponseEntity<Object> createAccount(Authentication authentication) {
         Client client = clientService.findByEmail(authentication.getName());

         if (client.getAccounts().size()>=3){
             return new ResponseEntity<>("Alcanzo el máximo número de cuentas permitidas", HttpStatus.FORBIDDEN);
         }
         Account account = new Account(accountService.getAccountNumber(), 0.0, LocalDateTime.now() );
         client.addAccount(account);
         //accountRepository.save(account);
         accountService.save(account);
         return new ResponseEntity<>("Cuenta creada con éxito", HttpStatus.CREATED);
     }
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        List<AccountDTO> currentAccounts = client.getAccounts().stream()
                                                                .map(AccountDTO::new)
                                                                .collect(toList());
        return currentAccounts;
    }

}
