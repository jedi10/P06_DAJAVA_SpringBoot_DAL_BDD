package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "EXTERNAL_TRANSACTION")
@Table(name = "EXTERNAL_TRANSACTION")
@NoArgsConstructor
public class ExternalTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ext_transaction_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private LocalDate date;
    @Getter
    @Setter
    private double amount;
    @Getter
    @Setter
    private EnumTransacStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_fk", referencedColumnName = "bank_account_id")
    @Getter
    @Setter
    private BankAccount accountDebit;
    /*
    @Getter
    @Setter
    private InternalCashAccount accountCredit;*/

    /**
     * <b>ExternalTransaction Constructor</b>
     * @param description description
     * @param date date
     * @param amount amount
     * @param status status
     * @param accountDebit accountDebit
     * @param accountCredit accountCredit
     */
    public ExternalTransaction(String description, LocalDate date, double amount, EnumTransacStatus status, BankAccount accountDebit, InternalCashAccount accountCredit) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.accountDebit = accountDebit;
        //this.accountCredit = accountCredit;
    }
}
