package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.InternalCashAccount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InternalCashAccountDalServiceBeanIT {

    @Autowired
    private IInternalCashAccountDalService internalCashAccountDalService;

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
    }

    @AfterEach
    void tearDown() {
    }


    @Order(1)
    @Test
    void create() {
        //WHEN
        InternalCashAccount intCashAccountResult1 = internalCashAccountDalService.create(intCashAccountsGiven.get(0));
        InternalCashAccount intCashAccountResult2 = internalCashAccountDalService.create(intCashAccountsGiven.get(1));

        //THEN
        assertEquals(intCashAccountsGiven.get(0), intCashAccountResult1);
        assertNotNull(intCashAccountResult1.getId());
        assertEquals(intCashAccountsGiven.get(1), intCashAccountResult2);
        assertNotNull( intCashAccountResult2.getId());
    }

    @Order(2)
    @Test
    void create_UniqueAccountNumberConstraint() {
        //GIVEN
        InternalCashAccount intCashAccountClone = new InternalCashAccount(
                "XX23453-456",
                "Compte de dépot Mr Balkani" );


        //WHEN
        Exception exception = assertThrows(DataIntegrityViolationException.class, ()-> {
            internalCashAccountDalService.create(intCashAccountClone);
        });
        assertTrue(exception.getMessage().contains("could not execute statement"));
        assertTrue(exception.getMessage().contains("constraint"));
    }

    @Order(3)
    @Test
    void findAll() {
        assertNotNull(internalCashAccountDalService);

        //WHEN
        List<InternalCashAccount> intCashAccountResult = internalCashAccountDalService.findAll();

        //THEN
        assertNotNull(intCashAccountResult);
        assertEquals(intCashAccountsGiven.size(), intCashAccountResult.size());
        assertEquals(intCashAccountsGiven.get(0).getNumber(),
                intCashAccountResult.get(0).getNumber());
        assertEquals(intCashAccountsGiven.get(1).getNumber(),
                intCashAccountResult.get(1).getNumber());
    }

    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        InternalCashAccount intCashAccountCreatedResult = internalCashAccountDalService.create(intCashAccountToCreate);
        assertEquals(intCashAccountToCreate.getNumber(), intCashAccountCreatedResult.getNumber());
        assertNotNull(intCashAccountCreatedResult.getId());

        //WHEN
        InternalCashAccount intCashAccountResult = internalCashAccountDalService.findOne(intCashAccountCreatedResult.getId());

        //THEN
        assertNotNull(intCashAccountResult, "intCashAccountToCreate has not been created or can not be find");
        assertEquals(intCashAccountCreatedResult.getNumber(), intCashAccountResult.getNumber());
        assertEquals(intCashAccountCreatedResult.getId(), intCashAccountResult.getId());
    }

    @Order(5)
    @Test
    void update() {
        //GIVEN
        InternalCashAccount intCashAccountCreatedResult = internalCashAccountDalService.create(intCashAccountToUpdate);
        assertEquals(intCashAccountToUpdate.getNumber(), intCashAccountCreatedResult.getNumber());
        assertNotNull(intCashAccountCreatedResult.getId());
        double amountUpdated = 500;
        assertNotEquals(amountUpdated, intCashAccountCreatedResult.getAmount());
        intCashAccountCreatedResult.setAmount(amountUpdated);

        //WHEN
        InternalCashAccount intCashAccountUpdateResult = internalCashAccountDalService.update(intCashAccountToUpdate);

        //THEN
        assertEquals(intCashAccountCreatedResult.getNumber(), intCashAccountUpdateResult.getNumber());
        assertEquals(amountUpdated, intCashAccountUpdateResult.getAmount());
    }

    @Order(6)
    @Test
    void delete() {
        List<InternalCashAccount> intCashAccountResult = internalCashAccountDalService.findAll();
        assertTrue(intCashAccountResult.size() > 0);
        int intCashAccountListSizeAtStart = intCashAccountResult.size();
        InternalCashAccount intCashAccountToRemove = internalCashAccountDalService.findOne(intCashAccountToUpdate.getId());
        assertTrue(intCashAccountResult.contains(intCashAccountToRemove));

        //WHEN
        internalCashAccountDalService.delete(intCashAccountToRemove.getId());

        //THEN
        List<InternalCashAccount> intCashAccountsResultAfter = internalCashAccountDalService.findAll();
        assertEquals(intCashAccountListSizeAtStart-1, intCashAccountsResultAfter.size());
        assertFalse(intCashAccountsResultAfter.contains(intCashAccountToRemove));
    }

    @Order(7)
    @Test
    void deleteAll() {
        List<InternalCashAccount> intCashAccountsResult = internalCashAccountDalService.findAll();
        assertTrue(intCashAccountsResult.size() > 0);

        //WHEN
        internalCashAccountDalService.deleteAll();

        //THEN
        List<InternalCashAccount> intCashAccountsResultAfter = internalCashAccountDalService.findAll();
        assertEquals(0, intCashAccountsResultAfter.size());
    }
}