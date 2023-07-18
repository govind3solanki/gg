package com.shivak.employee.repository;

import com.shivak.employee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public Optional<User> findById(Integer userId);

    Optional<User> findByUsername(String name);

    public User findByEmail(String usernameOrEmail);
}
