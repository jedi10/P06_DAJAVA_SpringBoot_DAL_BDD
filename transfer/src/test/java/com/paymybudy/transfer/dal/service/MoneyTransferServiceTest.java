package com.paymybudy.transfer.dal.service;


import com.paymybudy.transfer.exception.IntMoneyTransferExecutionException;
import com.paymybudy.transfer.exception.IntMoneyTransferPreparationException;
import com.paymybudy.transfer.models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTransferServiceTest {

    @Mock
    private IInternalCashAccountDalService internalCashAccountDalService;
    @Mock
    private IInternalTransactionDalService internalTransactionDalService;
    @Mock
    private IMoneyTransferTypeDalService moneyTransferTypeDalService;

    @InjectMocks
    private MoneyTransferService moneyTransferService;

    private User fromUser = new User("Jack", "Holster", "holster@paymybuddy.com", "xxxx");
    private User toUser = new User("Tobias", "Hamsterdil", "hamsterdil@paymybuddy.com", "xxxx");
    private InternalTransaction internalTransactionError = new InternalTransaction("Error",
            0);
    private InternalTransaction internalTransaction = new InternalTransaction("Paiement service livraison",
            1500);
    private InternalCashAccount intCashAccountToCreate = new InternalCashAccount("XX23453-007", "Compte de dépot Mr Jack" );
    private InternalCashAccount intCashAccountToCreate2 = new InternalCashAccount("XX23453-002", "Compte de dépot Mr Tobias" );
    private MoneyTransferType moneyTransferTypeError =
            new MoneyTransferType(new MoneyTransferTypeKey(1L, 1L),
                    intCashAccountToCreate, internalTransactionError, false);
    private MoneyTransferType moneyTransferTypeToCreate =
            new MoneyTransferType(new MoneyTransferTypeKey(2L, 2L),
                    intCashAccountToCreate2, internalTransaction, true);


    @BeforeEach
    void setUp() {
        fromUser.setId(1L);
        toUser.setId(2L);
        fromUser.setInternalCashAccount(intCashAccountToCreate);
        toUser.setInternalCashAccount(intCashAccountToCreate2);
        internalTransaction.setId(1L);
        //Config Mock
        when(internalTransactionDalService.create(internalTransactionError)).thenReturn(null);
        when(internalTransactionDalService.create(internalTransaction)).thenReturn(internalTransaction);
        when(internalTransactionDalService.update(any())).thenReturn(internalTransaction);

        when(moneyTransferTypeDalService.create(moneyTransferTypeToCreate)).thenReturn(moneyTransferTypeToCreate);
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void sendMoney1_transaction_creation_error() {
        //GIVEN
        assertEquals(EnumTransacStatus.INITIATED , internalTransactionError.getStatus());
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(internalTransactionDalService, never()).create(internalTransactionError);

        //WHEN-THEN
        Exception exception = assertThrows(IntMoneyTransferPreparationException.class, ()-> {
            moneyTransferService.sendMoney(fromUser, toUser, internalTransactionError);
        });
        assertTrue(exception.getMessage().contains(
                "internalTransaction can not be created:"));
        assertEquals(EnumTransacStatus.ABORTED , internalTransactionError.getStatus());
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalTransactionDalService, Mockito.times(1)).create(internalTransactionError);
    }

    @Order(2)
    @Test
    void sendMoney1_transaction_creation_success() {
        //GIVEN
        toUser.setInternalCashAccount(null);
        assertEquals(EnumTransacStatus.INITIATED , internalTransaction.getStatus());
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(internalTransactionDalService, never()).create(internalTransaction);

        //WHEN-THEN
        Exception exception = assertThrows(IntMoneyTransferPreparationException.class, ()-> {
            moneyTransferService.sendMoney(fromUser, toUser, internalTransaction);
        });
        assertTrue(exception.getMessage().contains(
                "Internal Cash Account not found for user id:"));
        assertEquals(EnumTransacStatus.ABORTED , internalTransaction.getStatus());
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalTransactionDalService, Mockito.times(1)).create(internalTransaction);

    }

    @Order(3)
    @Test
    void sendMoney2_moneyTransferType_creation_error() {
        //GIVEN
        //Mock Config
        when(moneyTransferTypeDalService.create(any(MoneyTransferType.class))).thenReturn(null);
        //Mock Injection
        moneyTransferService = new MoneyTransferService(
                internalCashAccountDalService,
                internalTransactionDalService,
                moneyTransferTypeDalService);
        moneyTransferService.internalTransaction = internalTransactionError;
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(moneyTransferTypeDalService, never()).create(any(MoneyTransferType.class));

        //WHEN-THEN
        Exception exception = assertThrows(IntMoneyTransferPreparationException.class, ()-> {
            moneyTransferService.sendMoneyPreparation(fromUser, toUser);
        });
        assertTrue(exception.getMessage().contains(
                "Money Transfer Type can not be created:"));
        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(moneyTransferTypeDalService, times(1)).create(any(MoneyTransferType.class));
    }

    @Order(4)
    @Test
    void sendMoney2_moneyTransferType_creation_success() throws Exception {
        //GIVEN
        fromUser.getInternalCashAccount().setAmount(500);
        toUser.getInternalCashAccount().setAmount(1000);
        //Mock Config
        when(moneyTransferTypeDalService.create(any(MoneyTransferType.class))).thenReturn(moneyTransferTypeToCreate);
        //Mock Injection
        moneyTransferService = new MoneyTransferService(
                internalCashAccountDalService,
                internalTransactionDalService,
                moneyTransferTypeDalService);
        moneyTransferService.internalTransaction = internalTransactionError;
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(moneyTransferTypeDalService, never()).create(any(MoneyTransferType.class));

        //WHEN
        moneyTransferService.sendMoneyPreparation(fromUser, toUser);

        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(moneyTransferTypeDalService, times(2)).create(any(MoneyTransferType.class));
    }

    @Order(5)
    @ParameterizedTest
    @CsvSource({"500, 1000, 1500"})
    void sendMoney3_cashAccount_update_AmountError(double debitAccount, double creditAccount,
                                             double transfer) {
        //GIVEN
        fromUser.getInternalCashAccount().setAmount(debitAccount);
        toUser.getInternalCashAccount().setAmount(creditAccount);
        internalTransaction.setAmount(transfer);
        //Mock Config
        when(internalCashAccountDalService.update(any(InternalCashAccount.class))).thenReturn(null);
        //Mock Injection
        moneyTransferService = new MoneyTransferService(
                internalCashAccountDalService,
                internalTransactionDalService,
                moneyTransferTypeDalService);
        moneyTransferService.internalTransaction = internalTransaction;
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(internalCashAccountDalService, never()).update(any(InternalCashAccount.class));

        //WHEN
        Exception exception = assertThrows(IntMoneyTransferExecutionException.class, ()-> {
            moneyTransferService.sendMoneyExecution(fromUser, toUser);
        });
        assertTrue(exception.getMessage().contains(
                "The balance of the account"));
        assertTrue(exception.getMessage().contains(
                "is not enough"));
        assertEquals(debitAccount, fromUser.getInternalCashAccount().getAmount());
        assertEquals(creditAccount, toUser.getInternalCashAccount().getAmount());

        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountDalService, never()).update(any(InternalCashAccount.class));
    }

    @Order(6)
    @ParameterizedTest
    @CsvSource({"1500, 1000, 1500","590.9, -500, 199.7"})
    void sendMoney3_cashAccount_update_errorDBB(double debitAccount, double creditAccount,
                                             double transfer) {
        //GIVEN
        fromUser.getInternalCashAccount().setAmount(debitAccount);
        toUser.getInternalCashAccount().setAmount(creditAccount);
        internalTransaction.setAmount(transfer);
        //Mock Config
        when(internalCashAccountDalService.update(any(InternalCashAccount.class))).thenReturn(null);
        //Mock Injection
        moneyTransferService = new MoneyTransferService(
                internalCashAccountDalService,
                internalTransactionDalService,
                moneyTransferTypeDalService);
        moneyTransferService.internalTransaction = internalTransaction;
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(internalCashAccountDalService, never()).update(any(InternalCashAccount.class));

        //WHEN-THEN
        Exception exception = assertThrows(IntMoneyTransferExecutionException.class, ()-> {
            moneyTransferService.sendMoneyExecution(fromUser, toUser);
        });
        assertTrue(exception.getMessage().contains(
                "Amount can't be updated for cash account id:"));
        assertEquals(debitAccount-transfer, fromUser.getInternalCashAccount().getAmount());
        assertEquals(creditAccount+transfer, toUser.getInternalCashAccount().getAmount());

        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountDalService, times(1)).update(any(InternalCashAccount.class));
    }


    @Order(7)
    @ParameterizedTest
    @CsvSource({"1500, 1000, 1500","590.9, -500, 199.7"})
    void sendMoney3_cashAccount_update_success(double debitAccount,
                                               double creditAccount,
                                              double transfer) throws Exception {
        //GIVEN
        fromUser.getInternalCashAccount().setAmount(debitAccount);
        toUser.getInternalCashAccount().setAmount(creditAccount);
        internalTransaction.setAmount(transfer);
        assertEquals(EnumTransacStatus.INITIATED , internalTransaction.getStatus());
        //Mock Config
        when(internalCashAccountDalService.update(any(InternalCashAccount.class))).thenReturn(intCashAccountToCreate);
        //Mock Injection
        moneyTransferService = new MoneyTransferService(
                internalCashAccountDalService,
                internalTransactionDalService,
                moneyTransferTypeDalService);
        moneyTransferService.internalTransaction = internalTransaction;
        //***********************************************************
        //****************CHECK MOCK INVOCATION at start***************
        //***********************************************************
        verify(internalCashAccountDalService, never()).update(any(InternalCashAccount.class));
        verify(internalTransactionDalService, never()).update(any(InternalTransaction.class));

        //WHEN
        moneyTransferService.sendMoneyExecution(fromUser, toUser);

        //THEN
        assertEquals(debitAccount-transfer, fromUser.getInternalCashAccount().getAmount());
        assertEquals(creditAccount+transfer, toUser.getInternalCashAccount().getAmount());
        assertEquals(EnumTransacStatus.FINISHED , internalTransaction.getStatus());

        //***********************************************************
        //****************CHECK MOCK INVOCATION at end***************
        //***********************************************************
        verify(internalCashAccountDalService, times(2)).update(any(InternalCashAccount.class));
        verify(internalTransactionDalService, times(1)).update(any(InternalTransaction.class));
    }
}