package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
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
            return new ResponseEntity<>("El monto soliciado no fue ingresado", HttpStatus.FORBIDDEN);
        }
        //403 forbidden, si la cuenta de destino no existe
       // Account account = .findByNumber(loanApplicationDTO.getToAccountNumber());

       //
    }



}
