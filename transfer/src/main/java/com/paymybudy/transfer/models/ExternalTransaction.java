package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "ExternalTransaction")
@Table(name = "EXTERNAL_TRANSACTION")
@NoArgsConstructor
public class ExternalTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ext_transaction_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "description", length = 350)
    @Getter
    @Setter
    private String description;

    @Column(name = "status_date", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime statusDate;
    @Getter
    @Setter
    private double amount;

    @Column(length = 15)
    @Getter
    @Setter
    private EnumTransacStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_fk", referencedColumnName = "bank_account_id")
    @Getter
    @Setter
    private BankAccount accountDebit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "int_cash_account_fk", referencedColumnName = "int_cash_account_id")
    @Getter
    @Setter
    private InternalCashAccount accountCredit;

    /**
     * <b>ExternalTransaction Constructor</b>
     * @param description description
     * @param amount amount
     * @param status status
     * @param accountDebit accountDebit
     * @param accountCredit accountCredit
     */
    public ExternalTransaction(String description, double amount, EnumTransacStatus status, BankAccount accountDebit, InternalCashAccount accountCredit) {
        this.description = description;
        this.statusDate = LocalDateTime.now();
        this.amount = amount;
        this.status = status;
        this.accountDebit = accountDebit;
        this.accountCredit = accountCredit;
        executeExternalTransfer();
    }

    public void executeExternalTransfer(){
        if (this.accountDebit != null && this.accountCredit != null){
            double initialAmount = this.accountCredit.getAmount();
            this.accountCredit.setAmount( initialAmount + this.amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalTransaction)) return false;
        ExternalTransaction that = (ExternalTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, amount);
    }
}


//https://www.baeldung.com/jpa-java-time