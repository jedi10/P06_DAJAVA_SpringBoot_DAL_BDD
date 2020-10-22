package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.MoneyTransfertType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMoneyTransferTypeRepository extends JpaRepository<MoneyTransfertType, Long> {

}
