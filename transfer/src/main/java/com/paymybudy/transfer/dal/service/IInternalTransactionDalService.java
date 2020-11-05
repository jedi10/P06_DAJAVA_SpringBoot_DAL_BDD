package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.InternalTransaction;

import java.util.List;

public interface IInternalTransactionDalService {

    List<InternalTransaction> findAll();
    InternalTransaction findOne(Long id);
    InternalTransaction create(InternalTransaction internalTransaction);
    InternalTransaction update(InternalTransaction internalTransaction);
    void delete(Long id);
    void deleteAll();
}
