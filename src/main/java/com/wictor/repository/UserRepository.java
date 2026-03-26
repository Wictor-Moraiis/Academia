package com.wictor.repository;

import com.wictor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByEmail1(String email1);
    boolean existsByTel1(String tel1);


}