package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IExternalTransactionRepository;
import com.paymybudy.transfer.models.*;
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
class ExternalTransactionDalServiceBeanTest {

    private IExternalTransactionDalService externalTransactionDalService;
    @Mock
    private IExternalTransactionRepository externalTransactionRepository;

    private static List<ExternalTransaction> extTransactionsGiven = new ArrayList<>();

    static {
        BankAccount bankAccount1 = new BankAccount("Banque Unibey","0100345678", "63 Rue de Londre 75000 Paris");
        BankAccount bankAccount2 = new BankAccount("Banque UBC","0145678676", "10 Rue des Homard 75000 Paris");
        InternalCashAccount internalCashAccount1 = new InternalCashAccount("XX23453-456", "Compte de dépot Mr Benzema" );
        InternalCashAccount internalCashAccount2 = new InternalCashAccount("XX23453-875", "Compte de dépot Mme GrandeJulie");

        ExternalTransaction externalTransaction1 = new ExternalTransaction(
                "Paiement prestation toilettage animal",
                252.55,
                EnumTransacStatus.FINISHED,
                bankAccount1,
                internalCashAccount1);
        ExternalTransaction externalTransaction2 = new ExternalTransaction(
                "Inscription Association Action pour le Climat",
                100.00,
                EnumTransacStatus.FINISHED,
                bankAccount2,
                internalCashAccount2);
        extTransactionsGiven.add(externalTransaction1);
        extTransactionsGiven.add(externalTransaction2);
    }
    private BankAccount bankAccountToCreate = new BankAccount("Banque Passilex","012345678", "1 Rue des morts 75000 Paris");
    private BankAccount bankAccountToUpdate = new BankAccount("Banque Optimex","0987654232", "3 Place Vendome 75000 Paris");
    private InternalCashAccount intCashAccountToCreate = new InternalCashAccount("XX23453-007", "Compte de dépot Mr Bond" );
    private InternalCashAccount intCashAccountToUpdate = new InternalCashAccount("XX23453-002", "Compte de dépot Mme Emma" );
    private  ExternalTransaction externalTransactionToCreate = new ExternalTransaction(
            "Paiement masque FFP2",
            55.99,
            EnumTransacStatus.FINISHED,
            bankAccountToCreate,
            intCashAccountToCreate);
    private  ExternalTransaction externalTransactionToUpdate = new ExternalTransaction(
            "Paiement Tapis",
            150,
            EnumTransacStatus.FINISHED,
            bankAccountToUpdate,
            intCashAccountToUpdate);

    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(externalTransactionRepository.findAll()).thenReturn(extTransactionsGiven);
        when(externalTransactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(extTransactionsGiven.get(0)));
        when(externalTransactionRepository.save(any())).thenReturn(externalTransactionToCreate);
        //For update operation
        externalTransactionToUpdate.setId(251L);
        when(externalTransactionRepository.save(externalTransactionToUpdate)).thenReturn(externalTransactionToUpdate);
        when(externalTransactionRepository.findById(externalTransactionToUpdate.getId())).thenReturn(Optional.of(externalTransactionToUpdate));

        //INSERT Mock
        externalTransactionDalService = new ExternalTransactionDalServiceBean(externalTransactionRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void findAll() {
        assertNotNull(externalTransactionDalService,
                "you have forget to declare externalTransactionService as a service to SpringBoot");
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).findAll();

        //WHEN
        List<ExternalTransaction> externalTransactionResult = externalTransactionDalService.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).findAll();

        assertNotNull(externalTransactionResult);
        assertEquals(extTransactionsGiven.size(), externalTransactionResult.size());
        assertEquals(extTransactionsGiven.get(0), externalTransactionResult.get(0));
        assertEquals(extTransactionsGiven.get(1), externalTransactionResult.get(1));
    }

    @Order(2)
    @Test
    void findOne() {
        //GIVEN
        ExternalTransaction extTransactionExpected = extTransactionsGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        ExternalTransaction externalTransactionResult = externalTransactionDalService.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(extTransactionExpected.getAmount(), externalTransactionResult.getAmount());
        assertEquals(extTransactionExpected.getStatusDate(), externalTransactionResult.getStatusDate());
    }

    @Order(3)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).save(any());

        //WHEN
        ExternalTransaction extTransactionCreatedResult = externalTransactionDalService.create(externalTransactionToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).save(any());

        assertEquals(externalTransactionToCreate, extTransactionCreatedResult);
    }

    @Order(4)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).findById(externalTransactionToUpdate.getId());
        verify(externalTransactionRepository, Mockito.never()).save(externalTransactionToUpdate);

        //WHEN
        ExternalTransaction extTransactionUpdatedResult = externalTransactionDalService.update(externalTransactionToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).findById(externalTransactionToUpdate.getId());
        verify(externalTransactionRepository, Mockito.times(1)).save(externalTransactionToUpdate);

        assertEquals(externalTransactionToUpdate, extTransactionUpdatedResult);
    }

    @Order(5)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).findById(externalTransactionToUpdate.getId());
        verify(externalTransactionRepository, Mockito.never()).delete(externalTransactionToUpdate);

        //WHEN
        externalTransactionDalService.delete(externalTransactionToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).findById(externalTransactionToUpdate.getId());
        verify(externalTransactionRepository, Mockito.times(1)).delete(externalTransactionToUpdate);
    }

    @Order(6)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.never()).deleteAll();

        //WHEN
        externalTransactionDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(externalTransactionRepository, Mockito.times(1)).deleteAll();
    }
}