package com.paymybudy.transfer.dal.service;


import com.paymybudy.transfer.models.*;
import org.junit.jupiter.api.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MoneyTransferTypeDalServiceBeanIT {

    @Autowired
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;

    @Autowired
    private IInternalCashAccountDalService internalCashAccountDalService;

    @Autowired
    private IInternalTransactionDalService internalTransactionDalService;

    private static List<MoneyTransferType> moneyTransferTypesGiven = new ArrayList<>();

    static {
        InternalTransaction intTransaction1 = new InternalTransaction(1L,"Paiement repassage",300);
        InternalCashAccount internalCashAccount1 = new InternalCashAccount(1L, "XX23453-456", "Compte de dépot Mr Benzema" );
        //User user1 = new User("John", "Carter", "carter@paymybuddy.com","xxxx");
        //internalCashAccount1.setUser(user1);
        MoneyTransferTypeKey key1 = new MoneyTransferTypeKey(internalCashAccount1.getId(), intTransaction1.getId());
        MoneyTransferType moneyTransferType1 = new MoneyTransferType(key1 ,internalCashAccount1, intTransaction1, true);

        InternalCashAccount internalCashAccount2 = new InternalCashAccount(2L,"XX23453-875", "Compte de dépot Mme GrandeJulie");
        //User user2 = new User("Lidia", "Topiac", "topiac@paymybuddy.com","xxxx");
        //internalCashAccount2.setUser(user2);
        MoneyTransferTypeKey key2 = new MoneyTransferTypeKey(internalCashAccount2.getId(), intTransaction1.getId());
        MoneyTransferType moneyTransferType2 = new MoneyTransferType(key2, internalCashAccount2, intTransaction1, false);

        moneyTransferTypesGiven.add(moneyTransferType1);
        moneyTransferTypesGiven.add(moneyTransferType2);
    }

    private InternalTransaction internalTransactionToCreate = new InternalTransaction(2L,"Remboursement emprunt", 990);
    private InternalCashAccount intCashAccountToCreate = new InternalCashAccount(3L,"XX23453-007", "Compte de dépot Mr Bond" );
    private InternalCashAccount intCashAccountToCreate2 = new InternalCashAccount(4L,"XX23453-002", "Compte de dépot Mme Emma" );
    private MoneyTransferTypeKey key3 = new MoneyTransferTypeKey(intCashAccountToCreate.getId(), internalTransactionToCreate.getId());
    private MoneyTransferTypeKey key4 = new MoneyTransferTypeKey(intCashAccountToCreate2.getId(), internalTransactionToCreate.getId());
    private MoneyTransferType moneyTransferTypeToCreate = new MoneyTransferType(key3, intCashAccountToCreate, internalTransactionToCreate, true);
    private MoneyTransferType moneyTransferTypeToCreate2 = new MoneyTransferType(key4, intCashAccountToCreate2, internalTransactionToCreate, true);

    @BeforeAll
    void beforeAll() {
        // To deal with an MoneyTransferType, we have to create and store
        // Internal Cash Account
        internalCashAccountDalService.create(moneyTransferTypesGiven.get(0).getInternalCashAccount());
        internalCashAccountDalService.create(moneyTransferTypesGiven.get(1).getInternalCashAccount());
        internalCashAccountDalService.create(intCashAccountToCreate);
        internalCashAccountDalService.create(intCashAccountToCreate2);

        // Internal Transaction
        internalTransactionDalService.create(moneyTransferTypesGiven.get(0).getInternalTransaction());
        internalTransactionDalService.create(internalTransactionToCreate);
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    void afterAll() {
        moneyTransferTypeDalService.deleteAll();
        internalCashAccountDalService.deleteAll();
        internalTransactionDalService.deleteAll();
    }


    @Order(1)
    @Test
    void serviceDeclarationInstantiation() {
        assertNotNull(internalTransactionDalService,
                "you have forgot to declare moneyTransferTypeDalService as a SpringBoot service or to autowire it");
    }

    @Order(2)
    @Test
    void create() {
        List<InternalCashAccount>intCashAccountsResult = internalCashAccountDalService.findAll();


        //WHEN
        MoneyTransferType moneyTransferTypeResult1 = moneyTransferTypeDalService.create(moneyTransferTypesGiven.get(0));
        MoneyTransferType moneyTransferTypeResult2 = moneyTransferTypeDalService.create(moneyTransferTypesGiven.get(1));

        //THEN
        assertEquals(moneyTransferTypesGiven.get(0).getId(), moneyTransferTypeResult1.getId());
        assertEquals(moneyTransferTypesGiven.get(1).getId(), moneyTransferTypeResult2.getId());

    }

    @Order(3)
    @Test
    void findAll() {
        //WHEN
        List<MoneyTransferType> moneyTransferTypesResult = moneyTransferTypeDalService.findAll();

        //THEN
        assertNotNull(moneyTransferTypesResult);
        assertEquals(moneyTransferTypesGiven.size(), moneyTransferTypesResult.size());
        assertEquals(moneyTransferTypesGiven.get(0).getId(),
                moneyTransferTypesResult.get(0).getId());
        assertEquals(moneyTransferTypesGiven.get(1).getId(),
                moneyTransferTypesResult.get(1).getId());
    }

    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        MoneyTransferType moneyTransferTypeCreatedResult = moneyTransferTypeDalService.create(moneyTransferTypeToCreate);
        assertEquals(moneyTransferTypeToCreate.getId(), moneyTransferTypeCreatedResult.getId());

        //WHEN
        MoneyTransferType moneyTransferTypeResult = moneyTransferTypeDalService.findOne(moneyTransferTypeCreatedResult.getId());

        //THEN
        assertNotNull(moneyTransferTypeResult, "moneyTransferTypeToCreate has not been created or can not be find");
        assertEquals(moneyTransferTypeCreatedResult.getId(), moneyTransferTypeResult.getId());
    }

    @Order(5)
    @Test
    void update() {
        //GIVEN
        MoneyTransferType moneyTransferTypeCreatedResult = moneyTransferTypeDalService.create(moneyTransferTypeToCreate2);
        assertNotNull(moneyTransferTypeCreatedResult.getId());
        assertEquals(moneyTransferTypeToCreate2.getId(), moneyTransferTypeCreatedResult.getId());
        assertNotEquals(false, moneyTransferTypeCreatedResult.isCredit());
        moneyTransferTypeCreatedResult.setCredit(false);

        //WHEN
        MoneyTransferType moneyTransferTypeUpdatedResult = moneyTransferTypeDalService.update(moneyTransferTypeCreatedResult);

        //THEN
        assertEquals(moneyTransferTypeCreatedResult.isCredit(), moneyTransferTypeUpdatedResult.isCredit());
        assertEquals(false, moneyTransferTypeCreatedResult.isCredit());
    }

    @Order(6)
    @Test
    void delete() {
        List<MoneyTransferType> moneyTransferTypeResult = moneyTransferTypeDalService.findAll();
        assertTrue(moneyTransferTypeResult.size() > 0);
        int moneyTransferTypeListSizeAtStart = moneyTransferTypeResult.size();
        MoneyTransferType moneyTransferTypeToRemove = moneyTransferTypeDalService.findOne(moneyTransferTypeToCreate2.getId());
        assertTrue(moneyTransferTypeResult.contains(moneyTransferTypeToRemove));//need model equals method

        //WHEN
        moneyTransferTypeDalService.delete(moneyTransferTypeToRemove.getId());

        //THEN
        List<MoneyTransferType> moneyTransferTypesResultAfter = moneyTransferTypeDalService.findAll();
        assertEquals(moneyTransferTypeListSizeAtStart-1, moneyTransferTypesResultAfter.size());
        assertFalse(moneyTransferTypesResultAfter.contains(moneyTransferTypeToRemove));
    }

    @Order(7)
    @Test
    void deleteAll() {
        List<MoneyTransferType> moneyTransferTypesResult = moneyTransferTypeDalService.findAll();
        assertTrue(moneyTransferTypesResult.size() > 0);

        //WHEN
        moneyTransferTypeDalService.deleteAll();

        //THEN
        List<MoneyTransferType> moneyTransferTypesResultAfter = moneyTransferTypeDalService.findAll();
        assertEquals(0, moneyTransferTypesResultAfter.size());
    }
}