package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    public Task create(String title, String description) {
        Task task = new Task(null, title, description, false, LocalDateTime.now());
        return taskRepository.save(task);
    }

    public TaskCommandResult createResult(String title, String description) {
        try {
            Task task = new Task(null, title, description, false, LocalDateTime.now());
            return new TaskCommandResult.Success(taskRepository.save(task));
        } catch (IllegalArgumentException | NullPointerException exception) {

            return new TaskCommandResult.ValidationError(exception.getMessage());
        }
    }

    public TaskCommandResult update(long id, String title, String description) {
        Optional<Task> found = taskRepository.findById(id);
        if (found.isEmpty()) {
            return new TaskCommandResult.NotFound(id);
        }

        try {
            Task updated = found.get().withContent(title, description);
        } catch (IllegalArgumentException | NullPointerException exception) {
        } catch (IllegalArgumentException exception) {
            return new TaskCommandResult.ValidationError(exception.getMessage());
        }
    }

    public TaskCommandResult complete(long id) {
        return taskRepository.findById(id)
                .map(task -> (TaskCommandResult) new TaskCommandResult.Success(
                        taskRepository.save(task.withCompleted(true))))
                .orElseGet(() -> new TaskCommandResult.NotFound(id));
    }

    public boolean delete(long id) {
        return taskRepository.deleteById(id);
    }
}
