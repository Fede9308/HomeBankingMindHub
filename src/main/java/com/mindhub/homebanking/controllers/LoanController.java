package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        /*List<Loan> allLoans = loanRepository.findAll();

        List<LoanDTO> loanDTOS = allLoans.stream().map(LoanDTO::new).collect(toList());
*/
        return loanService.getLoansDTO();
    }

    @GetMapping("loans/{id}")
    public LoanDTO getLoanById(@PathVariable Long id){

        Loan loan = loanService.findById(id);
        return  new LoanDTO(loan);
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @Autowired
    private ClientLoanService clientLoanService;


    @Autowired
    private TransactionService transactionService;

//    Debe recibir un objeto de solicitud de crédito con los datos del préstamo
//    Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0.
//    Verificar que el préstamo exista
//    Verificar que el monto solicitado no exceda el monto máximo del préstamo
//    Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
//    Verificar que la cuenta de destino exista
//    Verificar que la cuenta de destino pertenezca al cliente autenticado
//    Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
//    Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
//    Se debe actualizar la cuenta de destino sumando el monto solicitado.

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
       Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        if (account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (!accountService.existsByNumberAndClientId(account.getNumber(), client)) {
            return new ResponseEntity<>("La cuenta de destino debe pertenecer al cliente autenticado",
                    HttpStatus.FORBIDDEN);
        }

        if(!loanService.existsById(loanApplicationDTO.getLoanId())) {
            return new ResponseEntity<>("El prestamo ingresado no existe", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("El monto solocitado es mayor al monto disponible para el prestamo de " +
                    "tipo " + loan.getName(), HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("La cantidad de cuotas no esta diponible para el prestamo de tipo " +
                    loan.getName(), HttpStatus.FORBIDDEN);
        }



        ClientLoan makeLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());


        client.addLoans(makeLoan);
        loan.addClientLoans(makeLoan);
        clientLoanService.save(makeLoan);


        Transaction loanTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),
                loan.getName()
                        + " loan approved", LocalDateTime.now());

        account.addTransaction(loanTransaction);
        transactionService.save(loanTransaction);



        double accountBalance = account.getBalance();
        account.setBalance(accountBalance + loanApplicationDTO.getAmount());
        accountService.save(account);



        return new ResponseEntity<>("Todo ok", HttpStatus.ACCEPTED);



    }



}
