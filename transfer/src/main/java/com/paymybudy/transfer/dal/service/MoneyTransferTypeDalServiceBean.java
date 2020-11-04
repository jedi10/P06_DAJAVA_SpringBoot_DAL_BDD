package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IMoneyTransferTypeRepository;
import com.paymybudy.transfer.models.MoneyTransferTypeKey;
import com.paymybudy.transfer.models.MoneyTransferType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoneyTransferTypeDalServiceBean implements IMoneyTransferTypeDalService {

    private IMoneyTransferTypeRepository moneyTransferTypeRepository;

    //@Autowired
    //private ApplicationContext applicationContext;

    /**
     * <b>Money Transfer Dal Service Constructor</b>
     * @param moneyTransferTypeRepository repository moneyTransfer
     */
    public MoneyTransferTypeDalServiceBean(IMoneyTransferTypeRepository moneyTransferTypeRepository) {
        super();
        this.moneyTransferTypeRepository = moneyTransferTypeRepository;
    }

    @Override
    public List<MoneyTransferType> findAll() {
        return moneyTransferTypeRepository.findAll();
    }

    @Override
    public MoneyTransferType findOne(@NonNull MoneyTransferTypeKey id) {
        MoneyTransferType result = null;
        Optional<MoneyTransferType> moneyTransferTypeOptional = moneyTransferTypeRepository.findById(id);
        if (moneyTransferTypeOptional.isPresent()){
            result = moneyTransferTypeOptional.get();
        }
        return result;
    }

    @Override
    public MoneyTransferType create(@NonNull MoneyTransferType moneyTransferType) {
        //if (moneyTransferType.getId() != null) {
            //cannot create with specified Id value
        //    return null;
        //}
        return moneyTransferTypeRepository.save(moneyTransferType);
    }

    @Override
    public MoneyTransferType update(@NonNull MoneyTransferType moneyTransferType) {
        //MoneyTransferTypeKey key = moneyTransferType.getId();
        //Long hashCode = (long)key.hashCode();
        MoneyTransferType moneyTransferTypePersisted = findOne(moneyTransferType.getId());
        if (moneyTransferTypePersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return moneyTransferTypeRepository.save(moneyTransferType);
    }

    @Override
    public void delete(@NonNull MoneyTransferTypeKey id) {
        MoneyTransferType moneyTransferTypePersisted = findOne(id);
        if (moneyTransferTypePersisted != null){
            moneyTransferTypeRepository.delete(moneyTransferTypePersisted);
        }
    }

    @Override
    public void deleteAll() {
        moneyTransferTypeRepository.deleteAll();
    }
}
