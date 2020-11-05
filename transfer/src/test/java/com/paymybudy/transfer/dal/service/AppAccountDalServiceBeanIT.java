package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.AppAccount;
import com.paymybudy.transfer.models.EnumAppAccountStatus;
import com.paymybudy.transfer.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppAccountDalServiceBeanIT {

    @Autowired
    private IAppAccountDalService appAccountDalService;
    @Autowired
    private UserDalServiceBean userDalServiceBean;

    private static List<AppAccount> appAccountsGiven = new ArrayList<>();

    static {
        //GIVEN
        User user1 = new User("John", "Carter", "carter@paymybuddy.com","xxxx");
        User user2 = new User("Lidia", "Topiac", "topiac@paymybuddy.com","xxxx");
        AppAccount appAccount1 = null;
        AppAccount appAccount2 = null;
        try {
            appAccount1 = new AppAccount(user1, EnumAppAccountStatus.NOTCONFIRMED);
            appAccount2 = new AppAccount(user2, EnumAppAccountStatus.NOTCONFIRMED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        appAccountsGiven.add(appAccount1);
        appAccountsGiven.add(appAccount2);
    }
    private User userCreated = new User("Jack", "Holster", "holster@paymybuddy.com", "xxxx");
    private AppAccount appAccountToCreate = new AppAccount(userCreated, EnumAppAccountStatus.NOTCONFIRMED);
    private User userToUpdate = new User("Tobias", "Hamsterdil", "hamsterdil@paymybuddy.com", "xxxx");
    private AppAccount appAccountToUpdate = new AppAccount(userToUpdate, EnumAppAccountStatus.NOTCONFIRMED);

    AppAccountDalServiceBeanIT() throws Exception {
    }


    @BeforeAll
    void setUpAll() {
        //GIVEN
        User user1 = userDalServiceBean.create(appAccountsGiven.get(0).getUser());
        //appAccountsGiven.get(0).getUser().setId(user1.getId());
        User user2 = userDalServiceBean.create(appAccountsGiven.get(1).getUser());
        //appAccountsGiven.get(1).getUser().setId(user2.getId());
    }

    @AfterEach
    void tearDown() {

    }

    @Order(1)
    @Test
    void serviceDeclarationInstantiation() {
        assertNotNull(appAccountDalService,
                "you have forgot to declare appAccountDalService as a SpringBoot service or to autowire it");
    }

    @Order(2)
    @Test
    void create() {
        //GIVEN
        assertNotNull(appAccountsGiven);
        assertNotNull(appAccountsGiven.get(0));
        assertNotNull(appAccountsGiven.get(0).getUser());
        assertNotNull(appAccountsGiven.get(0).getUser().getId(),
                "You have to save user in DBB");
        assertNotNull(appAccountsGiven.get(1));
        assertNotNull(appAccountsGiven.get(1).getUser());
        assertNotNull(appAccountsGiven.get(1).getUser().getId(),
                "You have to save user in DBB");
        //WHEN
        AppAccount appAccountResult1 = appAccountDalService.create(appAccountsGiven.get(0));
        AppAccount appAccountResult2 = appAccountDalService.create(appAccountsGiven.get(1));

        //THEN
        assertEquals(appAccountsGiven.get(0), appAccountResult1);
        assertNotNull(appAccountResult1.getId());
        assertEquals(appAccountsGiven.get(1), appAccountResult2);
        assertNotNull(appAccountResult2.getId());
    }

    @Order(3)
    @Test
    void findAll() {

        //WHEN
        List<AppAccount> appAccountsResult = appAccountDalService.findAll();

        //THEN
        assertNotNull(appAccountsResult);
        assertEquals(appAccountsGiven.size(), appAccountsResult.size());
        assertEquals(appAccountsGiven.get(0).getUser(),
                appAccountsResult.get(0).getUser());
        assertEquals(appAccountsGiven.get(1).getUser(),
                appAccountsResult.get(1).getUser());
    }


    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        User userCreatedResult = userDalServiceBean.create(userCreated);
        AppAccount appAccountCreatedResult = appAccountDalService.create(appAccountToCreate);
        assertEquals(userCreated.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());
        assertEquals(appAccountToCreate.getUser(), appAccountCreatedResult.getUser());
        assertNotNull(appAccountCreatedResult.getId());

        //WHEN
        AppAccount appAccountResult = appAccountDalService.findOne(appAccountCreatedResult.getId());

        //THEN
        assertNotNull(appAccountResult, "appAccountToCreate has not been created or can not be find");
        assertEquals(appAccountCreatedResult.getUser(), appAccountResult.getUser());
        assertEquals(appAccountCreatedResult.getId(), appAccountResult.getId());
    }

    @Order(5)
    @Test
    void update() throws Exception {
        //GIVEN
        //USER Creation
        User userCreatedResult = userDalServiceBean.create(userToUpdate);
        assertEquals(userToUpdate.getEmail(), userCreatedResult.getEmail());
        assertNotNull(userCreatedResult.getId());
        //App Account Creation
        AppAccount appAccountCreatedResult =  appAccountDalService.create(appAccountToUpdate);
        assertEquals(appAccountToUpdate.getUser(), appAccountCreatedResult.getUser());
        assertNotNull(appAccountCreatedResult.getId());
        assertEquals(EnumAppAccountStatus.NOTCONFIRMED, appAccountCreatedResult.getAppAccountStatus());
        //Change Account Status but no Update
        appAccountCreatedResult.setAppAccountStatus(EnumAppAccountStatus.SUSPENDED);
        assertEquals(EnumAppAccountStatus.SUSPENDED, appAccountCreatedResult.getAppAccountStatus());

        //WHEN Update
        AppAccount appAccountUpdateResult = appAccountDalService.update(appAccountCreatedResult);

        //THEN
        assertEquals(appAccountCreatedResult.getAppAccountStatus(), appAccountUpdateResult.getAppAccountStatus());
    }

    @Order(6)
    @Test
    void delete() {
        List<AppAccount> appAccountsResult = appAccountDalService.findAll();
        assertTrue(appAccountsResult.size() > 0);
        int userListSizeAtStart = appAccountsResult.size();
        AppAccount appAccountToRemove = appAccountsResult.get(0);
        assertTrue(appAccountsResult.contains(appAccountToRemove));

        //WHEN
        appAccountDalService.delete(appAccountToRemove.getId());

        //THEN
        List<AppAccount> appAccountsResultAfter = appAccountDalService.findAll();
        assertEquals(userListSizeAtStart-1, appAccountsResultAfter.size());
        assertFalse(appAccountsResultAfter.contains(appAccountToRemove));
        assertNull(userDalServiceBean.findOne(appAccountToRemove.getUser().getId()),
                "User can not survive to the suppression of his account: on delete cascade");
    }

    @Order(7)
    @Test
    void deleteAll() {
        List<AppAccount> appAccountsResult = appAccountDalService.findAll();
        assertTrue(appAccountsResult.size() > 0);

        //WHEN
        appAccountDalService.deleteAll();

        //THEN
        List<User> usersResultAfter = userDalServiceBean.findAll();
        assertEquals(0, usersResultAfter.size());
        List<AppAccount> appAccountsResultAfter = appAccountDalService.findAll();
        assertEquals(0, appAccountsResultAfter.size());
    }
}