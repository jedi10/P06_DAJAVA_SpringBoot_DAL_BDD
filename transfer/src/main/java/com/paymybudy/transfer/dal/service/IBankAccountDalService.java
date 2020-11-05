package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.BankAccount;

import java.util.List;

public interface IBankAccountDalService {
    List<BankAccount> findAll();
    BankAccount findOne(Long id);
    BankAccount create(BankAccount bankAccount);
    BankAccount update(BankAccount bankAccount);
    void delete(Long id);
    void deleteAll();
}
