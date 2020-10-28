package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IAppAccountRepository;
import com.paymybudy.transfer.models.AppAccount;
import com.paymybudy.transfer.models.EnumAppAccountStatus;
import com.paymybudy.transfer.models.User;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppAccountDalServiceBeanTest {

    private IAppAccountDalService appAccountDalServiceBean;
    @Mock
    private IAppAccountRepository appAccountRepository;

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

    AppAccountDalServiceBeanTest() throws Exception {
    }


    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(appAccountRepository.findAll()).thenReturn(appAccountsGiven);
        when(appAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(appAccountsGiven.get(0)));
        when(appAccountRepository.save(any())).thenReturn(appAccountToCreate);
        //For update operation
        appAccountToUpdate.setId(251L);
        when(appAccountRepository.save(appAccountToUpdate)).thenReturn(appAccountToUpdate);
        when(appAccountRepository.findById(appAccountToUpdate.getId())).thenReturn(Optional.of(appAccountToUpdate));

        //INSERT Mock
        appAccountDalServiceBean = new AppAccountDalServiceBean(appAccountRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void findAll() {
        assertNotNull(appAccountDalServiceBean,
                "you have forget to declare AppAccountService as a service to SpringBoot");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).findAll();

        //WHEN
        List<AppAccount> appAccountsResult = appAccountDalServiceBean.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).findAll();

        assertNotNull(appAccountsResult);
        assertEquals(appAccountsGiven.size(), appAccountsResult.size());
        assertEquals(appAccountsGiven.get(0), appAccountsResult.get(0));
        assertEquals(appAccountsGiven.get(1), appAccountsResult.get(1));
    }

    @Order(2)
    @Test
    void findOne() {
        //GIVEN
        AppAccount appAccountExpected = appAccountsGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        AppAccount appAccountResult = appAccountDalServiceBean.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(appAccountExpected.getUser(), appAccountResult.getUser());
    }

    @Order(3)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).save(any());

        //WHEN
        AppAccount appAccountResult = appAccountDalServiceBean.create(appAccountToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).save(any());

        assertEquals(appAccountToCreate, appAccountResult);
    }

    @Order(4)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).findById(appAccountToUpdate.getId());
        verify(appAccountRepository, Mockito.never()).save(appAccountToUpdate);

        //WHEN
        AppAccount appAccountResult = appAccountDalServiceBean.update(appAccountToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).findById(appAccountToUpdate.getId());
        verify(appAccountRepository, Mockito.times(1)).save(appAccountToUpdate);

        assertEquals(appAccountToUpdate, appAccountResult);
    }

    @Order(5)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).findById(appAccountToUpdate.getId());
        verify(appAccountRepository, Mockito.never()).delete(appAccountToUpdate);

        //WHEN
        appAccountDalServiceBean.delete(appAccountToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).findById(appAccountToUpdate.getId());
        verify(appAccountRepository, Mockito.times(1)).delete(appAccountToUpdate);
    }

    @Order(6)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(appAccountRepository, Mockito.never()).deleteAll();

        //WHEN
        appAccountDalServiceBean.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(appAccountRepository, Mockito.times(1)).deleteAll();
    }
}