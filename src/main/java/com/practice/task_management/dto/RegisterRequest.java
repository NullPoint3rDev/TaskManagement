package com.practice.task_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "We need your name")
    private String name;

    @NotBlank(message = "We need an email")
    private String email;

    @NotBlank(message = "We need a password")
    private String
}
