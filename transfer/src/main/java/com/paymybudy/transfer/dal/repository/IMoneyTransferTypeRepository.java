package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.MoneyTransferType;
import com.paymybudy.transfer.models.MoneyTransferTypeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMoneyTransferTypeRepository extends JpaRepository<MoneyTransferType, MoneyTransferTypeKey> {

}
