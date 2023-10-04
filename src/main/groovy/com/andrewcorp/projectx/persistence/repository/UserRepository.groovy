package com.andrewcorp.projectx.persistence.repository

import com.andrewcorp.projectx.persistence.model.User;
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username)
    boolean existsByUsername(String username)
}