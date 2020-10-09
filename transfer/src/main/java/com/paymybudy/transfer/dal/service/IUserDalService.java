package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.User;

import java.util.Collection;

public interface IUserDalService {

    Collection<User> findAll();
    User findOne(Long id);
    User create(User user);
    User update(User user);
    void delete(Long id);
}
