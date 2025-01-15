package com.practice.task_management.controller;

import com.practice.task_management.dto.TaskDto;
import com.practice.task_management.model.User;
import com.practice.task_management.service.TaskService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public Iterable<User> getAllUser(Principal principal) {
        return userService.getUsers(principal.getName());
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getSingleUser(@PathVariable String email) {
        User user = userService.findSingleUser(email);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
}
