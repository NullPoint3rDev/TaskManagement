package com.practice.task_management.service;

import com.practice.task_management.exception.ProjectIdException;
import com.practice.task_management.exception.ProjectNotFoundException;
import com.practice.task_management.model.Backlog;
import com.practice.task_management.model.Project;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.BacklogRepository;
import com.practice.task_management.repository.ProjectRepository;
import com.practice.task_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final BacklogRepository backlogRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject != null && (!existingProject.getProjectLead().equals(username))) {
                throw new ProjectNotFoundException("Project not found");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project " + project.getProjectIdentifier() + " can't be updated because you don't have it");
            }
        }

        try {
            User user = userRepository.findByEmail(username);
            project.getUsers().add(user);
            project.setProjectLead(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null) {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            user.getProjects().add(project);
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project " + project.getProjectIdentifier().toUpperCase() + " already taken");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project " + projectId + " not found");
        }

        if (!project.getProjectLead().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Project findProjectByWorker(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId);

        if (project == null) {
            throw new ProjectIdException("Project " + projectId + " does not exist");
        }

        project.getUsers().stream().filter(user -> user.getEmail().equals(username))
                .findAny()
                .orElseThrow(() -> new ProjectNotFoundException("Project not found in your account"));

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLead(username);
    }

    public Project assignWorker(String projectId, String userEmail, String username) {
        Project project = findProjectByIdentifier(projectId, username);

        User user = userRepository.findByEmail(userEmail);

        project.getUsers().add(user);
        user.getProjects().add(project);

        return projectRepository.save(project);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

    public Project updateProjectById(String projectId, String username, Project updateProject) {
        Project project = findProjectByIdentifier(projectId, username);
        project.setProjectName(updateProject.getProjectName());
        project.setProjectDescription(updateProject.getProjectDescription());
        project.setStartDate(updateProject.getStartDate());
        project.setEndDate(updateProject.getEndDate());

        return projectRepository.save(project);
    }
}
