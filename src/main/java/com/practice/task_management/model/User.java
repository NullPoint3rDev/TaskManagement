package com.practice.task_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "User's name is required")
    private String name;

    @Email
    @Column(unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    @Getter
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            mappedBy = "users"
    )
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @OneToMany(
            cascade = CascadeType.REFRESH,
            fetch = FetchType.EAGER,
            mappedBy = "user",
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();
    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "WITHOUT TIMEZONE",
            updatable = false
    )
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "GMT+3")
    private Date createdAt;
    @Column(
            name = "updated_at",
            columnDefinition = "WITHOUT TIMEZONE"
    )
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}

