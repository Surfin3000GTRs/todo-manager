package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceTest {

    @Test
    void createUpdateCompleteAndDeleteTask() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);

        TaskCommandResult createResult = taskService.createResult("title", "description");
        Task createdTask = switch (createResult) {
            case TaskCommandResult.Success(var task) -> task;
            case TaskCommandResult.NotFound(var id) -> throw new AssertionError("Task not found: " + id);
            case TaskCommandResult.ValidationError(var message) ->
                throw new AssertionError("Validation failed: " + message);
        };

        assertThat(createdTask.id()).isEqualTo(1L);
        assertThat(createdTask.completed()).isFalse();

        TaskCommandResult updateResult = taskService.update(createdTask.id(), "updated", "new description");
        Task updatedTask = switch (updateResult) {
            case TaskCommandResult.Success(var task) -> task;
            case TaskCommandResult.NotFound(var id) -> throw new AssertionError("Task not found: " + id);
            case TaskCommandResult.ValidationError(var message) ->
                throw new AssertionError("Validation failed: " + message);
        };

        assertThat(updatedTask.title()).isEqualTo("updated");
        assertThat(updatedTask.completed()).isFalse();

        TaskCommandResult completeResult = taskService.complete(createdTask.id());
        Task completedTask = switch (completeResult) {
            case TaskCommandResult.Success(var task) -> task;
            case TaskCommandResult.NotFound(var id) -> throw new AssertionError("Task not found: " + id);
            case TaskCommandResult.ValidationError(var message) ->
                throw new AssertionError("Validation failed: " + message);
        };
        assertThat(completedTask.completed()).isTrue();

        assertThat(taskService.findAll()).hasSize(1);

        assertThat(taskService.delete(createdTask.id())).isTrue();
        assertThat(taskService.findAll()).isEmpty();
    }

    @Test
    void updateReturnsNotFoundWhenTaskDoesNotExist() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);

        TaskCommandResult result = taskService.update(999L, "updated", "new description");

        String outcome = switch (result) {
            case TaskCommandResult.Success(var task) -> "success:" + task.id();
            case TaskCommandResult.NotFound(var id) -> "not_found:" + id;
            case TaskCommandResult.ValidationError(var message) -> "validation:" + message;
        };

        assertThat(outcome).isEqualTo("not_found:999");
    }

    @Test
    void updateReturnsValidationErrorWhenTitleIsBlank() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);

        Task createdTask = taskService.create("title", "description");
        TaskCommandResult result = taskService.update(createdTask.id(), "   ", "new description");

        String outcome = switch (result) {
            case TaskCommandResult.Success(var task) -> "success:" + task.id();
            case TaskCommandResult.NotFound(var id) -> "not_found:" + id;
            case TaskCommandResult.ValidationError(var message) -> "validation:" + message;
        };

        assertThat(outcome).contains("validation:").contains("title");
    }

    @Test
    void deleteReturnsFalseWhenTaskDoesNotExist() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(repository);

        assertThat(taskService.delete(999L)).isFalse();
    }

    private static final class InMemoryTaskRepository implements TaskRepository {

        private final Map<Long, Task> tasks = new HashMap<>();
        private long nextId = 1L;

        @Override
        public List<Task> findAll() {
            return new ArrayList<>(tasks.values());
        }

        @Override
        public Optional<Task> findById(long id) {
            return Optional.ofNullable(tasks.get(id));
        }

        @Override
        public Task save(Task task) {
            Task storedTask = new Task(
                    task.id(),
                    task.title(),
                    task.description(),
                    task.completed(),
                    task.createdAt() == null ? LocalDateTime.now() : task.createdAt());

            if (storedTask.id() == null || storedTask.id() <= 0L) {
                storedTask = storedTask.withId(nextId++);
            }

            tasks.put(storedTask.id(), storedTask);
            return storedTask;
        }

        @Override
        public boolean deleteById(long id) {
            return tasks.remove(id) != null;
        }

        @Override
        public void loadTasks() {
        }

        @Override
        public void saveTasks() {
        }
    }
}
