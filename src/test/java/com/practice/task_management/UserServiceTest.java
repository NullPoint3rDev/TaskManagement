package com.practice.task_management;

import com.practice.task_management.model.Role;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.UserRepository;
import com.practice.task_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {
        // Arrange
        User user = new User(null, "John Doe", "john1.doe@example.com", "password", Role.USER, null, null, new Date(), new Date());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.register(user);

        // Assert
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    void testFindSingleUser() {
        // Arrange
        User user = new User(1, "John Doe", "john.doe@example.com", "password", Role.USER, null, null, new Date(), new Date());
        Mockito.when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);

        // Act
        User foundUser = userService.findSingleUser("john.doe@example.com");

        // Assert
        assertEquals("John Doe", foundUser.getName());
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }
}
