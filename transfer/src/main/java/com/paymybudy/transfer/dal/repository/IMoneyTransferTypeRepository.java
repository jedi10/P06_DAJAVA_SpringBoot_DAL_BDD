package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.MoneyTransferType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMoneyTransferTypeRepository extends JpaRepository<MoneyTransferType, Long> {

}
