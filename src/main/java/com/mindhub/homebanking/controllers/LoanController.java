package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
