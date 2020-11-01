package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IInternalTransactionRepository;
import com.paymybudy.transfer.models.ExternalTransaction;
import com.paymybudy.transfer.models.InternalTransaction;
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
class InternalTransactionDalServiceBeanTest {

    private IInternalTransactionDalService internalTransactionDalService;
    @Mock
    private IInternalTransactionRepository internalTransactionRepository;

    private static List<InternalTransaction> intTransactionsGiven = new ArrayList<>();

    static {
        InternalTransaction intTransaction1 = new InternalTransaction("Paiement repassage",300);
        InternalTransaction intTransaction2 = new InternalTransaction("Cadeau Anniversaire",500);
        intTransactionsGiven.add(intTransaction1);
        intTransactionsGiven.add(intTransaction2);
    }

    private  InternalTransaction internalTransactionToCreate = new InternalTransaction("Remboursement emprunt", 990);
    private  InternalTransaction internalTransactionToUpdate = new InternalTransaction("Paiement garde animaux", 150);


    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(internalTransactionRepository.findAll()).thenReturn(intTransactionsGiven);
        when(internalTransactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(intTransactionsGiven.get(0)));
        when(internalTransactionRepository.save(any())).thenReturn(internalTransactionToCreate);
        //For update operation
        internalTransactionToUpdate.setId(251L);
        when(internalTransactionRepository.save(internalTransactionToUpdate)).thenReturn(internalTransactionToUpdate);
        when(internalTransactionRepository.findById(internalTransactionToUpdate.getId())).thenReturn(Optional.of(internalTransactionToUpdate));

        //INSERT Mock
        internalTransactionDalService = new InternalTransactionDalServiceBean(internalTransactionRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void findAll() {
        assertNotNull(internalTransactionDalService,
                "you have forget to declare internalTransactionService as a service to SpringBoot");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).findAll();

        //WHEN
        List<InternalTransaction> internalTransactionResult = internalTransactionDalService.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).findAll();

        assertNotNull(internalTransactionResult);
        assertEquals(intTransactionsGiven.size(), internalTransactionResult.size());
        assertEquals(intTransactionsGiven.get(0), internalTransactionResult.get(0));
        assertEquals(intTransactionsGiven.get(1), internalTransactionResult.get(1));
    }

    @Order(2)
    @Test
    void findOne() {
        //GIVEN
        InternalTransaction intTransactionExpected = intTransactionsGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        InternalTransaction internalTransactionResult = internalTransactionDalService.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(intTransactionExpected.getAmount(), internalTransactionResult.getAmount());
        assertEquals(intTransactionExpected.getStatusDate(), internalTransactionResult.getStatusDate());
    }

    @Order(3)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).save(any());

        //WHEN
        InternalTransaction extTransactionCreatedResult = internalTransactionDalService.create(internalTransactionToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).save(any());

        assertEquals(internalTransactionToCreate, extTransactionCreatedResult);
    }

    @Order(4)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).findById(internalTransactionToUpdate.getId());
        verify(internalTransactionRepository, Mockito.never()).save(internalTransactionToUpdate);

        //WHEN
        InternalTransaction extTransactionUpdatedResult = internalTransactionDalService.update(internalTransactionToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).findById(internalTransactionToUpdate.getId());
        verify(internalTransactionRepository, Mockito.times(1)).save(internalTransactionToUpdate);

        assertEquals(internalTransactionToUpdate, extTransactionUpdatedResult);
    }

    @Order(5)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).findById(internalTransactionToUpdate.getId());
        verify(internalTransactionRepository, Mockito.never()).delete(internalTransactionToUpdate);

        //WHEN
        internalTransactionDalService.delete(internalTransactionToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).findById(internalTransactionToUpdate.getId());
        verify(internalTransactionRepository, Mockito.times(1)).delete(internalTransactionToUpdate);
    }

    @Order(6)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.never()).deleteAll();

        //WHEN
        internalTransactionDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(internalTransactionRepository, Mockito.times(1)).deleteAll();
    }
}