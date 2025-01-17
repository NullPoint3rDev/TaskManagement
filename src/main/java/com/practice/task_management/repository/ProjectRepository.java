package com.practice.task_management.repository;


import com.practice.task_management.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    // find project by project identifier
    Project findByProjectIdentifier(String projectId);

    // find all project
    @Override
    Iterable<Project> findAll();

    // find specific user project
    Iterable<Project> findAllByProjectLeader(String username);
}
