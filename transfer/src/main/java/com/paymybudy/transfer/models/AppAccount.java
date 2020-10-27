package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "AppAccount")
@Table(name = "APP_ACCOUNT")
@NoArgsConstructor
public class AppAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_account_id")
    @Getter
    @Setter
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_fk", referencedColumnName = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(name = "app_account_status", length = 15)
    @Getter
    private EnumAppAccountStatus appAccountStatus;

    @Column(name = "status_date", columnDefinition = "DATE")
    @Getter
    @Setter
    private LocalDate statusDate;

    @Column(name = "last_connection", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime lastConnection;

    /**
     * <b>AppLoginAccount Constructor</b>
     * @param user user
     * @param loginAccountStatus loginAccountStatus (Enum)
     * @throws Exception custom Exception
     * @see #setAppAccountStatus(EnumAppAccountStatus)
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