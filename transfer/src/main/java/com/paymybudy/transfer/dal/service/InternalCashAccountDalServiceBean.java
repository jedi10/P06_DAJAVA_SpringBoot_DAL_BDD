package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IInternalCashAccountRepository;
import com.paymybudy.transfer.models.InternalCashAccount;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternalCashAccountDalServiceBean implements IInternalCashAccountDalService {

    private IInternalCashAccountRepository internalCashAccountRepository;

    /**
     * <b>Internal Cash Account Dal Service Constructor</b>
     * @param internalCashAccountRepository repository internalCashAccount
     */
    public InternalCashAccountDalServiceBean(IInternalCashAccountRepository internalCashAccountRepository) {
        super();
        this.internalCashAccountRepository = internalCashAccountRepository;
    }

    @Override
    public List<InternalCashAccount> findAll() {
        return internalCashAccountRepository.findAll();
    }

    @Override
    public InternalCashAccount findOne(@NonNull Long id) {
        InternalCashAccount result = null;
        Optional<InternalCashAccount> internalCashAccountOptional = internalCashAccountRepository.findById(id);
        if (internalCashAccountOptional.isPresent()){
            result = internalCashAccountOptional.get();
        }
        return result;
    }

    @Override
    public InternalCashAccount create(@NonNull InternalCashAccount internalCashAccount) {
        //if (internalCashAccount.getId() != null) {
            //cannot create with specified Id value
        //    return null;
        //}
        return internalCashAccountRepository.save(internalCashAccount);
    }

    @Override
    public InternalCashAccount update(@NonNull InternalCashAccount internalCashAccount) {
        InternalCashAccount intCashAccountPersisted = findOne(internalCashAccount.getId());
        if (intCashAccountPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return internalCashAccountRepository.save(internalCashAccount);
    }

    @Override
    public void delete(@NonNull Long id) {
        InternalCashAccount intCashAccountPersisted = findOne(id);
        if (intCashAccountPersisted != null){
            internalCashAccountRepository.delete(intCashAccountPersisted);
        }
    }

    @Override
    public void deleteAll() {
        internalCashAccountRepository.deleteAll();
    }
}
