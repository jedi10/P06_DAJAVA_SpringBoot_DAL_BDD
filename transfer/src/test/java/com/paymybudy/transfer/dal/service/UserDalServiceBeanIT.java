package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.User;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDalServiceBeanIT {

    @Autowired
    private UserDalServiceBean userDalService;

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
    void serviceDeclaration_instantiation() {
        assertNotNull(userDalService,
                "you have forgot to declare userDalService as a SpringBoot service or to autowire it");
    }

    @DisplayName("Spring Security: user creation with encrypt password")
    @Order(2)
    @Test
    void create() {
        //GIVEN
        List<UserRegistrationDto> userDtoList =  usersGiven.stream()
                .map(user -> {return new UserRegistrationDto(usersGiven.get(0).getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword());
                })
                .collect(Collectors.toList());
        //WHEN
        User userResult1 = userDalService.create2(userDtoList.get(0));
        User userResult2 = userDalService.create2(userDtoList.get(1));

        //THEN
        assertEquals(usersGiven.get(0).getEmail(), userResult1.getEmail());
        assertNotEquals(usersGiven.get(0).getPassword(), userResult1.getPassword());
        assertNotNull(userResult1.getId());
        usersGiven.get(0).setId(userResult1.getId());
        usersGiven.get(0).setPassword(userResult1.getPassword());
        assertEquals(usersGiven.get(1).getEmail(), userResult2.getEmail());
        assertNotEquals(usersGiven.get(1).getPassword(), userResult2.getPassword());
        assertNotNull(userResult2.getId());
        usersGiven.get(1).setId(userResult2.getId());
        usersGiven.get(1).setPassword(userResult2.getPassword());
    }

    @Order(3)
    @Test
    void create_UniqueEmailConstraint() {
        //GIVEN
        User userClone = new User(
                usersGiven.get(0).getFirstName(),
                usersGiven.get(0).getLastName(),
                usersGiven.get(0).getEmail(),
                usersGiven.get(0).getPassword());
        UserRegistrationDto userRegistrationDtoClone = new UserRegistrationDto(
                userClone.getFirstName(),
                userClone.getLastName(),
                userClone.getEmail(),
                userClone.getPassword());

        //WHEN
        Exception exception = assertThrows(DataIntegrityViolationException.class, ()-> {
            userDalService.create2(userRegistrationDtoClone);
        });
        assertTrue(exception.getMessage().contains("could not execute statement"));
        assertTrue(exception.getMessage().contains("constraint"));
    }

    @Order(4)
    @Test
    void findAll() {

        //WHEN
        List<User> usersResult = userDalService.findAll();

        //THEN
        assertNotNull(usersResult);
        assertEquals(usersGiven.size(), usersResult.size());
        assertEquals(usersGiven.get(0).getEmail(),
                usersResult.get(0).getEmail());
        assertEquals(usersGiven.get(1).getEmail(),
                usersResult.get(1).getEmail());
    }

    @Order(5)
    @Test
    void findOne() {
        //GIVEN
        User userCreatedResult = userDalService.create(userToCreate);
        assertEquals(userToCreate.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());

        //WHEN
        User userResult = userDalService.findOne(userCreatedResult.getId());

        //THEN
        assertNotNull(userResult, "userToCreate has not been created or can not be find");
        assertEquals(userCreatedResult.getEmail(), userResult.getEmail());
        assertEquals(userCreatedResult.getId(), userResult.getId());
    }

    @DisplayName("Spring Security: load by Email")
    @Order(6)
    @Test
    void loadUserByUsername() {
        //GIVEN
        UserRegistrationDto userRegistrationDtoToCreate = new UserRegistrationDto(
                userToCreateBis.getFirstName(),
                userToCreateBis.getLastName(),
                userToCreateBis.getEmail(),
                userToCreateBis.getPassword());

        User userCreatedResult = userDalService.create2(userRegistrationDtoToCreate);
        assertEquals(userToCreateBis.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());

        //WHEN
        UserDetails userResult = userDalService.loadUserByUsername(userCreatedResult.getEmail());

        //THEN
        assertNotNull(userResult, "userToCreateBis has not been created or can not be find");
        assertEquals(userCreatedResult.getEmail(), userResult.getUsername());
        assertEquals(userCreatedResult.getPassword(), userResult.getPassword());
    }

    @Order(7)
    @Test
    void findByEmail() {
        //WHEN
        User userResult = userDalService.findByEmail(usersGiven.get(0).getEmail());

        //THEN
        assertNotNull(userResult, "user can not be find");
        assertEquals(usersGiven.get(0).getEmail(), userResult.getEmail());
        assertEquals(usersGiven.get(0).getPassword(), userResult.getPassword());
        assertEquals(usersGiven.get(0).getId(), userResult.getId());
    }

    @Order(8)
    @Test
    void update() {
        //GIVEN
        User userCreatedResult = userDalService.create(userToUpdate);
        assertEquals(userToUpdate.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());
        userCreatedResult.setFirstName("Vladimir");

        //WHEN
        User userUpdateResult = userDalService.update(userToUpdate);

        //THEN
        assertEquals(userCreatedResult.getFirstName(), userUpdateResult.getFirstName());
    }

    @Order(9)
    @Test
    void delete() {
        List<User> usersResult = userDalService.findAll();
        assertTrue(usersResult.size() > 0);
        int userListSizeAtStart = usersResult.size();
        User userToRemove = userDalService.findByEmail(userToCreateBis.getEmail());
        assertTrue(usersResult.contains(userToRemove));

        //WHEN
        userDalService.delete(userToRemove.getId());

        //THEN
        List<User> usersResultAfter = userDalService.findAll();
        assertEquals(userListSizeAtStart-1, usersResultAfter.size());
        assertFalse(usersResultAfter.contains(userToRemove));
    }

    @Order(10)
    @Test
    @Sql({"/import_users.sql"})
    void addOneContact() {
        //GIVEN
        List<User> usersResult = userDalService.findAll();
        assertTrue(usersResult.size() > 1);
        User user1 = usersResult.get(0);
        User friend = usersResult.get(usersResult.size()-1);
        //WHEN
        user1.addOneContact(friend);
        User userUpdateResult = userDalService.update(user1);
        User friendUpdateResult = userDalService.update(friend);

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

    @DisplayName("Check all Friends List clean up user to delete")
    @Order(11)
    @Test
    void delete_friendList() {
        List<User> usersResult = userDalService.findAll();
        assertTrue(usersResult.size() > 0);
        int userListSizeAtStart = usersResult.size();
        User userToRemove = usersResult.get(usersResult.size()-1);
        assertTrue(usersResult.contains(userToRemove));
        //User who have userToDelete in its contactList
        User userWhoHasUserToDeleteAsFriend = usersResult.get(0);
        assertTrue(userWhoHasUserToDeleteAsFriend.getContactList().contains(userToRemove));

        //WHEN
        userDalService.delete(userToRemove.getId());

        //THEN
        //Check Friend List deletion of other user
        User userWithUpdatedContactList = userDalService.findByEmail(userWhoHasUserToDeleteAsFriend.getEmail());
        assertFalse(userWithUpdatedContactList.getContactList().contains(userToRemove));
        //Check Deletion
        List<User> usersResultAfter = userDalService.findAll();
        assertEquals(userListSizeAtStart-1, usersResultAfter.size());
        assertFalse(usersResultAfter.contains(userToRemove));
    }

    @Order(12)
    @Test
    void deleteAll() {
        List<User> usersResult = userDalService.findAll();
        assertTrue(usersResult.size() > 0);

        //WHEN
        userDalService.deleteAll();
        //usersResult.forEach(
        //       (e)-> { userDalServiceBean.delete(e.getId()); }
        //);

        //THEN
        List<User> usersResultAfter = userDalService.findAll();
        assertEquals(0, usersResultAfter.size());
    }
}

//Import SQL File, 5.
//https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
//https://www.baeldung.com/spring-boot-testing