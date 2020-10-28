package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IBankAccountRepository;
import com.paymybudy.transfer.models.BankAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountDalServiceBean implements IBankAccountDalService {

    private IBankAccountRepository bankAccountRepository;

    /**
     * <b>Bank Account Dal Service Constructor</b>
     * @param bankAccountRepository repository bankAccount
     */
    public BankAccountDalServiceBean(IBankAccountRepository bankAccountRepository) {
        super();
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount findOne(Long id) {
        BankAccount result = null;
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(id);
        if (bankAccountOptional.isPresent()){
            result = bankAccountOptional.get();
        }
        return result;
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        if (bankAccount.getId() != null) {
            //cannot create with specified Id value
            return null;
        }
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        BankAccount bankAccountPersisted = findOne(bankAccount.getId());
        if (bankAccountPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void delete(Long id) {
        BankAccount bankAccountPersisted = findOne(id);
        if (bankAccountPersisted != null){
            bankAccountRepository.delete(bankAccountPersisted);
        }
    }

    @Override
    public void deleteAll() {
        bankAccountRepository.deleteAll();
    }
}
