package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {

    private Long id;

    private Long loanId;

    private String name;
    private Double ammount;

    private Integer payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoanId().getId();
        this.name= clientLoan.getLoanId().getName();
        this.ammount = clientLoan.getAmmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public Double getAmmount() {
        return ammount;
    }

    public Integer getPayments() {
        return payments;
    }
}
