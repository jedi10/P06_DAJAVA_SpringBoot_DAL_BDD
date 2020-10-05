package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ExternalTransaction {
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
    @Getter
    @Setter
    private BankAccount accountDebit;
    @Getter
    @Setter
    private InternalCashAccount accountCredit;

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
        this.accountCredit = accountCredit;
    }
}
