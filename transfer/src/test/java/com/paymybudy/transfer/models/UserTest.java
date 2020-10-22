package com.paymybudy.transfer.models;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    void tearDownClass(){

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
    }
/*
    @Order(2)
    @Test
    void createUserAndFriend() {
        //Given
        User user = em.createQuery("select u from User u where u.firstName = ?1 " +
                "and u.lastName = ?2 and u.email=?3 and u.password = ?4", User.class)
                .setParameter(1, "Hans").setParameter(2, "Zimmer").setParameter(3, "Hans@frei.com").setParameter(4, "XXX")
                .getSingleResult();
        User friend = em.createQuery("select u from User u where u.firstName = ?1 " +
                "and u.lastName = ?2 and u.email=?3 and u.password = ?4", User.class)
                .setParameter(1, "Susi").setParameter(2, "Labrousse").setParameter(3, "Susi@mail.com").setParameter(4, "XXX")
                .getSingleResult();
        //When
        user.addOneContact(friend);
        //tx.begin();
        em.persist(user);
        //tx.commit();

        //THEN
        assertEquals("Hans@frei.com", friend.getContactList().get(0).getEmail());
        assertEquals("Susi@mail.com", user.getContactList().get(0).getEmail());
    }*/

//https://stuetzpunkt.wordpress.com/2013/10/19/jpa-recursive-manytomany-relationship/


}