package com.practice.task_management.repository;

import com.practice.task_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User getById(Integer id);

    Iterable<User> findByEmailNot(String email);
}
