package com.practice.task_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "We need a project name")
    private String projectName;

    @NotBlank(message = "We need a project ID")
    @Size(min = 4, max = 5, message = "Use from 4 to 5 characters")
    @Column(unique = true, updatable = false)
    private String projectIdentifier;

    @NotBlank(message = "We need a project description")
    private String projectDescription;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    private String projectLead;

    @OneToMany(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER,
            mappedBy = "project",
            orphanRemoval = true
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "project"
    )
    @JsonIgnore
    private Backlog backlog;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "workers",
    joinColumns = {@JoinColumn(name = "project_id")},
    inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "WITHOUT TIMEZONE",
            updatable = false
    )
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "GMT+3")
    private Date createdAt;

    @Column(name = "updated_at",
    columnDefinition = "WITHOUT TIMEZONE")
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
