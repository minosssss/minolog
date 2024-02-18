package com.minolog.api.repository;

import com.minolog.api.domain.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {

    @EntityGraph(attributePaths = {"sessions"})
    Optional<Users> findByEmailAndPassword(String email, String password);
}
