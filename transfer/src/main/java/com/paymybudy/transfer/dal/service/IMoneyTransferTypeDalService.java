package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.MoneyTransferType;
import com.paymybudy.transfer.models.MoneyTransferTypeKey;

import java.util.List;

public interface IMoneyTransferTypeDalService {

    List<MoneyTransferType> findAll();
    MoneyTransferType findOne(MoneyTransferTypeKey id);
    MoneyTransferType create(MoneyTransferType moneyTransferType);
    MoneyTransferType update(MoneyTransferType moneyTransferType);
    void delete(MoneyTransferTypeKey id);
    void deleteAll();
}
