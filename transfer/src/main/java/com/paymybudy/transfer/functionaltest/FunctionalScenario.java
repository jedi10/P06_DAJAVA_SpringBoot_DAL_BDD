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
    private IInternalTransactionDalService internalTransactionDalService;
    @Autowired
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;

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

            User user2 = user1.getContactList().get(1);
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
    }
}
