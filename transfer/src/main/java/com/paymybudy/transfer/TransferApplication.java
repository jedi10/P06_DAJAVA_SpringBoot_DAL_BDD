package com.paymybudy.transfer;

import com.paymybudy.transfer.dal.service.IAppAccountDalService;
import com.paymybudy.transfer.dal.service.IExternalTransactionDalService;
import com.paymybudy.transfer.dal.service.IInternalCashAccountDalService;
import com.paymybudy.transfer.dal.service.IUserDalService;
import com.paymybudy.transfer.models.EnumTransacStatus;
import com.paymybudy.transfer.models.ExternalTransaction;
import com.paymybudy.transfer.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Collection;
import java.util.List;

/**
 * <b>Main Class of the Application (Root)</b>
 */
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class TransferApplication implements CommandLineRunner {

	@Autowired
	private IUserDalService userDalService;

	@Autowired
	private IExternalTransactionDalService externalTransactionDalService;
	@Autowired
	private IInternalCashAccountDalService internalCashAccountDalService;

	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        //Thread.sleep(1000);  --------------------------------------------------> line 1
		log.info("All users -> {}", userDalService.findAll());
		Collection<User> userList = userDalService.findAll();
		if (userList != null && userList.size() > 0){
			List<User> userList2 = (List<User>)userList;
			User user = userDalService.findByEmail(userList2.get(0).getEmail());

			ExternalTransaction externalTransaction = new ExternalTransaction(
					"Approvisionnement",
					1000,
					EnumTransacStatus.FINISHED,
					user.getBankAccount(),
					user.getInternalCashAccount());
			//externalTransaction.executeExternalTransfer();
			externalTransactionDalService.create(externalTransaction);
			internalCashAccountDalService.update(externalTransaction.getAccountCredit());
		}

		//https://stackoverflow.com/questions/62400654/is-data-sql-disabled-in-spring-boot-2-3-1-release
	}

	//https://www.tutorialspoint.com/jpa/jpa_entity_relationships.htm

}
