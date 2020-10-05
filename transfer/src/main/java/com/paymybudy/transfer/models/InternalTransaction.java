package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class InternalTransaction {
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
    private InternalCashAccount accountDebit;
    @Getter
    @Setter
    private InternalCashAccount accountCredit;
    @Getter
    @Setter
    private String transactionMessage;

    /**
     * <b>InternalTransaction Constructor</b>
     * @param description description
     * @param date date
     * @param amount amount
     * @param accountDebit accountDebit
     * @param accountCredit accountCredit
     */
    public InternalTransaction(String description, LocalDate date,
                               double amount,
                               InternalCashAccount accountDebit, InternalCashAccount accountCredit) {
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.status = EnumTransacStatus.INITIATED;
        this.accountDebit = accountDebit;
        this.accountCredit = accountCredit;
    }

    public void executeTransaction() {
        if (accountDebit.getAmount() >= amount){
            accountCredit.setAmount(accountCredit.getAmount() + amount);
            accountDebit.setAmount(accountDebit.getAmount() - amount);
            setStatus(EnumTransacStatus.FINISHED);
            setTransactionMessage("Transaction has been executed with success");
        } else {
            setStatus(EnumTransacStatus.ABORTED);
            setTransactionMessage("Not Enough cash to execute transaction");
        }
    }
}
