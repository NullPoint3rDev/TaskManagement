package com.practice.task_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;


@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String projectSequence;

    @NotBlank(message = "Please include a project summary!")
    private String summary;

    private String name;

    private String status;

    private Integer priority;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dueDate;
    private String assignBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "backlog_id", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    @Column(updatable = false)
    private String projectIdentifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(
            name = "created_at", nullable = false,
            columnDefinition = "WITHOUT TIMEZONE", updatable = false
    )
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "GMT+3")
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "WITHOUT TIMEZONE")
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "GMT+3")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}

