package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository){
		return (args) -> {

			Client melba = new Client("26984756","Melba","Morel","melba@mindhub.com" );
			Client federico = new Client("37638649","Federico","Becerra","fedefbecerra@gmail.com");
			Account vinn001 = new Account("VIN001", 5000.00, LocalDate.now());
			Account vinn002 = new Account("VIN002", 7500.00, LocalDate.now().plusDays(1));
			Account vinn003 = new Account("VIN003", 6536.00, LocalDate.now());
			Transaction tr001= new Transaction(TransactionType.CREDITO,
											4380.00, "Transacción de MindHub", LocalDate.now());
			Transaction tr002= new Transaction(TransactionType.DEBITO,
					-1380.00, "Pago subscripción a GymPlus", LocalDate.now());
			Transaction tr003= new Transaction(TransactionType.CREDITO,
					6290.00, "Transacción de Julia", LocalDate.now());

			Transaction tr004= new Transaction(TransactionType.DEBITO,
					-2640.00, "Pago Alquiler", LocalDate.now());
			clientRepository.save(melba);
			clientRepository.save(federico);

			melba.addAccount(vinn001);
			melba.addAccount(vinn002);
			federico.addAccount(vinn003);

			accountRepository.save(vinn001);
			accountRepository.save(vinn002);
			accountRepository.save(vinn003);

			vinn001.addTransaction(tr001);
			vinn001.addTransaction(tr002);
			vinn002.addTransaction(tr003);
			vinn003.addTransaction(tr004);

			transactionRepository.save(tr001);
			transactionRepository.save(tr002);
			transactionRepository.save(tr003);
			transactionRepository.save(tr004);
		};
	}
}
