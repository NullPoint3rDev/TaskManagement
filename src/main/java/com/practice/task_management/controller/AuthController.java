package com.practice.task_management.controller;

import com.practice.task_management.dto.AuthRequest;
import com.practice.task_management.dto.AuthResponse;
import com.practice.task_management.dto.RegisterRequest;
import com.practice.task_management.model.User;
import com.practice.task_management.service.AuthService;
import com.practice.task_management.service.UserService;
import com.practice.task_management.service.ValidationErrorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final UserService userService;

    private final ValidationErrorService validationErrorService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);

        if (errorMap != null) return errorMap;

        User newUser = userService.register(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

}
