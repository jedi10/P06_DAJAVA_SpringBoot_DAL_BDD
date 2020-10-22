package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.AppAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppAccountRepository extends JpaRepository<AppAccount, Long> {

}
