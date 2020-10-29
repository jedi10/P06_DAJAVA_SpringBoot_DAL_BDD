package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.InternalCashAccount;

import java.util.List;

public interface IInternalCashAccountDalService {

    List<InternalCashAccount> findAll();
    InternalCashAccount findOne(Long id);
    InternalCashAccount create(InternalCashAccount internalCashAccount);
    InternalCashAccount update(InternalCashAccount internalCashAccount);
    void delete(Long id);
    void deleteAll();
}
