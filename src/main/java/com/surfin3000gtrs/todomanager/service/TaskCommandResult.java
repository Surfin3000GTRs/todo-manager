package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;

public sealed interface TaskCommandResult permits TaskCommandResult.Success, TaskCommandResult.NotFound,
        TaskCommandResult.ValidationError {

    sealed interface Success extends TaskCommandResult permits Created, Updated, Completed, Deleted {
    }

    record Created(Task task) implements Success {
    }

    record Updated(Task task) implements Success {
    }

    record Completed(Task task) implements Success {
    }

    record Deleted(long id) implements Success {
    }

    record NotFound(long id) implements TaskCommandResult {
    }

    record ValidationError(String message) implements TaskCommandResult {
    }
}
