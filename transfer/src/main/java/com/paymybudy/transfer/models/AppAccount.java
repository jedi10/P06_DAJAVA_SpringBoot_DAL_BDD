package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

public class AppAccount {
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private EnumAppAccountStatus loginAccountStatus;

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
                      EnumAppAccountStatus loginAccountStatus) {
        this.user = user;
        this.loginAccountStatus = loginAccountStatus;
        this.creationDate = LocalDate.from(Instant.now());
    }
}
