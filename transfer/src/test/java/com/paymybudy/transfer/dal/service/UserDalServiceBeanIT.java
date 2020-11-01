package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.User;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDalServiceBeanIT {

    @Autowired
    private UserDalServiceBean userDalServiceBean;

    private static List<User> usersGiven = new ArrayList<>();
    static {
        //GIVEN
        User user1 = new User("John", "Carter", "carter@paymybuddy.com","xxxx");
        User user2 = new User("Lidia", "Topiac", "topiac@paymybuddy.com","xxxx");
        usersGiven.add(user1);
        usersGiven.add(user2);
    }
    private User userToCreate = new User("Jack", "Holster", "holster@paymybuddy.com", "xxxx");
    private User userToCreateBis = new User("Jack", "Holster", "holsterBis@paymybuddy.com", "xxxx");
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
        User userCreatedResult = userDalServiceBean.create(userToCreate);
        assertEquals(userToCreate.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());

        //WHEN
        User userResult = userDalServiceBean.findOne(userCreatedResult.getId());

        //THEN
        assertNotNull(userResult, "userToCreate has not been created or can not be find");
        assertEquals(userCreatedResult.getEmail(), userResult.getEmail());
        assertEquals(userCreatedResult.getId(), userResult.getId());
    }

    @Order(5)
    @Test
    void findByEmail() {
        //GIVEN
        User userCreatedResult = userDalServiceBean.create(userToCreateBis);
        assertEquals(userToCreateBis.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());

        //WHEN
        User userResult = userDalServiceBean.findByEmail(userCreatedResult.getEmail());

        //THEN
        assertNotNull(userResult, "userToCreateBis has not been created or can not be find");
        assertEquals(userCreatedResult.getEmail(), userResult.getEmail());
        assertEquals(userCreatedResult.getId(), userResult.getId());
    }

    @Order(6)
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

    @Order(7)
    @Test
    void delete() {
        List<User> usersResult = userDalServiceBean.findAll();
        assertTrue(usersResult.size() > 0);
        int userListSizeAtStart = usersResult.size();
        User userToRemove = userDalServiceBean.findByEmail(userToCreateBis.getEmail());
        assertTrue(usersResult.contains(userToRemove));

        //WHEN
        userDalServiceBean.delete(userToRemove.getId());

        //THEN
        List<User> usersResultAfter = userDalServiceBean.findAll();
        assertEquals(userListSizeAtStart-1, usersResultAfter.size());
        assertFalse(usersResultAfter.contains(userToRemove));
    }

    @Order(8)
    @Test
    @Sql({"/import_users.sql"})
    void addOneContact() {
        //GIVEN
        List<User> usersResult = userDalServiceBean.findAll();
        assertTrue(usersResult.size() > 1);
        User user1 = usersResult.get(0);
        User friend = usersResult.get(usersResult.size()-1);
        //WHEN
        user1.addOneContact(friend);
        User userUpdateResult = userDalServiceBean.update(user1);
        User friendUpdateResult = userDalServiceBean.update(friend);

        //THEN
        //Check that user has a new contact
        assertEquals(user1, userUpdateResult,
                "user is not the same");
        assertEquals(1, userUpdateResult.getContactList().size());
        assertEquals(user1.getContactList().get(0), userUpdateResult.getContactList().get(0),
                "contact added is not the same");

        //Check that friend has a new contact
        assertEquals(friend, friendUpdateResult,
                "friend is not the same");
        assertEquals(1, friendUpdateResult.getContactList().size());
        assertEquals(friend.getContactList().get(0), friendUpdateResult.getContactList().get(0),
                "contact added is not the same");
    }

    @Order(9)
    @Test
    void deleteAll() {
        List<User> usersResult = userDalServiceBean.findAll();
        assertTrue(usersResult.size() > 0);

        //WHEN
        userDalServiceBean.deleteAll();
        //usersResult.forEach(
        //       (e)-> { userDalServiceBean.delete(e.getId()); }
        //);

        //THEN
        List<User> usersResultAfter = userDalServiceBean.findAll();
        assertEquals(0, usersResultAfter.size());
    }
}

//Import SQL File, 5.
//https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
//https://www.baeldung.com/spring-boot-testing