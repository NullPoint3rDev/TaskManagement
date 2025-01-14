package com.practice.task_management.controller;

import com.practice.task_management.dto.TaskDto;
import com.practice.task_management.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Tasks")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation(value = "Create a new task", response = TaskDto.class)
    @ApiResponse(code = 201, message = "Task created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto, "user@example.com");
    }

    @ApiOperation(value = "Get all tasks", response = TaskDto.class, responseContainer = "List")
    @ApiResponse(code = 200, message = "Successfully retrieved tasks")
    @GetMapping
    public List<TaskDto> getTasks(Pageable pageable) {
        return taskService.getTasks("user@example.com");
    }

    @ApiOperation(value = "Get task by ID", response = TaskDto.class)
    @ApiResponse(code = 200, message = "Successfully retrieved task")
    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @ApiOperation(value = "Update task by ID", response = TaskDto.class)
    @ApiResponse(code = 200, message = "Task updated successfully")
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @ApiOperation(value = "Delete task by ID")
    @ApiResponse(code = 204, message = "Task deleted successfully")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
