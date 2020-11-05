package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.InternalCashAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInternalCashAccountRepository extends JpaRepository<InternalCashAccount, Long> {

}
