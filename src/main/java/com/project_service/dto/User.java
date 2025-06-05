package com.project_service.dto;

import java.util.UUID;

public class User {
    public UUID id;
    public User() {}
    public User(UUID id) { this.id = id; }
    public UUID getId() { return id; }

    public void setId(UUID id) {
        this.id = id;
    }
}