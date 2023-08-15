package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return (args) -> {


			Client melba = new Client("26984756","Melba","Morel","melba@mindhub.com" );
			Client federico = new Client("37638649","Federico","Becerra","fedefbecerra@gmail.com");

			LocalDate fechaActual = LocalDate.now();
			Account vinn001 = new Account("VIN001", 5000.00, fechaActual);
			Account vinn002 = new Account("VIN002", 7500.00, fechaActual.plusDays(1));
			Account vinn003 = new Account("VIN003", 6536.00, fechaActual);

			Transaction tr001= new Transaction(TransactionType.CREDIT,
											4380.00, "Transacción de MindHub", LocalDate.now());
			Transaction tr002= new Transaction(TransactionType.DEBIT,
					-1380.00, "Pago subscripción a GymPlus", fechaActual);
			Transaction tr003= new Transaction(TransactionType.CREDIT,
					6290.00, "Transacción de Julia", fechaActual);

			Transaction tr004= new Transaction(TransactionType.DEBIT,
					-2640.00, "Pago Alquiler",fechaActual);

			Loan hipotecario = new Loan("Hipotecario", 500000.00, List.of(12, 24 , 36, 48, 60) );
			Loan personal = new Loan("Personal", 100000.00,List.of(6, 12, 24));
			Loan automotriz = new Loan("Automotriz", 300000.00, List.of(6, 12, 24, 36));


			ClientLoan melbaLoanH = new ClientLoan(400000.00, 60);
			ClientLoan melbaLoanP = new ClientLoan(50000.0,12);

			ClientLoan federicoLoanP = new ClientLoan(100000.0, 20);
			ClientLoan federicoLoanA = new ClientLoan(200000.0, 36);

			clientRepository.save(melba);
			clientRepository.save(federico);

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);

			melba.addLoans(melbaLoanH);
			hipotecario.addClientLoans(melbaLoanH);
			melba.addLoans(melbaLoanP);
			personal.addClientLoans(melbaLoanP);

			federico.addLoans(federicoLoanP);
			personal.addClientLoans(federicoLoanP);
			federico.addLoans(federicoLoanA);
			automotriz.addClientLoans(federicoLoanA);


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


			clientLoanRepository.save(melbaLoanH);
			clientLoanRepository.save(melbaLoanP);
			clientLoanRepository.save(federicoLoanA);
			clientLoanRepository.save(federicoLoanP);




		};
	}
}
