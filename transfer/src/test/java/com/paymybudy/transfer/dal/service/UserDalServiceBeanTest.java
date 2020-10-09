package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.User;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDalServiceBeanTest {

    private UserDalServiceBean userDalServiceBean;
    @Mock
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
        //WHEN
        //CONFIG Mock
        when(userRepository.findAll()).thenReturn(usersGiven);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(usersGiven.get(0)));
        when(userRepository.save(any())).thenReturn(userCreated);
        //For update operation
        userToUpdate.setId(10L);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));

        //INSERT Mock
        userDalServiceBean = new UserDalServiceBean(userRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void findAll() {
        assertNotNull(userDalServiceBean);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findAll();
        //WHEN
        List<User> usersResult = userDalServiceBean.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findAll();

        assertNotNull(usersResult);
        assertEquals(usersGiven.size(), usersResult.size());
        assertEquals(usersGiven.get(0), usersResult.get(0));
        assertEquals(usersGiven.get(1), usersResult.get(1));
    }

    @Order(2)
    @Test
    void findOne() {
        //GIVEN
        User userExpected = usersGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        User userResult = userDalServiceBean.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(userExpected, userResult);
    }

    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).save(any());

        //WHEN
        User userResult = userDalServiceBean.create(userCreated);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).save(any());

        assertEquals(userResult, userCreated);
    }

    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findById(userToUpdate.getId());
        verify(userRepository, Mockito.never()).save(userToUpdate);

        //WHEN
        User userResult = userDalServiceBean.update(userToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findById(userToUpdate.getId());
        verify(userRepository, Mockito.times(1)).save(userToUpdate);

        assertEquals(userResult, userToUpdate);
    }

    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findById(userToUpdate.getId());
        verify(userRepository, Mockito.never()).delete(userToUpdate);

        //WHEN
        userDalServiceBean.delete(userToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findById(userToUpdate.getId());
        verify(userRepository, Mockito.times(1)).delete(userToUpdate);
    }
}