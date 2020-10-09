package com.paymybudy.transfer.dal.repository;

import com.paymybudy.transfer.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface IUserRepository extends CrudRepository<User, Long> {

}



//https://docs.spring.io/spring-data/data-jpa/docs/1.1.x/reference/html/#jpa.query-methods.query-creation
//https://openclassrooms.com/fr/courses/4668056-construisez-des-microservices/5123366-utilisez-jpa-pour-communiquer-avec-une-base-de-donnees