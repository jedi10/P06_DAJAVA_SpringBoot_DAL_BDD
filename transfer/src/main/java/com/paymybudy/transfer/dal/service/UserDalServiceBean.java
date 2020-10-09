package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.User;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
//@Transactional
public class UserDalServiceBean implements IUserDalService {

    private IUserRepository userRepository;

    //@Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDalServiceBean(IUserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        Iterable<User> usersIterable = userRepository.findAll();

        return StreamSupport.stream(usersIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public User findOne(@NonNull Long id) {
        User result = null;
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            result = userOptional.get();
        }
        return result;
    }

    @Override
    public User create(@NonNull User user) {
        if (user.getId() != null) {
            //cannot create user with specified Id value
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public User update(@NonNull User user) {
        User userPersisted = findOne(user.getId());
        if (userPersisted == null){
            //We can't update an object not persisted
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(@NonNull Long id) {
        User userPersisted = findOne(id);
        if (userPersisted != null){
            userRepository.delete(userPersisted);
        }
    }
}



//https://riptutorial.com/fr/spring-boot/example/21493/exemple-de-base-de-l-integration-spring-spring-et-spring-data-jpa
//https://stackoverflow.com/questions/3317381/what-is-the-difference-between-collection-and-list-in-java