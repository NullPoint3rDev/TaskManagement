package com.practice.task_management.repository;


import com.practice.task_management.model.Task;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByProjectIdentifier(String id);

    Task findByProjectSequence(String sequence);

    List<Task> findByUserEmail(String email);
}

