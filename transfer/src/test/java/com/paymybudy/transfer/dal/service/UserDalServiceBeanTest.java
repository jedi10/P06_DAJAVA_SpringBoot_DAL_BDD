package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.User;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDalServiceBeanTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
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
    private User userToUpdate = new User("Tobias", "Hamsterdil", "hamsterdil@paymybuddy.com", "xxxx");

    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(userRepository.findAll()).thenReturn(usersGiven);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(usersGiven.get(0)));
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usersGiven.get(0)));
        when(userRepository.save(any())).thenReturn(userToCreate);
        //For update operation
        userToUpdate.setId(251L);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("XXXXX");
        //for Delete Operation
        when(userRepository.save(usersGiven.get(0))).thenReturn(usersGiven.get(0));
        when(userRepository.save(usersGiven.get(1))).thenReturn(usersGiven.get(1));
        userDalService.passwordEncoder = this.passwordEncoder;
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void serviceInstantiation() {
        assertNotNull(userDalService,
                "you have forgot to create an Instance userDalService and inject Mock Repository inside");
    }

    @Order(2)
    @Test
    void findAll() {
        assertNotNull(userDalService);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findAll();
        //WHEN
        List<User> usersResult = userDalService.findAll();

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

    @Order(3)
    @Test
    void findOne() {
        //GIVEN
        User userExpected = usersGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        User userResult = userDalService.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(userExpected.getEmail(), userResult.getEmail());
    }

    @DisplayName("Spring Security: load By Email with Null Param")
    @Order(4)
    @Test
    void loadUserByUsername_nullParam() {
        //WHEN
        Exception exception = assertThrows(NullPointerException.class, () -> {
                    userDalService.findByEmail(null);
        });
        //THEN
        assertTrue(exception.getMessage().contains(
                "email is marked non-null but is null"));
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.never()).findByEmail(Mockito.anyString());
    }

    @DisplayName("Spring Security: load By Email")
    @Order(5)
    @Test
    void loadUserByUsername_ok() {
        //GIVEN
        User userExpected = usersGiven.get(0);
        UserDetails userDetailsExpected = new org.springframework.security.core.userdetails.User(
                userExpected.getEmail(),
                userExpected.getPassword(),
                List.of(new SimpleGrantedAuthority("role1"))
        );
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findByEmail(Mockito.anyString());

        //WHEN
        UserDetails userDetailsResult = userDalService.loadUserByUsername("tartantion@email.fr");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());

        //THEN
        assertEquals(userDetailsExpected, userDetailsResult);
    }

    @Order(6)
    @Test
    void findByEmail() {
        //GIVEN
        User userExpected = usersGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findByEmail(Mockito.anyString());

        //WHEN
        User userResult = userDalService.findByEmail("tartantion@email.fr");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findByEmail(Mockito.anyString());

        //THEN
        assertEquals(userExpected, userResult);
    }

    @DisplayName("Spring Security: user creation with null param")
    @Order(7)
    @Test
    void create2_nullParam() {
        //WHEN
        Exception exception = assertThrows(NullPointerException.class, () -> {
            userDalService.create2(null);
        });
        //THEN
        assertTrue(exception.getMessage().contains(
                "userRegistrationDto is marked non-null but is null"));

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.never()).save(any());
    }

    @DisplayName("Spring Security: user creation ok")
    @Order(8)
    @Test
    void create2_ok() {
        //GIVEN
        UserRegistrationDto userDtoToCreate = new UserRegistrationDto(
                userToCreate.getFirstName(),
                userToCreate.getLastName(),
                userToCreate.getEmail(),
                userToCreate.getPassword()
        );
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).save(any());

        //WHEN
        User userResult = userDalService.create2(userDtoToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).save(any());

        assertEquals(userToCreate, userResult);
    }

    @Order(9)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).save(any());

        //WHEN
        User userResult = userDalService.create(userToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).save(any());

        assertEquals(userToCreate, userResult);
    }

    @Order(10)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).findById(userToUpdate.getId());
        verify(userRepository, Mockito.never()).save(userToUpdate);

        //WHEN
        User userResult = userDalService.update(userToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).findById(userToUpdate.getId());
        verify(userRepository, Mockito.times(1)).save(userToUpdate);

        assertEquals(userToUpdate, userResult);
    }

    @Order(11)
    @Test
    void delete() {
        //GIVEN
        usersGiven.get(0).setId(123L);
        usersGiven.get(1).setId(321L);
        usersGiven.get(0).getContactList().add(userToUpdate);
        usersGiven.get(1).getContactList().add(userToUpdate);
        assertTrue(usersGiven.get(0).getContactList().contains(userToUpdate));
        assertTrue(usersGiven.get(1).getContactList().contains(userToUpdate));
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).save(usersGiven.get(0));
        verify(userRepository, Mockito.never()).save(usersGiven.get(1));
        verify(userRepository, Mockito.never()).findById(userToUpdate.getId());
        verify(userRepository, Mockito.never()).delete(userToUpdate);

        //WHEN
        userDalService.delete(userToUpdate.getId());

        //THEN
        //User have to be removed from all others Friend List
        assertFalse(usersGiven.get(0).getContactList().contains(userToUpdate));
        assertFalse(usersGiven.get(1).getContactList().contains(userToUpdate));

        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).save(usersGiven.get(0));
        verify(userRepository, Mockito.times(1)).save(usersGiven.get(1));
        verify(userRepository, Mockito.times(1)).findById(userToUpdate.getId());
        verify(userRepository, Mockito.times(1)).delete(userToUpdate);
    }

    @Order(12)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(userRepository, Mockito.never()).deleteAll();

        //WHEN
        userDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(userRepository, Mockito.times(1)).deleteAll();
    }
}