package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.BankAccount;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountDalServiceBeanIT {

    @Autowired
    private IBankAccountDalService bankAccountDalService;

    private static List<BankAccount> bankAccountsGiven = new ArrayList<>();

    static {
        BankAccount bankAccount1 = new BankAccount("Banque Unibey","0100345678", "63 Rue de Londre 75000 Paris");
        BankAccount bankAccount2 = new BankAccount("Banque UBC","0145678676", "10 Rue des Homard 75000 Paris");
        bankAccountsGiven.add(bankAccount1);
        bankAccountsGiven.add(bankAccount2);
    }

    private BankAccount bankAccountToCreate = new BankAccount("Banque Passilex","012345678", "1 Rue des morts 75000 Paris");

    private BankAccount bankAccountToUpdate = new BankAccount("Banque Optimex","0987654232", "3 Place Vendome 75000 Paris");



    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void serviceDeclarationInstantiation() {
        assertNotNull(bankAccountDalService,
                "you have forgot to declare bankAccountDalService as a SpringBoot service or to autowire it");
    }

    @Order(2)
    @Test
    void create() {
        //GIVEN
        assertNotNull(bankAccountsGiven);
        assertNotNull(bankAccountsGiven.get(0));
        assertNotNull(bankAccountsGiven.get(1));
        //WHEN
        BankAccount bankAccountResult1 = bankAccountDalService.create(bankAccountsGiven.get(0));
        BankAccount bankAccountResult2 = bankAccountDalService.create(bankAccountsGiven.get(1));

        //THEN
        assertEquals(bankAccountsGiven.get(0), bankAccountResult1);
        assertNotNull(bankAccountResult1.getId());
        assertEquals(bankAccountsGiven.get(1), bankAccountResult2);
        assertNotNull(bankAccountResult2.getId());
    }

    @Order(3)
    @Test
    void findAll() {

        //WHEN
        List<BankAccount> bankAccountsResult = bankAccountDalService.findAll();

        //THEN
        assertNotNull(bankAccountsResult);
        assertEquals(bankAccountsGiven.size(), bankAccountsResult.size());
        assertEquals(bankAccountsGiven.get(0).getName(),
                bankAccountsResult.get(0).getName());
        assertEquals(bankAccountsGiven.get(1).getName(),
                bankAccountsResult.get(1).getName());
    }


    @Order(4)
    @Test
    void findOne() {
        //GIVEN
        BankAccount bankAccountCreatedResult = bankAccountDalService.create(bankAccountToCreate);
        assertEquals(bankAccountToCreate.getName(), bankAccountCreatedResult.getName());
        assertNotNull(bankAccountCreatedResult.getId());

        //WHEN
        BankAccount bankAccountResult = bankAccountDalService.findOne(bankAccountCreatedResult.getId());

        //THEN
        assertNotNull(bankAccountResult, "bankAccountToCreate has not been created or can not be find");
        assertEquals(bankAccountCreatedResult.getName(), bankAccountResult.getName());
        assertEquals(bankAccountCreatedResult.getId(), bankAccountResult.getId());
    }

    @Order(5)
    @Test
    void update() {
        //GIVEN
        //Bank Account Creation
        BankAccount bankAccountCreatedResult =  bankAccountDalService.create(bankAccountToUpdate);
        assertEquals(bankAccountToUpdate.getName(), bankAccountCreatedResult.getName());
        assertNotNull(bankAccountCreatedResult.getId());
        //Change Bank Account Status but no Update
        String nameSuffix = " & Arlex Company";
        bankAccountCreatedResult.setName(bankAccountCreatedResult.getName()+ nameSuffix);

        //WHEN Update
        BankAccount bankAccountUpdateResult = bankAccountDalService.update(bankAccountCreatedResult);

        //THEN
        assertTrue(bankAccountUpdateResult.getName().contains(nameSuffix));
    }

    @Order(6)
    @Test
    void delete() {
        List<BankAccount> bankAccountsResult = bankAccountDalService.findAll();
        assertTrue(bankAccountsResult.size() > 0);
        int bankAccountListSizeAtStart = bankAccountsResult.size();
        BankAccount bankAccountToRemove = bankAccountsResult.get(0);
        assertTrue(bankAccountsResult.contains(bankAccountToRemove));
        assertNotNull(bankAccountToRemove.getId());

        //WHEN
        bankAccountDalService.delete(bankAccountToRemove.getId());

        //THEN
        List<BankAccount> bankAccountsResultAfter = bankAccountDalService.findAll();
        assertEquals(bankAccountListSizeAtStart-1, bankAccountsResultAfter.size());
        assertFalse(bankAccountsResultAfter.contains(bankAccountToRemove));
    }

    @Order(7)
    @Test
    void deleteAll() {
        List<BankAccount> bankAccountsResult = bankAccountDalService.findAll();
        assertTrue(bankAccountsResult.size() > 0);

        //WHEN
        bankAccountDalService.deleteAll();

        //THEN
        List<BankAccount> bankAccountsResultAfter = bankAccountDalService.findAll();
        assertEquals(0, bankAccountsResultAfter.size());
    }

}