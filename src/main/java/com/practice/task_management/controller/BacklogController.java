package com.practice.task_management.controller;


import com.practice.task_management.model.Task;
import com.practice.task_management.service.TaskService;
import com.practice.task_management.service.ValidationErrorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/backlog")
@CrossOrigin
public class BacklogController {
    private final TaskService taskService;
    private final ValidationErrorService validationErrorService;

    @Operation(summary = "Add a task to the backlog", description = "Adds a new task to the specified backlog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/{backlog_id}/{userEmail}")
    public ResponseEntity<?> addPTtoBacklog(
            @Valid @RequestBody Task task,
            BindingResult result,
            @PathVariable String backlog_id,
            @PathVariable String userEmail,
            Principal principal
    ) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);
        if (errorMap != null) return errorMap;

        Task task1 = taskService.addProjectTask(backlog_id, task, userEmail, principal.getName());

        return new ResponseEntity<Task>(task1, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all tasks from a backlog", description = "Retrieves all tasks for a specified backlog.")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved")
    @GetMapping("/{backlog_id}")
    public Iterable<Task> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {
        return taskService.findBacklogById(backlog_id, principal.getName());
    }

    @Operation(summary = "Get task details", description = "Retrieves details for a specific task.")
    @ApiResponse(responseCode = "200", description = "Task details retrieved")
    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
        Task projectTask = taskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<Task>(projectTask, HttpStatus.OK);
    }

    // find task by task sequence
    @GetMapping("/taskDetails/{pt_sequence}")
    public ResponseEntity<?> getTask(@PathVariable String pt_sequence, Principal principal) {
        Task projectTask = taskService.findTaskByTaskSequence(pt_sequence, principal.getName());

        return new ResponseEntity<Task>(projectTask, HttpStatus.OK);
    }

    // update project task
    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(
            @Valid @RequestBody Task task,
            BindingResult result,
            @PathVariable String backlog_id,
            @PathVariable String pt_id,
            Principal principal
    ) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);
        if (errorMap != null) return errorMap;
        Task updatedTask = taskService.updateByProjectSequence(task, backlog_id, pt_id, principal.getName());

        return new ResponseEntity<Task>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
        taskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<String>("Project Task '" + pt_id + "' was deleted successfully", HttpStatus.OK);
    }

    // find all task of a user
    @GetMapping("/all_task")
    public Iterable<Task> getAllTaskOfUser(Principal principal) {
        return taskService.findAllTaskByUser(principal.getName());
    }

    @Operation(summary = "Update task status", description = "Updates the status of a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PutMapping("/update-status/{task_id}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String task_id, @RequestBody Integer status, Principal principal) {
        System.out.println("Task Id" + status + " Status " + task_id);
        Task updateTask = taskService.updateStatus(task_id, status, principal.getName());
        return new ResponseEntity<Task>(updateTask, HttpStatus.OK);
    }

}
