package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IInternalTransactionRepository;
import com.paymybudy.transfer.models.InternalTransaction;

import java.util.List;
import java.util.Optional;

public class InternalTransactionDalServiceBean implements IInternalTransactionDalService {

    private IInternalTransactionRepository internalTransactionRepository;

    /**
     * <b>Internal Transaction Dal Service Constructor</b>
     * @param internalTransactionRepository repository internalTransaction
     */
    public InternalTransactionDalServiceBean(IInternalTransactionRepository internalTransactionRepository) {
        super();
        this.internalTransactionRepository = internalTransactionRepository;
    }

    @Override
    public List<InternalTransaction> findAll() {
        return internalTransactionRepository.findAll();
    }

    @Override
    public InternalTransaction findOne(Long id) {
        InternalTransaction result = null;
        Optional<InternalTransaction> internalTransactionOptional = internalTransactionRepository.findById(id);
        if (internalTransactionOptional.isPresent()){
            result = internalTransactionOptional.get();
        }
        return result;
    }

    @Override
    public InternalTransaction create(InternalTransaction internalTransaction) {
        if (internalTransaction.getId() != null) {
            //cannot create with specified Id value
            return null;
        }
        return internalTransactionRepository.save(internalTransaction);
    }

    @Override
    public InternalTransaction update(InternalTransaction internalTransaction) {
        InternalTransaction internalTransactionPersisted = findOne(internalTransaction.getId());
        if (internalTransactionPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return internalTransactionRepository.save(internalTransaction);
    }

    @Override
    public void delete(Long id) {
        InternalTransaction internalTransactionPersisted = findOne(id);
        if (internalTransactionPersisted != null){
            internalTransactionRepository.delete(internalTransactionPersisted);
        }
    }

    @Override
    public void deleteAll() {
        internalTransactionRepository.deleteAll();
    }
}
