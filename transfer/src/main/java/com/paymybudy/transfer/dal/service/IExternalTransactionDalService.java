package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.ExternalTransaction;

import java.util.List;

public interface IExternalTransactionDalService {

    List<ExternalTransaction> findAll();
    ExternalTransaction findOne(Long id);
    ExternalTransaction create(ExternalTransaction externalTransaction);
    ExternalTransaction update(ExternalTransaction externalTransaction);
    void delete(Long id);
    void deleteAll();
}
