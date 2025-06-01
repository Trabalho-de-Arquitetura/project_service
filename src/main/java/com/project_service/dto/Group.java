package com.project_service.dto;

import java.util.UUID;

public class Group {
    private UUID id;
    public Group(UUID id) { this.id = id; }
    public UUID getId() { return id; }
}