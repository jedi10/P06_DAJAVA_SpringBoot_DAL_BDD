package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.User;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


//TODO : pass this test to H2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDalServiceBeanIT {

    @Autowired
    private UserDalServiceBean userDalServiceBean;

    @Autowired
    private IUserRepository userRepository;

    private static List<User> usersGiven = new ArrayList<>();
    static {
        //GIVEN
        User user1 = new User("John", "Carter", "carter@paymybuddy.com","xxxx");
        User user2 = new User("Lidia", "Topiac", "topiac@paymybuddy.com","xxxx");
        usersGiven.add(user1);
        usersGiven.add(user2);
    }
    private User userCreated = new User("Jack", "Holster", "holster@paymybuddy.com", "xxxx");
    private User userToUpdate = new User("Tobias", "Hamsterdil", "hamsterdil@paymybuddy.com", "xxxx");

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void create() {
        //WHEN
        User userResult1 = userDalServiceBean.create(usersGiven.get(0));
        User userResult2 = userDalServiceBean.create(usersGiven.get(1));

        //THEN
        assertEquals(usersGiven.get(0), userResult1);
        assertNotNull(userResult1.getId());
        assertEquals(usersGiven.get(1), userResult2);
        assertNotNull( userResult2.getId());
    }

    @Order(2)
    @Test
    void create_UniqueEmailConstraint() {
        //GIVEN
        User userClone = new User(
                usersGiven.get(0).getFirstName(),
                usersGiven.get(0).getLastName(),
                usersGiven.get(0).getEmail(),
                usersGiven.get(0).getPassword());

        //WHEN
        Exception exception = assertThrows(DataIntegrityViolationException.class, ()-> {
            userDalServiceBean.create(userClone);
        });
        assertTrue(exception.getMessage().contains("could not execute statement"));
        assertTrue(exception.getMessage().contains("constraint"));
    }

    @Order(3)
    @Test
    void findAll() {
        assertNotNull(userDalServiceBean);

        //WHEN
        List<User> usersResult = userDalServiceBean.findAll();

        //THEN
        assertNotNull(usersResult);
        assertEquals(usersGiven.size(), usersResult.size());
        assertEquals(usersGiven.get(0).getEmail(),
                usersResult.get(0).getEmail());
        assertEquals(usersGiven.get(1).getEmail(),
                usersResult.get(1).getEmail());
    }

    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        User userCreatedResult = userDalServiceBean.create(userCreated);
        assertEquals(userCreated.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());

        //WHEN
        User userResult = userDalServiceBean.findOne(userCreatedResult.getId());

        //THEN
        assertEquals(userCreatedResult.getEmail(), userResult.getEmail());
        assertEquals(userCreatedResult.getId(), userResult.getId());
    }

    @Order(5)
    @Test
    void update() {
        //GIVEN
        User userCreatedResult = userDalServiceBean.create(userToUpdate);
        assertEquals(userToUpdate.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());
        userCreatedResult.setFirstName("Vladimir");

        //WHEN
        User userUpdateResult = userDalServiceBean.update(userToUpdate);

        //THEN
        assertEquals(userCreatedResult.getFirstName(), userUpdateResult.getFirstName());
    }

    @Order(6)
    @Test
    void delete() {
        List<User> usersResult = userDalServiceBean.findAll();
        assertTrue(usersResult.size() > 0);

        //WHEN
        usersResult.forEach(
                (e)-> { userDalServiceBean.delete(e.getId()); }
        );
        //THEN
        List<User> usersResultAfter = userDalServiceBean.findAll();
        assertEquals(0, usersResultAfter.size());
    }
}