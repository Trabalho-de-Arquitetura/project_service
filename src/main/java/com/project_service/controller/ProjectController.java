package com.project_service.controller;

import com.project_service.dto.CreateProjectInput;
import com.project_service.dto.Group;
import com.project_service.dto.User;
import com.project_service.entity.Project;
import com.project_service.entity.ProjectStatus;
import com.project_service.repository.ProjectRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @QueryMapping
    public Project projectById(@Argument UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Project> allProjects() {
        return projectRepository.findAll();
    }

    @MutationMapping
    public Project createProject(@Argument CreateProjectInput input) {
        Project project = new Project();
        project.setName(input.name);
        project.setObjective(input.objective);
        project.setSummaryScope(input.summaryScope);
        project.setTargetAudience(input.targetAudience);
        project.setExpectedStartDate(input.expectedStartDate);
        project.setStatus(input.status != null ? input.status : ProjectStatus.PENDING_ANALYSIS);
        project.setRequesterId(input.requesterId);
        project.setGroupId(input.groupId);
        return projectRepository.save(project);
    }

    @SchemaMapping(typeName = "Project", field = "requester")
    public UUID requester(Project project) {
        return new User(project.getRequesterId()).getId();
    }

    @SchemaMapping(typeName = "Project", field = "group")
    public UUID group(Project project) {
        if (project.getGroupId() == null) {
            return null;
        }
        return new Group(project.getGroupId()).getId();
    }
}