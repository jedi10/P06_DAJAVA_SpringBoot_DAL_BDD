package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppAccountTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void setAppAccountStatus() throws Exception {
        //GIVEN
        String firstName = "bill";
        String lastName = "gates";
        String email = "billgates@vaccinforeveryone.com";
        String password = "bilou666";
        User user = new User(firstName, lastName, email, password);
        //WHEN
        AppAccount appAccount = new AppAccount(user, EnumAppAccountStatus.NOTCONFIRMED);
        //THEN
        assertEquals(EnumAppAccountStatus.NOTCONFIRMED, appAccount.getAppAccountStatus());
    }

    @Order(2)
    @Test
    void setAppAccountStatus_Exception() throws Exception {
        //GIVEN
        String firstName = "bill";
        String lastName = "gates";
        String email = "billgates@vaccinforeveryone.com";
        String password = "bilou666";
        User user = new User(firstName, lastName, email, password);
        //WHEN

        //THEN
        Exception exception = assertThrows(Exception.class, ()-> {
            new AppAccount(user, EnumAppAccountStatus.CONFIRMED);
        } );
        assertTrue(exception.getMessage().contains(
                "User Bank Account and User Internal Account have to be created"));
    }
}