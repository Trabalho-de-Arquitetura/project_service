package com.project_service.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT") // Para textos mais longos
    private String objective;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summaryScope;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String targetAudience;

    @Column(nullable = false)
    private LocalDate expectedStartDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId; // ID do User requisitante

    @Column(name = "group_id") // Pode ser nulo se o projeto ainda não foi atribuído a um grupo
    private UUID groupId; // ID do Group

    public Project() {}
    public Project(String name, String objective, String summaryScope, String targetAudience, LocalDate expectedStartDate, ProjectStatus status, UUID requesterId, UUID groupId) {
        this.name = name;
        this.objective = objective;
        this.summaryScope = summaryScope;
        this.targetAudience = targetAudience;
        this.expectedStartDate = expectedStartDate;
        this.status = status;
        this.requesterId = requesterId;
        this.groupId = groupId;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }
    public String getSummaryScope() { return summaryScope; }
    public void setSummaryScope(String summaryScope) { this.summaryScope = summaryScope; }
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    public LocalDate getExpectedStartDate() { return expectedStartDate; }
    public void setExpectedStartDate(LocalDate expectedStartDate) { this.expectedStartDate = expectedStartDate; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public UUID getRequesterId() { return requesterId; }
    public void setRequesterId(UUID requesterId) { this.requesterId = requesterId; }
    public UUID getGroupId() { return groupId; }
    public void setGroupId(UUID groupId) { this.groupId = groupId; }
}