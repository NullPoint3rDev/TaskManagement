package com.practice.task_management.repository;


import com.practice.task_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    User getById(Integer id);

    // find all user instead of current user
    Iterable<User> findByEmailNot(String email);
}
