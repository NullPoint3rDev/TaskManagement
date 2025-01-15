package com.practice.task_management.service;

//import com.practice.task_management.dto.TaskDto;
import com.practice.task_management.exception.ProjectNotFoundException;
import com.practice.task_management.exception.TaskNotFoundException;
import com.practice.task_management.model.*;
import com.practice.task_management.repository.BacklogRepository;
import com.practice.task_management.repository.ProjectRepository;
import com.practice.task_management.repository.TaskRepository;
import com.practice.task_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BacklogRepository backlogRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public Task addProjectTask(String projectIdentifier, Task projectTask, String userEmail, String username) {
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

        projectTask.setBacklog(backlog);

        User user = userRepository.findByEmail(userEmail);
        projectTask.setUser(user);

        projectTask.setAssignBy(username);

        Integer backLogSequence = backlog.getSequence();
        backLogSequence++;
        backlog.setSequence(backLogSequence);

        projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backLogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
            projectTask.setStatus("TO DO");
        }

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        return taskRepository.save(projectTask);
    }

    public Iterable<Task> findBacklogById(String backlogId, String username) {
        projectService.findProjectByWorker(backlogId, username);
        return taskRepository.findByProjectIdentifier(backlogId);
    }

    public Task findPTByProjectSequence(String backlog_id, String pt_id, String username) {
        projectService.findProjectByIdentifier(backlog_id, username);

        Task task = taskRepository.findByProjectSequence(pt_id);
        if (task == null) {
            throw new ProjectNotFoundException("Task " + pt_id + "was not found");
        }

        if (!task.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException("Task " + pt_id + "does not exist");
        }
        return task;
    }

    public Task findTaskBySequence(String projectSequence, String username) {
        Task task = taskRepository.findByProjectSequence(projectSequence);
        projectService.findProjectByWorker(task.getProjectIdentifier(), username);
        return task;
    }

    public Task updateByProjectSequence(Task updateTask, String backlog_id, String pt_id, String username) {
        Task task = findPTByProjectSequence(backlog_id, pt_id, username);
        task = updateTask;
        return taskRepository.save(task);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
        Task projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        taskRepository.delete(projectTask);
    }

    public Iterable<Task> findAllTaskByUser(String username) {
        return taskRepository.findByUserEmail(username);
    }

    public Task updateStatus(String task_id, Integer status, String username) {
        Task task = taskRepository.findByProjectSequence(task_id);

        if (!task.getUser().getEmail().equals(username)) {
            throw new TaskNotFoundException("Task " + task_id + "not found");
        }
        task.setStatus(status == 2 ? "PROGRESS" : "COMPLETED");
        return taskRepository.save(task);
    }
}
