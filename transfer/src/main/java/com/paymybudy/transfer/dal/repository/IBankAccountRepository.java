package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IBankAccountRepository extends JpaRepository<BankAccount, Long> {

}
