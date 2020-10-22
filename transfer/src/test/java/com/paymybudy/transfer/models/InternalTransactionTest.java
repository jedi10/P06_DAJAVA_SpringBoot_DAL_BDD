package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InternalTransactionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @ParameterizedTest
    @CsvSource({"2000, 1500", "1500.50, 1499.99"})
    void executeTransaction_Ok(double amountAccount, double amountTransferred){
        //GIVEN
        InternalCashAccount debitAccount = new InternalCashAccount("12345","Compte de depot 12345 de Mr Smith");
        debitAccount.addCash(amountAccount);
        double initialAmountForDonor = debitAccount.getAmount();
        InternalCashAccount creditAccount = new InternalCashAccount("54321","Compte de depot 54321 De Mme Digger");
        double initialAmountForBeneficiary = creditAccount.getAmount();
        InternalTransaction transaction = new InternalTransaction(
                "Transfer Mensuel pour Service Art et culture",
                amountTransferred);

        //WHEN
        transaction.executeTransaction(debitAccount, creditAccount);

        //THEN
        assertEquals(EnumTransacStatus.FINISHED ,transaction.getStatus());
        assertEquals(initialAmountForBeneficiary+amountTransferred, creditAccount.getAmount());
        assertEquals(initialAmountForDonor-amountTransferred, debitAccount.getAmount());
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({"1000, 1500", "10.49, 15.99"})
    void executeTransaction_Failed(double amountAccount, double amountTransferred){
        //GIVEN
        InternalCashAccount debitAccount = new InternalCashAccount("12345","Compte de depot 12345 de Mr Smith");
        debitAccount.addCash(amountAccount);
        double initialAmountForDonor = debitAccount.getAmount();
        InternalCashAccount creditAccount = new InternalCashAccount("54321","Compte de depot 54321 De Mme Digger");
        double initialAmountForBeneficiary = creditAccount.getAmount();

        InternalTransaction transaction = new InternalTransaction(
                "Transfer Mensuel pour Service Art et culture",
                amountTransferred);

        //WHEN
        transaction.executeTransaction(debitAccount, creditAccount);

        //THEN
        assertEquals(EnumTransacStatus.ABORTED , transaction.getStatus());
        assertEquals(initialAmountForBeneficiary, creditAccount.getAmount());
        assertEquals(initialAmountForDonor, debitAccount.getAmount());
    }
}