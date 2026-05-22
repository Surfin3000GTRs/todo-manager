package com.surfin3000gtrs.todomanager.model;

import jakarta.validation.constraints.NotBlank;

public class TaskForm {

    @NotBlank(message = "タイトルは必須です")
    private String title;

    private String description;

    private boolean completed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
