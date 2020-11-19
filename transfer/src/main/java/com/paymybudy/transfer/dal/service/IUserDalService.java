package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.models.User;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface IUserDalService extends UserDetailsService {

    Collection<User> findAll();
    User findOne(Long id);
    User findByEmail(String email);
    User create2(UserRegistrationDto userRegistrationDto);
    User create(User user);
    User update(User user);
    void delete(Long id);
    void deleteAll();
}
