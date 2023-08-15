package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static java.util.stream.Collectors.toSet;

public class LoanDTO {

    private Long id;

    private  String name;

    private Double maxAmount;

    private List<Integer> payments = new ArrayList<>();

    private Set<ClientLoanDTO> clientLoans = new HashSet<>();


    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.clientLoans = loan.getClientLoans()
                                .stream()
                                .map(ClientLoanDTO::new)
                                .collect(toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Set<ClientLoanDTO> getClientLoans() {
        return clientLoans;
    }


}
