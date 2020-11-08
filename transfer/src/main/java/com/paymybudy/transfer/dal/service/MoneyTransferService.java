package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.exception.InternalMoneyTransferException;
import com.paymybudy.transfer.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoneyTransferService {

    @Autowired
    private IInternalCashAccountDalService internalCashAccountDalService;
    @Autowired
    private IInternalTransactionDalService internalTransactionDalService;
    @Autowired
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;

    private InternalTransaction internalTransaction;


    // Do not catch Exception in this method
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = InternalMoneyTransferException.class)
    public void sendMoney(User fromUser, User toUser, String transactionDescription, double transactionAmount) throws InternalMoneyTransferException {
        //Transaction creation
        internalTransaction = new InternalTransaction(transactionDescription, transactionAmount);
        //save in DBB
        InternalTransaction internalTransactionCreated = internalTransactionDalService.create(internalTransaction);
        if (internalTransactionCreated == null) {
            String messageTransactionAborted = String.format(
                    "internalTransaction can not be created: %s",
                    internalTransaction.toString()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }
        //Money Transfer
        addAmount(fromUser, false);
        addAmount(toUser,true);
        updateTransactionStatus(
                "Transaction has been executed with success",
                EnumTransacStatus.FINISHED);
    }

    // MANDATORY: Transaction must be created before.
    @Transactional(propagation = Propagation.MANDATORY )
    public void addAmount(User user, boolean isCredit) throws InternalMoneyTransferException {

        //check if user have an Internal Account
        if (user.getInternalCashAccount() == null){
            String messageTransactionAborted = String.format(
                    "Account not found for user id: %s",
                    user.getId()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }
        //Attach transaction with internal Account
        MoneyTransferTypeKey keyDebitOperation = new MoneyTransferTypeKey(user.getInternalCashAccount().getId(), internalTransaction.getId());
        MoneyTransferType moneyTransferType = new MoneyTransferType(keyDebitOperation, user.getInternalCashAccount(), internalTransaction, isCredit);
        MoneyTransferType moneyTransferTypeCreated = moneyTransferTypeDalService.create(moneyTransferType);
        if (moneyTransferTypeCreated == null){
            String messageTransactionAborted = String.format(
                    "Money Transfer Type can not be created:  %s",
                    moneyTransferType.toString()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }

        //Execute Money Transfer
        double newBalance = 0;
        if (isCredit){
            newBalance = user.getInternalCashAccount().getAmount() + internalTransaction.getAmount();
        } else {
            newBalance = user.getInternalCashAccount().getAmount() - internalTransaction.getAmount();
        }

        if (newBalance < 0 ){
            String messageTransactionAborted = String.format(
                    "The money in the account '%s' is not enough (%s)",
                    user.getInternalCashAccount().getId(),
                    user.getInternalCashAccount().getAmount()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }

        user.getInternalCashAccount().setAmount(newBalance);
        //Save Money Transfer in DBB
        InternalCashAccount internalCashAccountUpdated = internalCashAccountDalService.update(user.getInternalCashAccount());
        if (internalCashAccountUpdated == null){
            String messageTransactionAborted = String.format(
                    "Amount can't be updated for cash account id: %s",
                    user.getInternalCashAccount().getId()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }
    }

    private void updateTransactionStatus(String message, EnumTransacStatus status) throws InternalMoneyTransferException {
        internalTransaction.setStatus(status);
        internalTransaction.setTransactionMessage(message);
        InternalTransaction internalTransactionUpdated = internalTransactionDalService.update(internalTransaction);
        if (internalTransactionUpdated == null) {
            String messageTransactionAborted = String.format(
                    "Transaction with id '%s' Status can't be updated",
                    internalTransaction.getId()
            );
            throw new InternalMoneyTransferException(messageTransactionAborted);
        }
    }
}


//https://www.baeldung.com/spring-transactional-propagation-isolation
//https://o7planning.org/fr/11661/tutoriel-spring-boot-jpa-et-spring-transaction