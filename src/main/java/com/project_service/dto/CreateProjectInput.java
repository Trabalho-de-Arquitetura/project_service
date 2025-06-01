package com.project_service.dto;

import com.project_service.entity.ProjectStatus;

import java.time.LocalDate;
import java.util.UUID;

public class CreateProjectInput {
    public String name;
    public String objective;
    public String summaryScope;
    public String targetAudience;
    public LocalDate expectedStartDate;
    public ProjectStatus status;
    public UUID requesterId;
    public UUID groupId; // pode ser nulo
}