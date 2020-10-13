package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BankAccountTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    /*
    @Order(1)
    @Test
    void checkConstructor_ListEmpty() {
        //Given
        String name = "livret standart";
        String tel = "0344556677";
        String address = "place de la bourse 75000 Paris";


        //When
        BankAccount bankAccount = new BankAccount(name, tel, address);

        //Then
        assertNotNull(bankAccount);

        assertNotNull(bankAccount.getExternalTransactionList());
        assertTrue(bankAccount.getExternalTransactionList().isEmpty());
    }*/

}