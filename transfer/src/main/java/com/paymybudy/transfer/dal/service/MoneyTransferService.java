package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.exception.IntMoneyTransferExecutionException;
import com.paymybudy.transfer.exception.IntMoneyTransferPreparationException;
import com.paymybudy.transfer.models.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Service controls Money transfer between two internal accounts</b>
 * <p>this service uses transactional SQL</p>
 */
@Service
public class MoneyTransferService {


    private IInternalCashAccountDalService internalCashAccountDalService;
    private IInternalTransactionDalService internalTransactionDalService;
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;

    InternalTransaction internalTransaction;

    public MoneyTransferService(IInternalCashAccountDalService internalCashAccountDalService,
                                IInternalTransactionDalService internalTransactionDalService,
                                IMoneyTransferTypeDalService moneyTransferTypeDalService) {
        this.internalCashAccountDalService = internalCashAccountDalService;
        this.internalTransactionDalService = internalTransactionDalService;
        this.moneyTransferTypeDalService = moneyTransferTypeDalService;
    }


    /**
     * <b>Send Money from one to another user</b>
     * <p>Different Steps</p>
     * <ul>
     *    <li>transaction creation</li>
     *    <li>check internal accounts existence</li>
     *    <li>build MoneyTransferType for credit and debit (transactional)</li>
     *    <li>add and remove credit (transactional)</li>
     *    <li>update transaction status</li>
     * </ul>
     * @param fromUser user wants to give money
     * @param toUser user will received money
     * @param internalTransaction transaction relative
     * @throws Exception exception
     */
    public void sendMoney(User fromUser, User toUser, InternalTransaction internalTransaction) throws Exception {
        //Transaction
        this.internalTransaction = internalTransaction;
        //save in DBB
        InternalTransaction internalTransactionCreated = internalTransactionDalService.create(internalTransaction);
        if (internalTransactionCreated == null) {
            String messageTransactionAborted = String.format(
                    "internalTransaction can not be created: %s",
                    internalTransaction.toString()
            );
            updateTransactionStatus(messageTransactionAborted, EnumTransacStatus.ABORTED);
            throw new IntMoneyTransferPreparationException(messageTransactionAborted);
        }
        try {
            //MoneyTransfer Preparation
            sendMoneyPreparation(fromUser, toUser);
            //Money Transfer Execution
            sendMoneyExecution(fromUser, toUser);
        } catch (Exception e) {
            updateTransactionStatus(e.getMessage(), EnumTransacStatus.ABORTED);
            throw e;
        }
    }

