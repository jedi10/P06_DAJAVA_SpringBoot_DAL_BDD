package com.paymybudy.transfer.functionaltest;

import com.paymybudy.transfer.dal.service.*;
import com.paymybudy.transfer.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class FunctionalScenario {
    @Autowired
    private IUserDalService userDalService;
    @Autowired
    private IExternalTransactionDalService externalTransactionDalService;
    @Autowired
    private IInternalCashAccountDalService internalCashAccountDalService;
    @Autowired
    private MoneyTransferService moneyTransferService;

    public FunctionalScenario() {
        super();
    }

    public void moneyTransfer() throws Exception {
        List<User> userList = (List<User>) userDalService.findAll();
        if (userList != null && userList.size() > 1){
            User user1 = userDalService.findByEmail(userList.get(0).getEmail());
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

            //User 1 want to give money to User 2
            User user2 = user1.getContactList().get(1);

            //TRANSACTIONAL FUNCTIONALITY
            moneyTransferService.sendMoney(user1, user2,
                    "Paiement service livraison",
                    500);
        }
    }
}

//https://o7planning.org/fr/11661/tutoriel-spring-boot-jpa-et-spring-transaction