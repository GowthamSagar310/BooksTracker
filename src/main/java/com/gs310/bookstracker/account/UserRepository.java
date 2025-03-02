package com.gs310.bookstracker.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    // if we follow a specific naming convention, JPA automatically knows that this needs to query a certain column
    Optional<UserEntity> findByUsername(String userName);

    Optional<UserEntity> findByEmail(String email);

}
