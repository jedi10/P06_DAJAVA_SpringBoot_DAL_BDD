package com.paymybudy.transfer.functionaltest;

import com.paymybudy.transfer.dal.service.*;
import com.paymybudy.transfer.models.*;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
    private MoneyTransferService moneyTransferService;
    @Autowired
    private IAppAccountDalService appAccountDalService;
    @Autowired
    private IBankAccountDalService bankAccountDalService;

    public FunctionalScenario() {
        super();
    }

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
}

//https://o7planning.org/fr/11661/tutoriel-spring-boot-jpa-et-spring-transaction