package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void checkUserConstructor_ListEmpty() {
        //Given
        String firstName = "bill";
        String lastName = "gates";
        String email = "billgates@vaccinforeveryone.com";
        String password = "bilou666";

        //When
        User user = new User(firstName, lastName, email, password);

        //Then
        assertNotNull(user);

        assertNotNull(user.getContactList());
        assertTrue(user.getContactList().isEmpty());

        assertNotNull(user.getExternalTransactionList());
        assertTrue(user.getExternalTransactionList().isEmpty());

        assertNotNull(user.getInternalTransactionList());
        assertTrue(user.getInternalTransactionList().isEmpty());


    }


}