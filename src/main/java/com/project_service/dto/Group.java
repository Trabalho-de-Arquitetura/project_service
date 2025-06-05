package com.project_service.dto;

import java.util.UUID;

public class Group {
    public UUID id;
    public Group() {}
    public Group(UUID id) { this.id = id; }
    public UUID getId() { return id; }
    public void setId(UUID id) {
        this.id = id;
    }
}