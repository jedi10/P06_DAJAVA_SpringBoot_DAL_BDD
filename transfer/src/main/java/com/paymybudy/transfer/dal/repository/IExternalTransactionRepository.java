package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.ExternalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExternalTransactionRepository extends JpaRepository<ExternalTransaction, Long> {

}
