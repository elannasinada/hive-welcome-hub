package com.gl.hive.ProjectService.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity for managing the relationship of members of a Project.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_members")
public class ProjectMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;

    /* relationships */
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    /* end of relationships */

    private boolean active = true;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;

    public ProjectMembers(Long userId, Project project) {
        this.userId = userId;
        this.project = project;
    }

}
