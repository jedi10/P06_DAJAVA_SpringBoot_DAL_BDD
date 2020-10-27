package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.AppAccount;

import java.util.List;

public interface IAppAccountDalService  {

    List<AppAccount> findAll();
    AppAccount findOne(Long id);
    AppAccount create(AppAccount appAccount);
    AppAccount update(AppAccount appAccount);
    void delete(Long id);
    void deleteAll();
}
