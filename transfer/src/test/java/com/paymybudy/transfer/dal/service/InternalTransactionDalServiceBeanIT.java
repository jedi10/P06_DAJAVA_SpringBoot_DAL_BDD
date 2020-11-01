package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.InternalTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InternalTransactionDalServiceBeanIT {

    @Autowired
    private IInternalTransactionDalService internalTransactionDalService;

    private static List<InternalTransaction> intTransactionsGiven = new ArrayList<>();

    static {
        InternalTransaction intTransaction1 = new InternalTransaction("Paiement repassage",300);
        InternalTransaction intTransaction2 = new InternalTransaction("Cadeau Anniversaire",500);
        intTransactionsGiven.add(intTransaction1);
        intTransactionsGiven.add(intTransaction2);
    }

    private  InternalTransaction intTransactionToCreate = new InternalTransaction("Remboursement emprunt", 990);
    private  InternalTransaction intTransactionToUpdate = new InternalTransaction("Paiement garde animaux", 150);

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void serviceDeclarationInstantiation() {
        assertNotNull(internalTransactionDalService,
                "you have forgot to declare internalTransactionDalService as a SpringBoot service or to autowire it");
    }

    @Order(2)
    @Test
    void create() {
        //WHEN
        InternalTransaction internalTransactionResult1 = internalTransactionDalService.create(intTransactionsGiven.get(0));
        InternalTransaction internalTransactionResult2 = internalTransactionDalService.create(intTransactionsGiven.get(1));

        //THEN
        assertEquals(intTransactionsGiven.get(0), internalTransactionResult1);
        assertNotNull(internalTransactionResult1.getId());
        assertEquals(intTransactionsGiven.get(1), internalTransactionResult2);
        assertNotNull( internalTransactionResult2.getId());
    }

    @Order(3)
    @Test
    void findAll() {
        //WHEN
        List<InternalTransaction> intTransactionsResult = internalTransactionDalService.findAll();

        //THEN
        assertNotNull(intTransactionsResult);
        assertEquals(intTransactionsGiven.size(), intTransactionsResult.size());
        assertEquals(intTransactionsGiven.get(0).getAmount(),
                intTransactionsResult.get(0).getAmount());
        assertEquals(intTransactionsGiven.get(1).getAmount(),
                intTransactionsResult.get(1).getAmount());
    }

    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        InternalTransaction extTransactionCreatedResult = internalTransactionDalService.create(intTransactionToCreate);
        assertEquals(intTransactionToCreate.getAmount(), extTransactionCreatedResult.getAmount());
        assertNotNull(extTransactionCreatedResult.getId());

        //WHEN
        InternalTransaction intTransactionResult = internalTransactionDalService.findOne(extTransactionCreatedResult.getId());

        //THEN
        assertNotNull(intTransactionResult, "intTransactionToCreate has not been created or can not be find");
        assertEquals(extTransactionCreatedResult.getAmount(), intTransactionResult.getAmount());
        assertEquals(extTransactionCreatedResult.getId(), intTransactionResult.getId());
    }

    @Order(5)
    @Test
    void update() {
        //GIVEN
        InternalTransaction internalTransactionCreatedResult = internalTransactionDalService.create(intTransactionToUpdate);
        assertEquals(intTransactionToUpdate.getAmount(), internalTransactionCreatedResult.getAmount());
        assertNotNull(intTransactionToUpdate.getId());
        assertNotNull(internalTransactionCreatedResult.getId());
        internalTransactionCreatedResult.setAmount(internalTransactionCreatedResult.getAmount()+ 11.5);

        //WHEN
        InternalTransaction intTransactionUpdatedResult = internalTransactionDalService.update(internalTransactionCreatedResult);

        //THEN
        assertEquals(internalTransactionCreatedResult.getAmount(), intTransactionUpdatedResult.getAmount());
    }

    @Order(6)
    @Test
    void delete() {
        List<InternalTransaction> intTransactionsResult = internalTransactionDalService.findAll();
        assertTrue(intTransactionsResult.size() > 0);
        int intTransactionListSizeAtStart = intTransactionsResult.size();
        InternalTransaction intTransactionToRemove = internalTransactionDalService.findOne(intTransactionToUpdate.getId());
        assertTrue(intTransactionsResult.contains(intTransactionToRemove));//need model equals method

        //WHEN
        internalTransactionDalService.delete(intTransactionToRemove.getId());

        //THEN
        List<InternalTransaction> intTransactionsResultAfter = internalTransactionDalService.findAll();
        assertEquals(intTransactionListSizeAtStart-1, intTransactionsResultAfter.size());
        assertFalse(intTransactionsResultAfter.contains(intTransactionToRemove));
    }

    @Order(7)
    @Test
    void deleteAll() {
        List<InternalTransaction> intTransactionsResult = internalTransactionDalService.findAll();
        assertTrue(intTransactionsResult.size() > 0);

        //WHEN
        internalTransactionDalService.deleteAll();

        //THEN
        List<InternalTransaction> intTransactionsResultAfter = internalTransactionDalService.findAll();
        assertEquals(0, intTransactionsResultAfter.size());
    }
}