package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Embeddable Table used for MoneyTransferType Primary Key</b>
 * <p>Define a primary key composed with internalCashAccount ID and internalTransaction ID</p>
 * <p>Serializable, Equals and Hash Code are mandatory</p>
 */
@Embeddable
@NoArgsConstructor
public class MoneyTransferTypeKey implements Serializable {

    @Column(name="int_cash_account_id")
    @Getter
    @Setter
    Long internalCashAccountId;

    @Column(name="int_transaction_id")
    @Getter
    @Setter
    Long internalTransactionId;

    public MoneyTransferTypeKey(Long cashAccountId, Long transactionId) {
        this.internalCashAccountId = cashAccountId;
        this.internalTransactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyTransferTypeKey)) return false;
        MoneyTransferTypeKey that = (MoneyTransferTypeKey) o;
        return internalCashAccountId.equals(that.internalCashAccountId) &&
                internalTransactionId.equals(that.internalTransactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalCashAccountId, internalTransactionId);
    }
}

//https://www.baeldung.com/jpa-many-to-many