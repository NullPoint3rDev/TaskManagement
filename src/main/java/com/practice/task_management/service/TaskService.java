package com.practice.task_management.service;


import com.practice.task_management.exception.ProjectNotFoundException;
import com.practice.task_management.exception.TaskNotFoundException;
import com.practice.task_management.model.Backlog;
import com.practice.task_management.model.Task;
import com.practice.task_management.model.User;
import com.practice.task_management.repository.BacklogRepository;
import com.practice.task_management.repository.ProjectRepository;
import com.practice.task_management.repository.TaskRepository;
import com.practice.task_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final BacklogRepository backlogRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final UserRepository userRepository;

    // add project task
    public Task addProjectTask(String projectIdentifier, Task projectTask, String userEmail, String username) {
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

        projectTask.setBacklog(backlog);

        // find user and assign task
        User user = userRepository.findByEmail(userEmail);
        projectTask.setUser(user);

        // set task assigner
        projectTask.setAssignBy(username);

        // update sequence
        Integer backLogSequence = backlog.getPTSequence();
        backLogSequence++;
        backlog.setPTSequence(backLogSequence);

        // add sequence to project task
        projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backLogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        // initial status when status is null
        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO_DO");
        }

        //initial priority when priority null // projectTask.getPriority() == 0 ||
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        return taskRepository.save(projectTask);
    }

    // get project backlog for all assigned developer
    public Iterable<Task> findBacklogById(String backlogId, String username) {
        // checking project leader
        projectService.findProjectByDeveloper(backlogId, username);
        return taskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    // find task by project sequence
    // only available for project leader
    public Task findPTByProjectSequence(String backlog_id, String pt_id, String username) {
        // make sure searching an existing backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        // make sure searching an existing task
        Task task = taskRepository.findByProjectSequence(pt_id);
        if (task == null) {
            throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found!");
        }

        // make sure that the backlog/project id int the path corresponds to right project
        if (!task.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Project Task '" + pt_id + "' does not exist in this project!");
        }

        return task;
    }

    // find task by task id & assigned user
    public Task findTaskByTaskSequence(String projectSequence, String username) {
        // find task by projectSequence
        Task task = taskRepository.findByProjectSequence(projectSequence);
        projectService.findProjectByDeveloper(task.getProjectIdentifier(), username);
        return task;
    }

    // update project task
    public Task updateByProjectSequence(Task updateTask, String backlog_id, String pt_id, String username) {
        Task task = findPTByProjectSequence(backlog_id, pt_id, username);
        task = updateTask;
        return taskRepository.save(task);
    }

    // delete task
    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
        Task projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        taskRepository.delete(projectTask);
    }

    // find all task of a user
    public Iterable<Task> findAllTaskByUser(String username) {
        return taskRepository.findByUserEmail(username);
    }

    // update task status
    public Task updateStatus(String task_id, Integer status, String username) {
        Task task = taskRepository.findByProjectSequence(task_id);

        if(!task.getUser().getEmail().equals(username)) {
            throw new TaskNotFoundException("Project Task '" + task_id + "' does not exist in your account!");
        }
        task.setStatus(status == 2 ? "PROGRESS" : "COMPLETED");
        return taskRepository.save(task);
    }
}
