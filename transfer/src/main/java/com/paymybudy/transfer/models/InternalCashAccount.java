package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "InternalCashAccount")
@Table(name = "INT_CASH_ACCOUNT", uniqueConstraints = @UniqueConstraint(columnNames = "number"))
@NoArgsConstructor
public class InternalCashAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_cash_account_id")
    @Getter
    @Setter
    private Long id;

    @Column(length = 35)
    @Getter
    @Setter
    private String number;
    @Getter
    @Setter
    private String libelle;

    @Column(length = 35)
    @Getter
    private final String currency = "Euros";

    @Getter
    @Setter
    private double amount;

    @OneToOne(mappedBy = "internalCashAccount")
    private User user;

    @OneToMany(mappedBy = "internalCashAccount")
    @Getter
    private List<MoneyTransferType> transfertTypeList;
    /*
    @OneToMany(targetEntity = InternalTransaction.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "accountDebit")
    @OrderBy("date ASC")
    @Getter
    private List<InternalTransaction> internalTransactionList;*/



    public InternalCashAccount(String number, String libelle) {
        this.number = number;
        this.libelle = libelle;
        this.amount = 0;
        setTransfertTypeList(null);
    }

    public void addCash(double amount){
        this.amount = this.amount + amount;
    }

    public void removeCash(double amount){
        this.amount = this.amount - amount;
    }

    public void setTransfertTypeList(List<MoneyTransferType> internalList){
        this.transfertTypeList = Optional.ofNullable(internalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalCashAccount)) return false;
        InternalCashAccount that = (InternalCashAccount) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "InternalCashAccount{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", libelle='" + libelle + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}



//https://www.baeldung.com/jpa-many-to-many