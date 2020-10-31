package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IExternalTransactionRepository;
import com.paymybudy.transfer.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalTransactionDalServiceBeanIT {

    @Autowired
    private IExternalTransactionDalService externalTransactionDalService;
    @Autowired
    private IBankAccountDalService bankAccountDalServiceBean;
    @Autowired
    private IInternalCashAccountDalService internalCashAccountDalService;
    @Autowired
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
    private  ExternalTransaction extTransactionToCreate = new ExternalTransaction(
            "Paiement masque FFP2",
            55.99,
            EnumTransacStatus.FINISHED,
            bankAccountToCreate,
            intCashAccountToCreate);
    private  ExternalTransaction extTransactionToUpdate = new ExternalTransaction(
            "Paiement Tapis",
            150,
            EnumTransacStatus.FINISHED,
            bankAccountToUpdate,
            intCashAccountToUpdate);

    @BeforeAll
    void setUpAll() {
        // To deal with an External Transaction, we have to create and store
        // Bank Account
        bankAccountDalServiceBean.create(extTransactionsGiven.get(0).getAccountDebit());
        bankAccountDalServiceBean.create(extTransactionsGiven.get(1).getAccountDebit());
        bankAccountDalServiceBean.create(bankAccountToCreate);
        bankAccountDalServiceBean.create(bankAccountToUpdate);
        // Internal Account
        internalCashAccountDalService.create(extTransactionsGiven.get(0).getAccountCredit());
        internalCashAccountDalService.create(extTransactionsGiven.get(1).getAccountCredit());
        internalCashAccountDalService.create(intCashAccountToCreate);
        internalCashAccountDalService.create(intCashAccountToUpdate);

    }

    @BeforeEach
    void setUp() {
    }

    @AfterAll
    void tearDown() {
        bankAccountDalServiceBean.deleteAll();
        internalCashAccountDalService.deleteAll();
    }


    @Order(1)
    @Test
    void create() {
        assertNotNull(externalTransactionDalService,
                "You have forgotten to inject an instance of ExternalTransactionDalService");

        //WHEN
        ExternalTransaction externalTransactionResult1 = externalTransactionDalService.create(extTransactionsGiven.get(0));
        ExternalTransaction externalTransactionResult2 = externalTransactionDalService.create(extTransactionsGiven.get(1));

        //THEN
        assertEquals(extTransactionsGiven.get(0), externalTransactionResult1);
        assertNotNull(externalTransactionResult1.getId());
        assertEquals(extTransactionsGiven.get(1), externalTransactionResult2);
        assertNotNull( externalTransactionResult2.getId());
    }


    @Order(2)
    @Test
    void findAll() {
        assertNotNull(externalTransactionDalService);

        //WHEN
        List<ExternalTransaction> extTransactionsResult = externalTransactionDalService.findAll();

        //THEN
        assertNotNull(extTransactionsResult);
        assertEquals(extTransactionsGiven.size(), extTransactionsResult.size());
        assertEquals(extTransactionsGiven.get(0).getAmount(),
                extTransactionsResult.get(0).getAmount());
        assertEquals(extTransactionsGiven.get(1).getAmount(),
                extTransactionsResult.get(1).getAmount());
    }

    @Order(3)
    @Test
    void findOne() {
        //GIVEN
        ExternalTransaction extTransactionCreatedResult = externalTransactionDalService.create(extTransactionToCreate);
        assertEquals(extTransactionToCreate.getAmount(), extTransactionCreatedResult.getAmount());
        assertNotNull(extTransactionCreatedResult.getId());

        //WHEN
        ExternalTransaction extTransactionResult = externalTransactionDalService.findOne(extTransactionCreatedResult.getId());

        //THEN
        assertEquals(extTransactionCreatedResult.getAmount(), extTransactionResult.getAmount());
        assertEquals(extTransactionCreatedResult.getId(), extTransactionResult.getId());
    }

    @Order(4)
    @Test
    void update() {
        //GIVEN
        ExternalTransaction externalTransactionCreatedResult = externalTransactionDalService.create(extTransactionToUpdate);
        assertEquals(extTransactionToUpdate.getAmount(), externalTransactionCreatedResult.getAmount());
        assertNotNull(extTransactionToUpdate.getId());
        assertNotNull(externalTransactionCreatedResult.getId());
        externalTransactionCreatedResult.setAmount(externalTransactionCreatedResult.getAmount()+ 11.5);

        //WHEN
        ExternalTransaction extTransactionUpdatedResult = externalTransactionDalService.update(externalTransactionCreatedResult);

        //THEN
        assertEquals(externalTransactionCreatedResult.getAmount(), extTransactionUpdatedResult.getAmount());
    }

    @Order(5)
    @Test
    void delete() {
        List<ExternalTransaction> extTransactionsResult = externalTransactionDalService.findAll();
        assertTrue(extTransactionsResult.size() > 0);
        int extTransactionListSizeAtStart = extTransactionsResult.size();
        ExternalTransaction extTransactionToRemove = externalTransactionDalService.findOne(extTransactionToUpdate.getId());
        assertTrue(extTransactionsResult.contains(extTransactionToRemove));//need model equals method

        //WHEN
        externalTransactionDalService.delete(extTransactionToRemove.getId());

        //THEN
        List<ExternalTransaction> extTransactionsResultAfter = externalTransactionDalService.findAll();
        assertEquals(extTransactionListSizeAtStart-1, extTransactionsResultAfter.size());
        assertFalse(extTransactionsResultAfter.contains(extTransactionToRemove));
    }

    @Order(6)
    @Test
    void deleteAll() {
        List<ExternalTransaction> extTransactionsResult = externalTransactionDalService.findAll();
        assertTrue(extTransactionsResult.size() > 0);

        //WHEN
        externalTransactionDalService.deleteAll();

        //THEN
        List<ExternalTransaction> usersResultAfter = externalTransactionDalService.findAll();
        assertEquals(0, usersResultAfter.size());
    }
}