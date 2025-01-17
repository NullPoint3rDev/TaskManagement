package com.practice.task_management.service;


import com.practice.task_management.dto.AuthenticationRequest;
import com.practice.task_management.dto.AuthenticationResponse;
import com.practice.task_management.exception.EmailAlreadyExistsException;
import com.practice.task_management.model.Role;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // register new user
    public User register(User user) {
        try {
            var newUser = User.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(Role.USER)
                    .build();
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' already exists");
        }
    }

    // login
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // get all users
    public Iterable<User> getUsers(String username) {
        return userRepository.findByEmailNot(username);
    }

    // find single user
    public User findSingleUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
