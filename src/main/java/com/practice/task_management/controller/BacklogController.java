package com.practice.task_management.controller;

import com.practice.task_management.model.Task;
import com.practice.task_management.service.TaskService;
import com.practice.task_management.service.ValidationErrorService;
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

    @PostMapping("/{backlog_id}/{userEmail}")
    public ResponseEntity<?> addSeqToBacklog(@Valid @RequestBody Task task, BindingResult result,
                                             @PathVariable String backlog_id, @PathVariable String userEmail, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);

        if (errorMap != null) return errorMap;

        Task task1 = taskService.addProjectTask(backlog_id, task, userEmail, principal.getName());

        return new ResponseEntity<Task>(task1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<Task> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {
        return taskService.findBacklogById(backlog_id, principal.getName());

    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id,
                                            @PathVariable String pt_id, Principal principal) {

        Task projectTask = taskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<Task>(projectTask, HttpStatus.OK);
    }

    @GetMapping("/taskDetails/{pt_sequence}")
    public ResponseEntity<?> getTask(@PathVariable String pt_sequence, Principal principal) {
        Task projectTask = taskService.findTaskBySequence(pt_sequence, principal.getName());

        return new ResponseEntity<Task>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid
                                               @RequestBody Task task, BindingResult result,
                                               @PathVariable String backlog_id,
                                               @PathVariable String pt_id, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);

        if (errorMap != null) return errorMap;

        Task updatedTask = taskService.updateByProjectSequence(task, backlog_id, pt_id, principal.getName());

        return new ResponseEntity<Task>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id,
                                               @PathVariable String pt_id, Principal principal) {
        taskService.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<String>("Task ' " + pt_id + "' was deleted", HttpStatus.OK);
    }

    @GetMapping("/all_task")
    public Iterable<Task> getAllUserTask(Principal principal) {
        return taskService.findAllTaskByUser(principal.getName());
    }

    @PutMapping("/update-status/{task_id}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String task_id,
                                              @RequestBody Integer status, Principal principal) {
        System.out.println("Task ID " + status + " Status " + task_id);
        Task updateTask = taskService.updateStatus(task_id, status, principal.getName());

        return new ResponseEntity<Task>(updateTask, HttpStatus.OK);
    }
}
