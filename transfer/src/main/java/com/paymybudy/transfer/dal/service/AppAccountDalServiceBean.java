package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IAppAccountRepository;
import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.AppAccount;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class AppAccountDalServiceBean implements IAppAccountDalService {

    private IAppAccountRepository appAccountRepository;
    @Autowired
    private IUserDalService userDalService;

    /**
     * <b>App Account Dal Service Constructor</b>
     * @param appAccountRepository repository appAccount
     */
    public AppAccountDalServiceBean(IAppAccountRepository appAccountRepository) {
        super();
        this.appAccountRepository = appAccountRepository;
    }

    @Override
    public List<AppAccount> findAll() {
        return appAccountRepository.findAll();
    }

    @Override
    public AppAccount findOne(@NonNull Long id) {
        AppAccount result = null;
        Optional<AppAccount> appAccountOptional = appAccountRepository.findById(id);
        if (appAccountOptional.isPresent()){
            result = appAccountOptional.get();
        }
        return result;
    }

    @Override
    public AppAccount create(@NonNull AppAccount appAccount) {
        if (appAccount.getId() != null) {
            //cannot create with specified Id value
            return null;
        }
        return appAccountRepository.save(appAccount);
    }

    @Override
    public AppAccount update(@NonNull AppAccount appAccount) {
        AppAccount appAccountPersisted = findOne(appAccount.getId());
        if (appAccountPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return appAccountRepository.save(appAccount);
    }

    @Override
    public void delete(@NonNull Long id) {
        AppAccount appAccountPersisted = findOne(id);
        if (appAccountPersisted != null && appAccountPersisted.getUser() != null){
            userDalService.removeOneUserFromAllFriendList(appAccountPersisted.getUser());
            appAccountRepository.delete(appAccountPersisted);
        }
    }

    @Override
    public void deleteAll() {
        appAccountRepository.deleteAll();
    }
}
