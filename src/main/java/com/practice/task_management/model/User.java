package com.practice.task_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@Schema(description = "User")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя")
    private Integer id;

    @Schema(description = "Имя пользователя", example = "John Doe")
    @NotBlank(message = "Name is required!")
    private String name;

    @Email
    @Column(unique = true)
    @NotBlank(message = "Email is required!")
    @Schema(description = "Электронная почта пользователя", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // one to many with project
    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
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
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE",
            updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT")
    private Date createdAt;
    @Column(
            name = "updated_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT")
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

    public String getPassword() {
        return password;
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
