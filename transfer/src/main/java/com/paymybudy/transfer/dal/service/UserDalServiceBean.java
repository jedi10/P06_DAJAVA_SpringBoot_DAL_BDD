package com.paymybudy.transfer.dal.service;

import com.paymybudy.transfer.dal.repository.IUserRepository;
import com.paymybudy.transfer.models.User;
import com.paymybudy.transfer.web.dto.UserRegistrationDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
//@Transactional
public class UserDalServiceBean implements IUserDalService {

    private IUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

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

    /**
     * <b>Spring Security Method to log-in</b>
     * @param email email is used to log in
     * @return UserDetails
     * @throws UsernameNotFoundException spring security exception
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(),
                user.get().getPassword(),
                List.of(new SimpleGrantedAuthority("role1")));
    }

    @Override
    public User findByEmail(@NonNull String email) {
        User result = null;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            result = userOptional.get();
        }
        return result;
    }

    /**
     * <b>Spring Security Method for user registration</b>
     * @param userRegistrationDto user Data Transfer Object
     * @return User user
     */
    @Override
    public User create2(@NonNull UserRegistrationDto userRegistrationDto) {
        User user = new User(userRegistrationDto.getFirstName(),
                userRegistrationDto.getLastName(), userRegistrationDto.getEmail(),
                passwordEncoder.encode(userRegistrationDto.getPassword()));
        return userRepository.save(user);
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
            this.removeOneUserFromAllFriendList(userPersisted);
            userRepository.delete(userPersisted);
        }
    }

    /**
     * <b>Remove one user from all Friends List</b>
     * @param userToRemove user
     */
    public void removeOneUserFromAllFriendList(User userToRemove){
        List<User> userList = findAll();
        //User user1 = userList.get(0);
        //user1.getContactList().remove(userToRemove);
        //this.update(user1);
        for (User u : userList){
            List<User> contactList = u.getContactList();
            boolean isPresent = contactList.remove(userToRemove);
            if (isPresent) this.update(u);
        }
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }



}


//https://www.javaguides.net/2020/06/spring-security-tutorial-with-spring-boot-spring-data-jpa-thymeleaf-and-mysql-database.html
//https://riptutorial.com/fr/spring-boot/example/21493/exemple-de-base-de-l-integration-spring-spring-et-spring-data-jpa
//https://stackoverflow.com/questions/3317381/what-is-the-difference-between-collection-and-list-in-java