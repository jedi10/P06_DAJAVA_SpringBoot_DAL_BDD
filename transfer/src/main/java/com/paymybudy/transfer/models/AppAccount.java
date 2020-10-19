package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "APP_ACCOUNT")
@Table(name = "APP_ACCOUNT")
@NoArgsConstructor
public class AppAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_account_id")
    @Getter
    @Setter
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_fk", referencedColumnName = "user_id")
    @Getter
    @Setter
    private User user;

    @Getter
    private EnumAppAccountStatus appAccountStatus;

    @Column(name = "status_date", columnDefinition = "DATE")
    @Getter
    @Setter
    private LocalDate statusDate;

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
        this.statusDate = LocalDate.now();
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


//https://www.baeldung.com/jpa-java-time