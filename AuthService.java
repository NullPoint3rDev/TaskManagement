package com.practice.task_management.service;


import com.practice.task_management.dto.AuthRequest;
import com.practice.task_management.dto.AuthResponse;
import com.practice.task_management.dto.RegisterRequest;
import com.practice.task_management.exception.TaskNotFoundException;
import com.practice.task_management.model.Role;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.UserRepository;
import com.practice.task_management.security.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new TaskNotFoundException.UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new TaskNotFoundException("Invalid credentials");
        }
        String token = jwtTokenUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
