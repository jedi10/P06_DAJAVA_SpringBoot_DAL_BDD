package com.paymybudy.transfer;

import com.paymybudy.transfer.dal.service.IUserDalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class TransferApplication implements CommandLineRunner {

	@Autowired
	private IUserDalService userDalService;

	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        //Thread.sleep(1000);  --------------------------------------------------> line 1
		log.info("All users -> {}", userDalService.findAll());
		//https://stackoverflow.com/questions/62400654/is-data-sql-disabled-in-spring-boot-2-3-1-release
	}

	//https://www.tutorialspoint.com/jpa/jpa_entity_relationships.htm

}
