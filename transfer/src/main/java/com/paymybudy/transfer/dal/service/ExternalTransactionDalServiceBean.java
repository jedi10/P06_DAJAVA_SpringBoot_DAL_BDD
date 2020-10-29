package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IExternalTransactionRepository;

import com.paymybudy.transfer.models.ExternalTransaction;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalTransactionDalServiceBean implements IExternalTransactionDalService {

    private IExternalTransactionRepository externalTransactionRepository;

    /**
     * <b>External Transaction Dal Service Constructor</b>
     * @param externalTransactionRepository repository internalTransaction
     */
    public ExternalTransactionDalServiceBean(IExternalTransactionRepository externalTransactionRepository) {
        super();
        this.externalTransactionRepository = externalTransactionRepository;
    }

    @Override
    public List<ExternalTransaction> findAll() {
        return externalTransactionRepository.findAll();
    }

    @Override
    public ExternalTransaction findOne(Long id) {
        ExternalTransaction result = null;
        Optional<ExternalTransaction> externalTransactionOptional = externalTransactionRepository.findById(id);
        if (externalTransactionOptional.isPresent()){
            result = externalTransactionOptional.get();
        }
        return result;
    }

    @Override
    public ExternalTransaction create(ExternalTransaction externalTransaction) {
        if (externalTransaction.getId() != null) {
            //cannot create with specified Id value
            return null;
        }
        return externalTransactionRepository.save(externalTransaction);
    }

    @Override
    public ExternalTransaction update(ExternalTransaction externalTransaction) {
        ExternalTransaction externalTransactionPersisted = findOne(externalTransaction.getId());
        if (externalTransactionPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return externalTransactionRepository.save(externalTransaction);
    }

    @Override
    public void delete(Long id) {
        ExternalTransaction externalTransactionPersisted = findOne(id);
        if (externalTransactionPersisted != null){
            externalTransactionRepository.delete(externalTransactionPersisted);
        }
    }

    @Override
    public void deleteAll() {
        externalTransactionRepository.deleteAll();
    }
}
