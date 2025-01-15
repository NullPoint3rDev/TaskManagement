package com.practice.task_management.controller;

import com.practice.task_management.model.Project;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
@CrossOrigin("*")
public class ProjectController {

    private final ProjectService projectService;

    private final ValidationErrorService validationErrorService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,
                                              BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);

        if (errorMap != null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @PostMapping("/{projectId}/{userEmail}")
    public ResponseEntity<?> assignWorker(@PathVariable String projectId,
                                          @PathVariable String userEmail, Principal principal) {
        Project project = projectService.assignWorker(projectId, userEmail, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping
    public Iterable<Project> getAllProject(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(@Valid @PathVariable String projectId,
                                           @RequestBody Project project, BindingResult result,
                                           Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);
        if (errorMap != null) return errorMap;

        Project updateProject = projectService.updateProjectById(projectId, project, principal.getName());
        return new ResponseEntity<Project>(updateProject, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project " + projectId + "was deleted", HttpStatus.OK);
    }

}
