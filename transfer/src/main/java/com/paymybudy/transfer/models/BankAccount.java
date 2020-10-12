package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Getter
    private List<ExternalTransaction> externalTransactionList;

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
        setExternalTransactionList(null);
    }

    public void setExternalTransactionList(List<ExternalTransaction> externalList){
        this.externalTransactionList = Optional.ofNullable(externalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }
}
