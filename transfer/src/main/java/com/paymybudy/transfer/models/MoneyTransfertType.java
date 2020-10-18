package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
/**
 * <b>Joint Table between InternalCashAccount and Internal Transaction</b>
 * <p>Define a key property to determine if money transfer is for Credit or Debit</p>
 */
@Entity(name = "money_transfer_type")
@Table(name = "money_transfer_type")
@NoArgsConstructor
public class MoneyTransfertType {

    @EmbeddedId
    @Getter
    @Setter
    MoneyTransferTypeKey id;

    @ManyToOne
    @MapsId("internalCashAccountId")
    @JoinColumn(name = "int_cash_account_id")
    @Getter
    @Setter
    InternalCashAccount internalCashAccount;

    @ManyToOne
    @MapsId("internalTransactionId")
    @JoinColumn(name = "int_transaction_id")
    @Getter
    @Setter
    InternalTransaction internalTransaction;

    @Getter
    @Setter
    boolean isCredit;

    public MoneyTransfertType(InternalCashAccount internalCashAccount,
                              InternalTransaction internalTransaction,
                              boolean isCredit) {
        this.internalCashAccount = internalCashAccount;
        this.internalTransaction = internalTransaction;
        this.isCredit = isCredit;
    }
}
