package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

public class BankAccount {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String telNumber;
    @Getter
    @Setter
    private String address;

    /**
     * <b>BankAccount constructor</b>
     * @param name bank name
     * @param telNumber telephone
     * @param address address
     */
    public BankAccount(String name, String telNumber, String address) {
        this.name = name;
        this.telNumber = telNumber;
        this.address = address;
    }
}
