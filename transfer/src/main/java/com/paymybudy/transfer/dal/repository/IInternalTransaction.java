package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.InternalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInternalTransaction extends JpaRepository<InternalTransaction, Long> {

}
