package com.project.bank1;

import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.AcquirerRepository;
import com.project.bank1.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

import java.util.HashSet;

@SpringBootApplication
@EnableEurekaClient
public class BankApplication implements CommandLineRunner {
	@Autowired
	private Environment environment;
	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private AcquirerRepository acquirerRepository;

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		insert();
	}

	private void insert() {
		Bank b1 = new Bank();
		b1.setId(1L);
		b1.setName("Bank 1");
		b1.setBankUrl(environment.getProperty("bank1-application.backend"));
		b1.setAcquirers(new HashSet<>());
		bankRepository.save(b1);

		Bank b2 = new Bank();
		b2.setId(2L);
		b2.setName("Bank 2");
		b2.setBankUrl(environment.getProperty("bank2-application.backend"));
		b2.setAcquirers(new HashSet<>());
		bankRepository.save(b2);

		Acquirer acquirer=new Acquirer();
		acquirer.setBank(b1);
		acquirer.setQrCodePayment(true);
		acquirer.setShopId("123456789");
		acquirer.setMerchantId("not set");
		acquirer.setMerchantPassword("not set");
		acquirer.setTransactions(new HashSet<>());
		acquirer.setBankPayment(true);
		acquirerRepository.save(acquirer);
	}

}
