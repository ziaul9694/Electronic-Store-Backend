package com.cwz.electronic.store.repositories;

import com.cwz.electronic.store.dtos.UserDto;
import com.cwz.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndName(String email, String name);
    List<User> findByNameContaining(String keyword);
}
