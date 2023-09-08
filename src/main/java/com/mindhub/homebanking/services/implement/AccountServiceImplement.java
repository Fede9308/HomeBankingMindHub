package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

        @Autowired
        private AccountRepository accountRepository;

        @Override
        public List<AccountDTO> getAccountsDTO(){
            return accountRepository.findAll()
                                    .stream()
                                    .map(AccountDTO::new)
                                    .collect(toList());
        }
        @Override
        public Account findByNumberAndClientId(String accountNumber, Client clientId){
            return accountRepository.findByNumberAndClientId(accountNumber, clientId);

        }

        @Override
        public void save(Account account){
            accountRepository.save(account);
        }

        @Override
        public boolean existsByNumber(String accountNumber) {
            return accountRepository.existsByNumber(accountNumber);
        }

        @Override
        public Account findByNumber(String accountNumber) {
            return accountRepository.findByNumber(accountNumber);
        }

        @Override
        public boolean existsByNumberAndClientId(String accountNumber, Client clientId){
            return accountRepository.existByNumberAndClientId(accountNumber, clientId);
        }
        @Override
        public Account findById(Long id){
            return accountRepository.findById(id).orElse(null);
        }

}
