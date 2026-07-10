package com.surfin3000gtrs.todomanager.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record Task(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt) {

    public Task {
        title = Objects.requireNonNull(title, "title must not be null").strip();
        if (title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        description = description == null ? "" : description.strip();
        createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public Task withId(Long newId) {
        return new Task(newId, title, description, completed, createdAt);
    }

    public Task withContent(String newTitle, String newDescription) {
        return new Task(id, newTitle, newDescription, completed, createdAt);
    }

    public Task withCompleted(boolean newCompleted) {
        return new Task(id, title, description, newCompleted, createdAt);
    }
}
