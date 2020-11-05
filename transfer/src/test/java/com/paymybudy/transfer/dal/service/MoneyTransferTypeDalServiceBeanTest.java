package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IMoneyTransferTypeRepository;
import com.paymybudy.transfer.models.InternalCashAccount;
import com.paymybudy.transfer.models.InternalTransaction;
import com.paymybudy.transfer.models.MoneyTransferType;
import com.paymybudy.transfer.models.MoneyTransferTypeKey;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
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
class MoneyTransferTypeDalServiceBeanTest {

    @Mock
    private IMoneyTransferTypeRepository moneyTransferTypeRepository;

    @InjectMocks
    private MoneyTransferTypeDalServiceBean moneyTransferTypeDalService;

    private static List<MoneyTransferType> moneyTransferTypesGiven = new ArrayList<>();

    static {
        InternalTransaction intTransaction1 = new InternalTransaction("Paiement repassage",300);
        InternalCashAccount internalCashAccount1 = new InternalCashAccount("XX23453-456", "Compte de dépot Mr Benzema" );
        MoneyTransferType moneyTransferType1 = new MoneyTransferType(internalCashAccount1, intTransaction1, true);

        InternalCashAccount internalCashAccount2 = new InternalCashAccount("XX23453-875", "Compte de dépot Mme GrandeJulie");
        MoneyTransferType moneyTransferType2 = new MoneyTransferType(internalCashAccount2, intTransaction1, false);

        moneyTransferTypesGiven.add(moneyTransferType1);
        moneyTransferTypesGiven.add(moneyTransferType2);
    }

    private InternalTransaction internalTransactionToCreate = new InternalTransaction("Remboursement emprunt", 990);
    private InternalCashAccount intCashAccountToCreate = new InternalCashAccount("XX23453-007", "Compte de dépot Mr Bond" );
    private InternalCashAccount intCashAccountToCreate2 = new InternalCashAccount("XX23453-002", "Compte de dépot Mme Emma" );
    private MoneyTransferType moneyTransferTypeToCreate = new MoneyTransferType(intCashAccountToCreate, internalTransactionToCreate, true);
    private MoneyTransferType moneyTransferTypeToCreate2 = new MoneyTransferType(intCashAccountToCreate2, internalTransactionToCreate, false);




    @BeforeEach
    void setUp() {
        //WHEN
        //CONFIG Mock
        when(moneyTransferTypeRepository.findAll()).thenReturn(moneyTransferTypesGiven);
        when(moneyTransferTypeRepository.findById(any())).thenReturn(Optional.of(moneyTransferTypesGiven.get(0)));
        when(moneyTransferTypeRepository.save(Mockito.any())).thenReturn(moneyTransferTypeToCreate);
        //For update operation
        MoneyTransferTypeKey key = new MoneyTransferTypeKey(1L,5L);
        moneyTransferTypeToCreate2.setId(key);
        when(moneyTransferTypeRepository.save(moneyTransferTypeToCreate2)).thenReturn(moneyTransferTypeToCreate2);
        when(moneyTransferTypeRepository.findById(moneyTransferTypeToCreate2.getId())).thenReturn(Optional.of(moneyTransferTypeToCreate2));
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void serviceInstantiation() {
        assertNotNull(moneyTransferTypeDalService,
                "you have forgot to create an Instance internalTransactionDalService and inject Mock Repository inside");
    }

    @Order(2)
    @Test
    void findAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).findAll();

        //WHEN
        List<MoneyTransferType> moneyTransferTypeResult = moneyTransferTypeDalService.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).findAll();

        assertNotNull(moneyTransferTypeResult);
        assertEquals(moneyTransferTypesGiven.size(), moneyTransferTypeResult.size());
        assertEquals(moneyTransferTypesGiven.get(0), moneyTransferTypeResult.get(0));
        assertEquals(moneyTransferTypesGiven.get(1), moneyTransferTypeResult.get(1));
    }

    @Order(3)
    @Test
    void findOne() {
        //GIVEN
        MoneyTransferType moneyTransferTypeExpected = moneyTransferTypesGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).findById(any());

        //WHEN
        MoneyTransferType moneyTransferTypeResult = moneyTransferTypeDalService.findOne(new MoneyTransferTypeKey());
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).findById(any());

        //THEN
        assertEquals(moneyTransferTypeExpected.getInternalCashAccount(), moneyTransferTypeResult.getInternalCashAccount());
        assertEquals(moneyTransferTypeExpected.getInternalTransaction(), moneyTransferTypeResult.getInternalTransaction());
    }

    @Order(4)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).save(any());

        //WHEN
        MoneyTransferType moneyTransferTypeCreatedResult = moneyTransferTypeDalService.create(moneyTransferTypeToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).save(any());

        assertEquals(moneyTransferTypeToCreate, moneyTransferTypeCreatedResult);
    }

    @Order(5)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).findById(moneyTransferTypeToCreate2.getId());
        verify(moneyTransferTypeRepository, Mockito.never()).save(moneyTransferTypeToCreate2);

        //WHEN
        MoneyTransferType moneyTransferTypeUpdatedResult = moneyTransferTypeDalService.update(moneyTransferTypeToCreate2);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).findById(moneyTransferTypeToCreate2.getId());
        verify(moneyTransferTypeRepository, Mockito.times(1)).save(moneyTransferTypeToCreate2);

        assertEquals(moneyTransferTypeToCreate2, moneyTransferTypeUpdatedResult);
    }

    @Order(6)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).findById(moneyTransferTypeToCreate2.getId());
        verify(moneyTransferTypeRepository, Mockito.never()).delete(moneyTransferTypeToCreate2);

        //WHEN
        moneyTransferTypeDalService.delete(moneyTransferTypeToCreate2.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).findById(moneyTransferTypeToCreate2.getId());
        verify(moneyTransferTypeRepository, Mockito.times(1)).delete(moneyTransferTypeToCreate2);
    }

    @Order(7)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.never()).deleteAll();

        //WHEN
        moneyTransferTypeDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(moneyTransferTypeRepository, Mockito.times(1)).deleteAll();
    }
}