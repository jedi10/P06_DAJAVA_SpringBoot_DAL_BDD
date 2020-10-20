package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Setter
    private EnumTransacStatus status;

    @OneToMany(mappedBy = "internalTransaction")
    @Getter
    @Setter
    private List<MoneyTransfertType> transfertType;

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
}

//https://www.baeldung.com/jpa-java-time
//https://stackoverflow.com/questions/54840769/how-to-persist-localdate-with-jpa
//https://www.baeldung.com/jpa-many-to-many