    /**
     * <b>Check transfer Money mandatory component </b>
     * <ul>
     *     <li>check internal accounts existence,</li>
     *     <li>build MoneyTransferType for credit and debit (transactional)</li>
     * </ul>
     * @param fromUser user wants to give money
     * @param toUser user will received money
     * @throws Exception specific exception for preparation of the money transfer
     */
    // Do not catch Exception in this method
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {IntMoneyTransferPreparationException.class, Exception.class})
    void sendMoneyPreparation(User fromUser, User toUser) throws Exception {
        //for debit user
        MoneyTransferType moneyTransferType = preparationMoneyTransferType(fromUser, false);
        //for credit user
        MoneyTransferType moneyTransferType2 = preparationMoneyTransferType(toUser, true);

        //MoneyTransferType DBB Creation
        MoneyTransferType moneyTransferTypeCreated = moneyTransferTypeDalService.create(moneyTransferType);
        checkPreparationMoneyTransferTypeDBBCreation(moneyTransferTypeCreated, moneyTransferType);

        MoneyTransferType moneyTransferTypeCreated2 = moneyTransferTypeDalService.create(moneyTransferType2);
        checkPreparationMoneyTransferTypeDBBCreation(moneyTransferTypeCreated2, moneyTransferType2);
    }

    @Transactional(propagation = Propagation.MANDATORY )
    void checkPreparationMoneyTransferTypeDBBCreation(MoneyTransferType moneyTransferTypeCreated,
            MoneyTransferType moneyTransferType) throws IntMoneyTransferPreparationException {
        if (moneyTransferTypeCreated == null){
            String messageTransactionAborted = String.format(
                    "Money Transfer Type can not be created:  %s",
                    moneyTransferType.toString()
            );
            throw new IntMoneyTransferPreparationException(messageTransactionAborted);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY )
    MoneyTransferType preparationMoneyTransferType(
            User user, boolean isCredit) throws IntMoneyTransferPreparationException {
        //check if user have an Internal Account
        if (user.getInternalCashAccount() == null) {
            String messageTransactionAborted = String.format(
                    "Internal Cash Account not found for user id: %s",
                    user.getId());
            throw new IntMoneyTransferPreparationException(messageTransactionAborted);
        }
        //Attach transaction with internal Account
        MoneyTransferTypeKey keyDebitOperation = new MoneyTransferTypeKey(user.getInternalCashAccount().getId(), internalTransaction.getId());
        return new MoneyTransferType(keyDebitOperation, user.getInternalCashAccount(), internalTransaction, isCredit);
    }

    /**
     * <b>Money transfer execution, main method</b>
     * <ul>
     *     <li>add and remove credit (transactional)</li>
     *     <li>update transaction status if transfer OK</li>
     * </ul>
     * @param fromUser user wants to give money
     * @param toUser user will received money
     * @throws Exception specific exception for money transfer execution
     */
    // Do not catch Exception in this method
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {IntMoneyTransferExecutionException.class, Exception.class})
    void sendMoneyExecution(User fromUser, User toUser) throws Exception {
        //Money Transfer
        addAmount(fromUser, false);
        addAmount(toUser,true);

        //Save Money Transfer in DBB
        InternalCashAccount internalCashAccountUpdated = internalCashAccountDalService.update(fromUser.getInternalCashAccount());
        checkExecutionCashAccountUpdate(internalCashAccountUpdated, fromUser);

        InternalCashAccount internalCashAccountUpdated2 = internalCashAccountDalService.update(toUser.getInternalCashAccount());
        checkExecutionCashAccountUpdate(internalCashAccountUpdated2, toUser);

        updateTransactionStatus(
                "Transaction has been executed with success",
                EnumTransacStatus.FINISHED);
    }

    @Transactional(propagation = Propagation.MANDATORY )
    void checkExecutionCashAccountUpdate(InternalCashAccount internalCashAccountUpdated,
                                         User user) throws IntMoneyTransferExecutionException {
        if (internalCashAccountUpdated == null){
            String messageTransactionAborted = String.format(
                    "Amount can't be updated for cash account id: %s",
                    user.getInternalCashAccount().getId()
            );
            throw new IntMoneyTransferExecutionException(messageTransactionAborted);
        }
    }

    /**
     * <b>Money transfer execution: change cash account</b>
     * @param user user concerned by the change of tha balance of its account
     * @param isCredit is a credit or a debit: true for credit
     * @throws IntMoneyTransferExecutionException specific exception for money transfer execution
     */
    // MANDATORY: Transaction must be created before.
    @Transactional(propagation = Propagation.MANDATORY )
    void addAmount(User user, boolean isCredit) throws IntMoneyTransferExecutionException {
        //Execute Money Transfer
        double newBalance = 0;
        if (isCredit){
            newBalance = user.getInternalCashAccount().getAmount() + internalTransaction.getAmount();
        } else {
            newBalance = user.getInternalCashAccount().getAmount() - internalTransaction.getAmount();
            if (newBalance < 0 ){
                String messageTransactionAborted = String.format(
                        "The balance of the account '%s' is not enough (%s)",
                        user.getInternalCashAccount().getId(),
                        user.getInternalCashAccount().getAmount()
                );
                throw new IntMoneyTransferExecutionException(messageTransactionAborted);
            }
        }
        user.getInternalCashAccount().setAmount(newBalance);
    }

    private void updateTransactionStatus(String message, EnumTransacStatus status) throws IntMoneyTransferExecutionException {
        internalTransaction.setStatus(status);
        internalTransaction.setTransactionMessage(message);
        InternalTransaction internalTransactionUpdated = internalTransactionDalService.update(internalTransaction);
        if (internalTransactionUpdated == null) {
            String messageTransactionAborted = String.format(
                    "Transaction with id '%s' can't be updated",
                    internalTransaction.getId()
            );
            throw new IntMoneyTransferExecutionException(messageTransactionAborted);
        }
    }
}

//https://www.marcobehler.com/guides/spring-transaction-management-transactional-in-depth
//https://www.baeldung.com/java-transactions
//https://www.baeldung.com/java-synchronized
//https://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
//https://mkyong.com/java/how-to-loop-a-map-in-java/

//https://www.baeldung.com/spring-transactional-propagation-isolation
//https://o7planning.org/fr/11661/tutoriel-spring-boot-jpa-et-spring-transaction