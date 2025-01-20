package com.practice.task_management;

import com.practice.task_management.model.Role;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User(null, "John Doe", "john.doe@example.com", "password", Role.USER, null, null, new Date(), new Date());
        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByEmail("john.doe@example.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User(null, "John Doe", "john.doe@example.com", "password", Role.USER, null, null, new Date(), new Date());
        User user2 = new User(null, "Jane Doe", "jane.doe@example.com", "password", Role.USER, null, null, new Date(), new Date());
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        Iterable<User> users = userRepository.findAll();

        // Assert
        assertNotNull(users);
        assertEquals(2, ((Collection<?>) users).size());
    }
}
