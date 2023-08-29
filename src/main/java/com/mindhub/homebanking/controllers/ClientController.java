package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        List<Client> allClients = clientRepository.findAll();

        //"ClientDTO::new" === currentClient -> new ClientDTO(currentClient)
        List<ClientDTO> convertedList = allClients
                                                .stream()
                                                .map(client -> new ClientDTO(client))
                                                .collect(toList());

        return  convertedList;
    }
    @GetMapping("clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        Optional<Client> client = clientRepository.findById(id);
        return new ClientDTO(client.get());
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountController accountController;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
        @RequestParam String firstName, @RequestParam String lastName,
        @RequestParam String email, @RequestParam String password){
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){

            return new ResponseEntity<>( "Mising data", HttpStatus.FORBIDDEN);
        }
        if(clientRepository.findByEmail(email)!=null){
            return new ResponseEntity<>( "Name already in use", HttpStatus.FORBIDDEN);
        }

        /*clientRepository.save(new Client(firstName, lastName, email,passwordEncoder.encode(password)));*/
        Client client = new Client(firstName, lastName, email,passwordEncoder.encode(password));
        clientRepository.save(client);
        Account account = new Account(accountController.getAccountNumber(), 0.0, LocalDate.now());
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>( HttpStatus.CREATED);
    }



    @RequestMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication){

        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }


}
