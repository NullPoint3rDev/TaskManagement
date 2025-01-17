package com.practice.task_management.controller;


import com.practice.task_management.model.User;
import com.practice.task_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей, кроме текущего.")
    public Iterable<User> getAllUser(Principal principal) {
        return userService.getUsers(principal.getName());
    }

    @Operation(summary = "Получить одного пользователя", description = "Возвращает данные пользователя по email.")
    @GetMapping("/{email}")
    public ResponseEntity<?> getSingleUser(@PathVariable String email) {
        User user = userService.findSingleUser(email);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
