package com.paymybudy.transfer;

import com.paymybudy.transfer.dal.service.*;
import com.paymybudy.transfer.models.*;
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
	@Autowired
	private IInternalTransactionDalService internalTransactionDalService;
	@Autowired
	private IMoneyTransferTypeDalService moneyTransferTypeDalService;

	public static void main(String[] args) {
		SpringApplication.run(TransferApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        //Thread.sleep(1000);  --------------------------------------------------> line 1
		log.info("All users -> {}", userDalService.findAll());
		Collection<User> userList = userDalService.findAll();
		if (userList != null && userList.size() > 1){
			List<User> userList2 = (List<User>)userList;
			User user1 = userDalService.findByEmail(userList2.get(0).getEmail());
			//USER 1 want to credit his internal Account
			ExternalTransaction externalTransaction = new ExternalTransaction(
					"Approvisionnement",
					1000,
					EnumTransacStatus.FINISHED,
					user1.getBankAccount(),
					user1.getInternalCashAccount());
			//Save transaction in DBB
			externalTransactionDalService.create(externalTransaction);
			internalCashAccountDalService.update(externalTransaction.getAccountCredit());

			User user2 = userDalService.findByEmail(userList2.get(1).getEmail());
			//User 1 want to give money to User 2
			InternalTransaction internalTransaction = new InternalTransaction("Paiement service livraison", 500);
			//Operation creation in DBB
			internalTransactionDalService.create(internalTransaction);
			//Attach this transaction with 2 internal Account (one for credit, one for debit)
			MoneyTransferTypeKey keyDebitOperation = new MoneyTransferTypeKey(user1.getInternalCashAccount().getId(), internalTransaction.getId());
			MoneyTransferType debitOperation = new MoneyTransferType(keyDebitOperation, user1.getInternalCashAccount(), internalTransaction, false);
			moneyTransferTypeDalService.create(debitOperation);
			MoneyTransferTypeKey keyCreditOperation = new MoneyTransferTypeKey(user2.getInternalCashAccount().getId(), internalTransaction.getId());
			MoneyTransferType creditOperation = new MoneyTransferType(keyCreditOperation, user2.getInternalCashAccount(), internalTransaction, true);
			moneyTransferTypeDalService.create(creditOperation);

			//Execute Money Transfer
			internalTransaction.executeTransaction(user1.getInternalCashAccount(), user2.getInternalCashAccount());
			//Save Money Transfer in DBB
			internalCashAccountDalService.update(user1.getInternalCashAccount());
			internalCashAccountDalService.update(user2.getInternalCashAccount());
			internalTransactionDalService.update(internalTransaction);
		}

		//https://stackoverflow.com/questions/62400654/is-data-sql-disabled-in-spring-boot-2-3-1-release
	}

	//https://www.tutorialspoint.com/jpa/jpa_entity_relationships.htm
	//https://programmerfriend.com/spring-boot-integration-testing-done-right/

}
