package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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

    public InternalCashAccount(String number, String libelle) {
        this.number = number;
        this.libelle = libelle;
        this.amount = 0;
    }

    public void addCash(double amount){
        this.amount = this.amount + amount;
    }

    public void removeCash(double amount){
        this.amount = this.amount - amount;
    }
}
