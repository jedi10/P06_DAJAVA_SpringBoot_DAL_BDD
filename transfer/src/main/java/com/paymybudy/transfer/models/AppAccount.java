package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

public class AppAccount {
    @Getter
    @Setter
    private User user;

    @Getter
    private EnumAppAccountStatus appAccountStatus;

    @Getter
    @Setter
    private LocalDate creationDate;

    @Getter
    @Setter
    private LocalDate lastConnection;

    /**
     * <b>AppLoginAccount Constructor</b>
     * @param user user
     * @param loginAccountStatus loginAccountStatus (Enum)
     */
    public AppAccount(User user,
                      EnumAppAccountStatus loginAccountStatus) throws Exception {
        this.user = user;
        setAppAccountStatus(loginAccountStatus);
        this.creationDate = LocalDate.now();
    }

    public void setAppAccountStatus(EnumAppAccountStatus appAccountStatus) throws Exception {
         if (EnumAppAccountStatus.CONFIRMED.equals(appAccountStatus)){
             if (userAccountsCreated()){
                 this.appAccountStatus = appAccountStatus;
             } else {
                 throw new Exception(
                         "User Bank Account and User Internal Account" +
                                 " have to be created before AppAccount can be Confirmed");
             }
         } else {
             this.appAccountStatus = appAccountStatus;
         }
    }

    private boolean userAccountsCreated(){
        return user.getBankAccount() != null &&
                user.getInternalCashAccount() != null;
    }
}
