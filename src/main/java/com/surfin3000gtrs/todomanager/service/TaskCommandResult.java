package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;

public sealed interface TaskCommandResult permits TaskCommandResult.Success, TaskCommandResult.NotFound,
        TaskCommandResult.ValidationError {

    record Success(Task task) implements TaskCommandResult {
    }

    record NotFound(long id) implements TaskCommandResult {
    }

    record ValidationError(String message) implements TaskCommandResult {
    }
}
