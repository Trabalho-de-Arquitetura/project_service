package com.project_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.project_service.dto.CreateProjectInput;
import com.project_service.dto.Group;
import com.project_service.dto.UpdateProjectInput;
import com.project_service.dto.User;
import com.project_service.entity.Project;
import com.project_service.entity.ProjectStatus;
import com.project_service.repository.ProjectRepository;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @QueryMapping
    public List<Project> findAllProjectsByRequester(@Argument UUID requester_id) {
        return projectRepository.findAllByRequesterId(requester_id);
    }

    @QueryMapping
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    @MutationMapping
    public Project saveProject(
            @Argument CreateProjectInput input
    ) {
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE ");
        System.out.println("TESTE "+ input.getName());
        System.out.println("INPUT "+ input.getExpectedStartDate());
        return projectRepository.save(new Project(
                input.name,
                input.objective,
                input.summaryScope,
                input.targetAudience,
                input.getExpectedStartDate(),
                ProjectStatus.PENDING_ANALYSIS,
                input.requesterId,
                null
        ));
    }

    @MutationMapping
    public Project updateProject(@Argument UpdateProjectInput input) {
        Project project = projectRepository.findById(input.id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + input.id));
        if (input.name != null) {
            project.setName(input.name);
        }
        if (input.objective != null) {
            project.setObjective(input.objective);
        }
        if (input.summaryScope != null) {
            project.setSummaryScope(input.summaryScope);
        }
        if (input.targetAudience != null) {
            project.setTargetAudience(input.targetAudience);
        }
        if (input.expectedStartDate != null) {
            project.setExpectedStartDate(input.expectedStartDate);
        }
        if (input.status != null) {
            project.setStatus(input.status);
        }
        if (input.requesterId != null) {
            project.setRequesterId(input.requesterId);
        }
        if (input.groupId != null) {
            project.setGroupId(input.groupId);
        }
        return projectRepository.save(project);
    }

    @SchemaMapping(typeName = "ProjectDTO", field = "requesterId")
    public UUID requester(Project projectDTO) {
        return new User(projectDTO.getRequesterId()).getId();
    }

    @SchemaMapping(typeName = "ProjectDTO", field = "groupId")
    public UUID group(Project projectDTO) {
        if (projectDTO.getGroupId() == null) {
            return null;
        }
        return new Group(projectDTO.getGroupId()).getId();
    }
}