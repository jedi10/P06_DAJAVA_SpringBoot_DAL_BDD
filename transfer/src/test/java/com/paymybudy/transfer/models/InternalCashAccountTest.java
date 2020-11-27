package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InternalCashAccountTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Order(1)
    @Test
    void checkUniqueCashAccountNumber() {
        //When
        InternalCashAccount cashAccount = new InternalCashAccount(
                "EA234",
                "Livret de depot standard");

        InternalCashAccount cashAccount2 = new InternalCashAccount(
                "EA234",
                "Livret de depot standard");

        //Then
        assertNotNull(cashAccount);
        assertNotNull(cashAccount2);

        assertNotNull(cashAccount.getNumber());
        assertNotNull(cashAccount2.getNumber());

        assertNotEquals(cashAccount.getNumber(), cashAccount2.getNumber());
    }

    @Order(2)
    @Test
    void constructor_amountValue() {
        //When
        InternalCashAccount cashAccount = new InternalCashAccount(
                "1234564343",
                "Livret de depot standard");
        //Then
        assertNotNull(cashAccount);
        assertEquals(0, cashAccount.getAmount());
    }


    @Order(3)
    @Test
    void addCash() {
        //Given
        InternalCashAccount cashAccount = new InternalCashAccount(
                "1234564343",
                "Livret de depot standard");
        //When
        cashAccount.addCash(101);
        //Then
        assertEquals(101, cashAccount.getAmount());
    }

    @Order(4)
    @Test
    void removeCash() {
        //Given
        InternalCashAccount cashAccount = new InternalCashAccount(
                "1234564343",
                "Livret de depot standard");
        cashAccount.addCash(101);
        //When
        cashAccount.removeCash(55);
        //Then
        assertEquals(101-55, cashAccount.getAmount());
    }
}