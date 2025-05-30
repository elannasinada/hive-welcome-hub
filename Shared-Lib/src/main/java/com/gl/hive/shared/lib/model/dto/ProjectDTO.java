package com.gl.hive.shared.lib.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;

    private String projectName;
    private String description;
    private LocalDate createdAt;
    private LocalTime creationTime;
    private int memberCount;

    private Long leaderId;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer progress;

}
