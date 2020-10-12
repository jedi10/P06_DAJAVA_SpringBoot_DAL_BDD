package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InternalCashAccount {
    @Getter
    @Setter
    private String number;
    @Getter
    @Setter
    private String libelle;

    @Getter
    private final String currency = "Euros";

    @Getter
    @Setter
    private double amount;

    @Getter
    private List<InternalTransaction> internalTransactionList;

    public InternalCashAccount(String number, String libelle) {
        this.number = number;
        this.libelle = libelle;
        this.amount = 0;
        setInternalTransactionList(null);
    }

    public void addCash(double amount){
        this.amount = this.amount + amount;
    }

    public void removeCash(double amount){
        this.amount = this.amount - amount;
    }

    public void setInternalTransactionList(List<InternalTransaction> internalList){
        this.internalTransactionList = Optional.ofNullable(internalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }
}
