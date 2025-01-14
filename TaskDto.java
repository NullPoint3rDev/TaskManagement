package com.practice.task_management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;


@ApiModel(description = "Details about the task")
public class TaskDto {

    @ApiModelProperty(notes = "The unique ID of the task")
    private Long id;

    @ApiModelProperty(notes = "The title of the task")
    @NotBlank(message = "Title is required")
    private String title;

    @ApiModelProperty(notes = "A description of the task")
    private String description;

    @ApiModelProperty(notes = "The current status of the task")
    @NotBlank(message = "Status is required")
    private String status;

    @ApiModelProperty(notes = "The priority level of the task")
    @NotBlank(message = "Priority is required")
    private String priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
