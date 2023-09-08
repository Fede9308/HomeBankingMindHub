package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        List<Loan> allLoans = loanRepository.findAll();

        List<LoanDTO> loanDTOS = allLoans.stream().map(LoanDTO::new).collect(toList());

        return loanDTOS;
    }

    @GetMapping("loans/{id}")
    public LoanDTO getLoanById(@PathVariable Long id){

        Loan loan = loanRepository.findById(id).orElse(null);
        return  new LoanDTO(loan);
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanMaker(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                                            Authentication authentication){
            if (loanApplicationDTO.getLoanId() == 0) {
                return new ResponseEntity<>("Préstamo no seleccionado", HttpStatus.FORBIDDEN);
            }

        if (loanApplicationDTO.getPayments() == 0) {
            return new ResponseEntity<>("Se debe seleccionar la cantidad de cuotas", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getToAccountNumber().isBlank()) {
            return new ResponseEntity<>("Cuenta de Destino no ingresada", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() == 0) {
            return new ResponseEntity<>("Monto soliciado no ingresado", HttpStatus.FORBIDDEN);
        }
        //403 forbidden, si la cuenta de destino no existe
       Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        if (account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        //403 forbidden, si la cuenta de destino no
        //pertenece al cliente autenticado

        Client client = clientService.findByEmail(authentication.getName());
        if (!accountService.existsByNumberAndClientId(account.getNumber(), client)) {
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente autenticado",
                    HttpStatus.FORBIDDEN);
        }

        // 403 forbidden, si el préstamo no existe
        if(!loanService.existsById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("El prestamo seleccionado no existe", HttpStatus.FORBIDDEN);
        }

    }



}
