package com.practice.task_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProjectNotFoundExceptionResponse {
    private String projectNotFound;
}
