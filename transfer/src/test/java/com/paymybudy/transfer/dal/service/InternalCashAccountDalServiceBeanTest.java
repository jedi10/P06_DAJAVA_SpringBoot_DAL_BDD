package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IInternalCashAccountRepository;
import com.paymybudy.transfer.models.InternalCashAccount;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InternalCashAccountDalServiceBeanTest {

    private IInternalCashAccountDalService internalCashAccountDalService;
    @Mock
    private IInternalCashAccountRepository internalCashAccountRepository;

    private static List<InternalCashAccount> intCashAccountsGiven = new ArrayList<>();

    static {
        InternalCashAccount internalCashAccount1 = new InternalCashAccount("XX23453-456", "Compte de dépot Mr Benzema" );
        InternalCashAccount internalCashAccount2 = new InternalCashAccount("XX23453-875", "Compte de dépot Mme GrandeJulie");
        intCashAccountsGiven.add(internalCashAccount1);
        intCashAccountsGiven.add(internalCashAccount2);
    }

    private InternalCashAccount intCashAccountToCreate = new InternalCashAccount("XX23453-007", "Compte de dépot Mr Bond" );
    private InternalCashAccount intCashAccountToUpdate = new InternalCashAccount("XX23453-002", "Compte de dépot Mme Emma" );


    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(internalCashAccountRepository.findAll()).thenReturn(intCashAccountsGiven);
        when(internalCashAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(intCashAccountsGiven.get(0)));
        when(internalCashAccountRepository.save(any())).thenReturn(intCashAccountToCreate);
        //For update operation
        intCashAccountToUpdate.setId(251L);
        when(internalCashAccountRepository.save(intCashAccountToUpdate)).thenReturn(intCashAccountToUpdate);
        when(internalCashAccountRepository.findById(intCashAccountToUpdate.getId())).thenReturn(Optional.of(intCashAccountToUpdate));

        //INSERT Mock
        internalCashAccountDalService = new InternalCashAccountDalServiceBean(internalCashAccountRepository);
    }

    @AfterEach
    void tearDown() {
    }


    @Order(1)
    @Test
    void findAll() {
        assertNotNull(internalCashAccountDalService,
                "you have forget to declare InternalCashAccountService as a service to SpringBoot");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).findAll();

        //WHEN
        List<InternalCashAccount> intCashAccountsResult = internalCashAccountDalService.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).findAll();

        assertNotNull(intCashAccountsResult);
        assertEquals(intCashAccountsGiven.size(), intCashAccountsResult.size());
        assertEquals(intCashAccountsGiven.get(0), intCashAccountsResult.get(0));
        assertEquals(intCashAccountsGiven.get(1), intCashAccountsResult.get(1));
    }

    @Order(2)
    @Test
    void findOne() {
        //GIVEN
        InternalCashAccount intCashAccountExpected = intCashAccountsGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        InternalCashAccount intCashAccountResult = internalCashAccountDalService.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(intCashAccountExpected.getNumber(), intCashAccountResult.getNumber());
    }

    @Order(3)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).save(any());

        //WHEN
        InternalCashAccount intCashAccountCreatedResult = internalCashAccountDalService.create(intCashAccountToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).save(any());

        assertEquals(intCashAccountToCreate, intCashAccountCreatedResult);
    }

    @Order(4)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).findById(intCashAccountToUpdate.getId());
        verify(internalCashAccountRepository, Mockito.never()).save(intCashAccountToUpdate);

        //WHEN
        InternalCashAccount intCashAccountUpdatedResult = internalCashAccountDalService.update(intCashAccountToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).findById(intCashAccountToUpdate.getId());
        verify(internalCashAccountRepository, Mockito.times(1)).save(intCashAccountToUpdate);

        assertEquals(intCashAccountToUpdate, intCashAccountUpdatedResult);
    }

    @Order(5)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).findById(intCashAccountToUpdate.getId());
        verify(internalCashAccountRepository, Mockito.never()).delete(intCashAccountToUpdate);

        //WHEN
        internalCashAccountDalService.delete(intCashAccountToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).findById(intCashAccountToUpdate.getId());
        verify(internalCashAccountRepository, Mockito.times(1)).delete(intCashAccountToUpdate);
    }

    @Order(6)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.never()).deleteAll();

        //WHEN
        internalCashAccountDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalCashAccountRepository, Mockito.times(1)).deleteAll();
    }

}