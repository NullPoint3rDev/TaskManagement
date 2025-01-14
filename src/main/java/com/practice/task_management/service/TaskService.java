package com.practice.task_management.service;

import com.practice.task_management.dto.TaskDto;
import com.practice.task_management.exception.TaskNotFoundException;
import com.practice.task_management.model.Priority;
import com.practice.task_management.model.Task;
import com.practice.task_management.model.TaskStatus;
import com.practice.task_management.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto createTask(TaskDto taskDto, String userEmail) {

        if (taskDto.getTitle() == null || taskDto.getTitle().isEmpty()) {
            throw new TaskNotFoundException.InvalidRequestException("Task title cannot be empty");
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskRepository.save(task);
        return mapToDto(task);
    }

    public List<TaskDto> getTasks(String userEmail) {
        return taskRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return mapToDto(task);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskRepository.save(task);
        return mapToDto(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<TaskDto> getTasksWithFilters(String status, String priority, String title, Pageable pageable) {
        return taskRepository.findAllByFilters(status, priority, title, pageable)
                .map(this::mapToDto);
    }

    private TaskDto mapToDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setPriority(task.getPriority().name());
        return dto;
    }


//    private TaskDto mapToDto(Task task) {
//        TaskDto dto = new TaskDto();
//        dto.setId(task.getId());
//        dto.setTitle(task.getTitle());
//        dto.setDescription(task.getDescription());
//        dto.setStatus(String.valueOf(TaskStatus.valueOf(dto.getStatus())));
//        dto.setPriority(String.valueOf(Priority.valueOf(dto.getPriority())));
//        return dto;
//    }
}
