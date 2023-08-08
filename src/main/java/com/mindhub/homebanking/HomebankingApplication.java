package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args) -> {

			Client melba = new Client("26984756","Melba","Morel","melba@mindhub.com" );
			Client federico = new Client("37638649","Federico","Becerra","fedefbecerra@gmail.com");
			Account vinn001 = new Account("VIN001", 5000.00, LocalDate.now(), melba);
			Account vinn002 = new Account("VIN002", 7500.00, LocalDate.now().plusDays(1), melba);

			clientRepository.save(melba);
			clientRepository.save(federico);
			accountRepository.save(vinn001);
			accountRepository.save(vinn002);
		};
	}
}
