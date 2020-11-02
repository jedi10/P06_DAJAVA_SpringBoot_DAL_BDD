package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "InternalTransaction")
@Table(name = "INTERNAL_TRANSACTION")
@NoArgsConstructor
public class InternalTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_transaction_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "description", length = 350)
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private double amount;

    @Column(name = "status_date", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime statusDate;

    @Column(length = 15)
    @Getter
    @Setter
    private EnumTransacStatus status;

    @OneToMany(mappedBy = "internalTransaction")
    @Getter
    @Setter
    private List<MoneyTransferType> transfertType;

    @Column(name = "transaction_message")
    @Getter
    @Setter
    private String transactionMessage;

    /**
     * <b>InternalTransaction Constructor</b>
     * @param description description
     * @param amount amount
     */
    public InternalTransaction(String description,
                               double amount) {
        this.description = description;
        this.amount = amount;
        this.statusDate = LocalDateTime.now();
        this.status = EnumTransacStatus.INITIATED;
    }

    public void executeTransaction(InternalCashAccount debitAccount, InternalCashAccount creditAccount) {

        if (debitAccount.getAmount() >= amount){
            creditAccount.setAmount(creditAccount.getAmount() + amount);
            debitAccount.setAmount(debitAccount.getAmount() - amount);
            setStatus(EnumTransacStatus.FINISHED);
            setTransactionMessage("Transaction has been executed with success");
        } else {
            setStatus(EnumTransacStatus.ABORTED);
            setTransactionMessage("Not Enough cash to execute transaction");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalTransaction)) return false;
        InternalTransaction that = (InternalTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(description, that.description) &&
                statusDate.equals(that.statusDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, amount, statusDate);
    }
}

//https://www.baeldung.com/jpa-java-time
//https://stackoverflow.com/questions/54840769/how-to-persist-localdate-with-jpa
//https://www.baeldung.com/jpa-many-to-many