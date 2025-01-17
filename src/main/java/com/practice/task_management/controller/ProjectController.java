package com.practice.task_management.controller;


import com.practice.task_management.model.Project;
import com.practice.task_management.service.ProjectService;
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
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
@CrossOrigin("*")
public class ProjectController {

    private final ProjectService projectService;
    private final ValidationErrorService validationErrorService;


    @Operation(summary = "Create a new project", description = "Creates a new project and stores it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createNewProject(
            @Valid
            @RequestBody Project project,
            BindingResult result,
            Principal principal) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);
        if (errorMap != null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    // assign user in project
    @PostMapping("/{projectId}/{userEmail}")
    public ResponseEntity<?> assignDeveloper(
            @PathVariable String projectId,
            @PathVariable String userEmail,
            Principal principal
    ) {
        Project project = projectService.assignDeveloper(projectId, userEmail, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @Operation(summary = "Get a project by ID", description = "Retrieves a project by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    // find all project
    @GetMapping
    public Iterable<Project> getAllProject(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    // update project by id
    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(
            @Valid
            @PathVariable String projectId,
            @RequestBody Project project,
            BindingResult result,
            Principal principal
    ) {
        ResponseEntity<?> errorMap = validationErrorService.ValidationService(result);
        if (errorMap != null) return errorMap;
        Project updateProject = projectService.updateProjectById(projectId, project, principal.getName());
        return new ResponseEntity<Project>(updateProject, HttpStatus.OK);
    }

    // delete project by identifier
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project with Id " + projectId + " was deleted.", HttpStatus.OK);
    }
}
