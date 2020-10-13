package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.BankAccount;
import com.paymybudy.transfer.models.User;
import org.springframework.data.repository.CrudRepository;

public interface IBankAccountRepository extends CrudRepository<BankAccount, Long> {
    
}
