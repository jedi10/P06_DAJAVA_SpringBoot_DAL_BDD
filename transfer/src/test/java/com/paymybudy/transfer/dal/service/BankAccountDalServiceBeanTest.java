package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IBankAccountRepository;
import com.paymybudy.transfer.models.BankAccount;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
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
class BankAccountDalServiceBeanTest {

    @Mock
    private IBankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountDalServiceBean bankAccountDalService;

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
        //WHEN
        //CONFIG Mock
        when(bankAccountRepository.findAll()).thenReturn(bankAccountsGiven);
        when(bankAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(bankAccountsGiven.get(0)));
        when(bankAccountRepository.save(any())).thenReturn(bankAccountToCreate);
        //For update operation
        bankAccountToUpdate.setId(251L);
        when(bankAccountRepository.save(bankAccountToUpdate)).thenReturn(bankAccountToUpdate);
        when(bankAccountRepository.findById(bankAccountToUpdate.getId())).thenReturn(Optional.of(bankAccountToUpdate));
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void serviceInstantiation() {
        assertNotNull(bankAccountDalService,
                "you have forgot to create an Instance bankAccountDalService and inject Mock Repository inside");
    }

    @Order(2)
    @Test
    void findAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).findAll();

        //WHEN
        List<BankAccount> bankAccountsResult = bankAccountDalService.findAll();

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).findAll();

        assertNotNull(bankAccountsResult);
        assertEquals(bankAccountsGiven.size(), bankAccountsResult.size());
        assertEquals(bankAccountsGiven.get(0), bankAccountsResult.get(0));
        assertEquals(bankAccountsGiven.get(1), bankAccountsResult.get(1));
    }

    @Order(3)
    @Test
    void findOne() {
        //GIVEN
        BankAccount bankAccountExpected = bankAccountsGiven.get(0);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).findById(Mockito.anyLong());

        //WHEN
        BankAccount bankAccountResult = bankAccountDalService.findOne(10L);
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).findById(Mockito.anyLong());

        //THEN
        assertEquals(bankAccountExpected.getName(), bankAccountResult.getName());
    }

    @Order(4)
    @Test
    void create() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).save(any());

        //WHEN
        BankAccount bankAccountResult = bankAccountDalService.create(bankAccountToCreate);

        //THEN
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).save(any());

        assertEquals(bankAccountToCreate, bankAccountResult);
    }

    @Order(5)
    @Test
    void update() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).findById(bankAccountToUpdate.getId());
        verify(bankAccountRepository, Mockito.never()).save(bankAccountToUpdate);

        //WHEN
        BankAccount bankAccountResult = bankAccountDalService.update(bankAccountToUpdate);
        //THEN
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).findById(bankAccountToUpdate.getId());
        verify(bankAccountRepository, Mockito.times(1)).save(bankAccountToUpdate);

        assertEquals(bankAccountToUpdate, bankAccountResult);
    }

    @Order(6)
    @Test
    void delete() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).findById(bankAccountToUpdate.getId());
        verify(bankAccountRepository, Mockito.never()).delete(bankAccountToUpdate);

        //WHEN
        bankAccountDalService.delete(bankAccountToUpdate.getId());
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).findById(bankAccountToUpdate.getId());
        verify(bankAccountRepository, Mockito.times(1)).delete(bankAccountToUpdate);
    }

    @Order(7)
    @Test
    void deleteAll() {
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start*************
        //***********************************************************
        verify(bankAccountRepository, Mockito.never()).deleteAll();

        //WHEN
        bankAccountDalService.deleteAll();
        //***********************************************************
        //*****************CHECK MOCK INVOCATION at end**************
        //***********************************************************
        verify(bankAccountRepository, Mockito.times(1)).deleteAll();
    }


}