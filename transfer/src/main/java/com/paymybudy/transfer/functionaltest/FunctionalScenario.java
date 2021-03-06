package com.paymybudy.transfer.functionaltest;

import com.paymybudy.transfer.dal.service.*;
import com.paymybudy.transfer.exception.IntMoneyTransferPreparationException;
import com.paymybudy.transfer.models.*;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <b>Functional Scenarios for demo purpose</b>
 */
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
    private MoneyTransferService moneyTransferService;
    @Autowired
    private IAppAccountDalService appAccountDalService;
    @Autowired
    private IBankAccountDalService bankAccountDalService;
    @Autowired
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;


    public FunctionalScenario() {
        super();
    }

    /**
     * <b>Execute an Internal Money transfer between two users created by external SQL script</b>
     * @throws Exception exception
     */
    public void internalMoneyTransfer() throws Exception {
        List<User> userList = (List<User>) userDalService.findAll();
        if (userList != null && userList.size() > 1){
            User user1 = userDalService.findByEmail(userList.get(0).getEmail());
            //User 1 want to give money to User 2
            User user2 = user1.getContactList().get(1);
            //user2.setInternalCashAccount(null);
            //for this Transaction
            InternalTransaction internalTransaction = new InternalTransaction("Paiement service livraison",
                    500);
            //TRANSACTIONAL FUNCTIONALITY
            moneyTransferService.sendMoney(user1, user2, internalTransaction);
        }
    }

    /**
     * <b>Execute an External Money transfer for one users created by external SQL script</b>
     */
    public void addExternalCashToInternalAccount(){
        List<User> userList = (List<User>) userDalService.findAll();
        if (userList != null && userList.size() > 1) {
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
        }
    }

    /**
     * <b>All Registration Process for one User on java side</b>
     * <ul>
     *     <li>User creation with password encode</li>
     *     <li>AppAccount creation</li>
     *     <li>BankAccount And InternalCashAccount creation</li>
     *     <li>Credit Internal Cash Account from outside Bank (external transaction)</li>
     *     <li>Add One friend in contact list</li>
     * </ul>
     * @throws Exception exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public void addUserFullInscription() throws Exception {
        //*************
        //User Creation
        //*************
        UserRegistrationDto userRegistrationDtoToCreate = new UserRegistrationDto(
                "Joli",
                "Jumper",
                "jolijumper@farwest.us",
                "luckyluke");

        User userCreated = userDalService.create2(userRegistrationDtoToCreate);

        //Check User Creation with password
        UserDetails userDetails = userDalService.loadUserByUsername(userCreated.getEmail());
        System.out.println(">>>>>>> Generated Password: '" + userDetails.getPassword()+"' <<<<<<<");

        //*******************
        //appAccount Creation
        //*******************
        AppAccount appAccount1 = new AppAccount(userCreated, EnumAppAccountStatus.NOTCONFIRMED);

        AppAccount appAccountResult1 = appAccountDalService.create(appAccount1);
        //check AppAccount Status
        System.out.println(">>>>>>> App Account id: '" + appAccount1.getId() +"' <<<<<<<");

        //******************************************************
        //BankAccount Creation && Internal Cash Account Creation
        //******************************************************
        BankAccount bankAccount1 = new BankAccount("Texaco Bank","0034445777", "72 Mulland Drive New York");
        BankAccount bankAccountResult1 = bankAccountDalService.create(bankAccount1);
        userCreated.setBankAccount(bankAccountResult1);

        InternalCashAccount internalCashAccount1 = new InternalCashAccount("XX23453-456", "Compte de dépot Joli Jumper" );
        InternalCashAccount intCashAccountResult1 = internalCashAccountDalService.create(internalCashAccount1);
        userCreated.setInternalCashAccount(intCashAccountResult1);

        User userUpdated = userDalService.update(userCreated);

        //**************************
        //Add Cash From outside Bank
        //**************************
        ExternalTransaction externalTransaction = new ExternalTransaction(
                "Approvisionnement",
                1000,
                EnumTransacStatus.FINISHED,
                userUpdated.getBankAccount(),
                userUpdated.getInternalCashAccount());
        //Save transaction in DBB
        externalTransactionDalService.create(externalTransaction);
        internalCashAccountDalService.update(externalTransaction.getAccountCredit());

        //**************
        //Add one Friend
        //**************
        List<User> userList = (List<User>) userDalService.findAll();

        User friend = userDalService.findByEmail(userList.get(0).getEmail());
        userUpdated.addOneContact(friend);
        userDalService.update(userUpdated);
    }

    /**
     * <b>Execute an Internal Money transfer with one user created on java side</b>
     * @throws Exception exception
     */
    public void newUserGiveCashToAnother() throws Exception {
            User user1 = userDalService.findByEmail("jolijumper@farwest.us");
            //User 1 want to give money to User 2
            User user2 = user1.getContactList().get(0);
            //user2.setInternalCashAccount(null);
            //for this Transaction
            InternalTransaction internalTransaction = new InternalTransaction("Livraison Fourrage ",
                    700);
            //TRANSACTIONAL FUNCTIONALITY
            moneyTransferService.sendMoney(user1, user2, internalTransaction);
    }

    /**
     * <b>Delete User in all tables</b>
     * <p>internal Transaction will be saved</p>
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    public void deleteNewUser(){
        User user1 = userDalService.findByEmail("jolijumper@farwest.us");
        List<AppAccount> appAccountList =  appAccountDalService.findAll();
        AppAccount appAccount1 =  appAccountList.stream().filter(
                e->  e.getUser().getEmail().equals(user1.getEmail()))
                .findFirst().get();
                //.limit(1);
                //.reduce((a, b) -> {
                //    throw new IllegalStateException("Multiple elements: " + a + ", " + b);
                //});

        appAccountDalService.delete(appAccount1.getId());

        //Delete Cash Account
        //internal Transaction will be saved
        InternalCashAccount internalCashAccount1 = user1.getInternalCashAccount();
        List<MoneyTransferType> moneyTransferTypeList = moneyTransferTypeDalService.findAll();
        List<MoneyTransferType> moneyTransferTypeListFiltered = moneyTransferTypeList.stream()
                .filter(e-> internalCashAccount1.getId().equals(e.getId().getInternalCashAccountId()))
                .collect(Collectors.toList());

        moneyTransferTypeListFiltered.forEach(e->
                {   MoneyTransferTypeKey moneyTransferTypeKey = new MoneyTransferTypeKey(
                        e.getId().getInternalCashAccountId(),
                        e.getId().getInternalTransactionId());
                    moneyTransferTypeDalService.delete(moneyTransferTypeKey);}
        );
        internalCashAccountDalService.delete(internalCashAccount1.getId());

        System.out.println("c'est fini");
    }

}

//https://o7planning.org/fr/11661/tutoriel-spring-boot-jpa-et-spring-transaction