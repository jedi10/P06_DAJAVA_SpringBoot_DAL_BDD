package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "INT_CASH_ACCOUNT")
@Table(name = "INT_CASH_ACCOUNT")
@NoArgsConstructor
public class InternalCashAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_cash_account_id")
    @Getter
    @Setter
    private Long id;

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

    @OneToOne(mappedBy = "internalCashAccount")
    private User user;

    /*
    @Getter
    private List<InternalTransaction> internalTransactionList;

     */

    public InternalCashAccount(String number, String libelle) {
        this.number = number;
        this.libelle = libelle;
        this.amount = 0;
        //setInternalTransactionList(null);
    }

    public void addCash(double amount){
        this.amount = this.amount + amount;
    }

    public void removeCash(double amount){
        this.amount = this.amount - amount;
    }
    /*
    public void setInternalTransactionList(List<InternalTransaction> internalList){
        this.internalTransactionList = Optional.ofNullable(internalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }

     */
}
