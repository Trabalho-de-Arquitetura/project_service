package com.project_service.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.project_service.entity.ProjectStatus;

public class UpdateProjectInput {
    public UUID id; // ID do projeto a ser atualizado
    public String name;
    public String objective;
    public String summaryScope;
    public String targetAudience;
    public LocalDate expectedStartDate;
    public ProjectStatus status;
    public UUID requesterId;
    public UUID groupId; // pode ser nulo

    public UpdateProjectInput(UUID id, String name, String objective, String summaryScope, String targetAudience,
                              LocalDate expectedStartDate, ProjectStatus status, UUID requesterId, UUID groupId) {
        this.id = id;
        this.name = name;
        this.objective = objective;
        this.summaryScope = summaryScope;
        this.targetAudience = targetAudience;
        this.expectedStartDate = expectedStartDate;
        this.status = status;
        this.requesterId = requesterId;
        this.groupId = groupId;
    }
}
