package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll().stream()
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .toList();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("タスクが見つかりません: " + id));
    }

    public Task create(String title, String description) {
        Task task = new Task(null, title, description, false, LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task update(Long id, String title, String description, boolean completed) {
        Task existingTask = findById(id);
        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setCompleted(completed);
        return taskRepository.save(existingTask);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public Task toggleCompleted(Long id) {
        Task existingTask = findById(id);
        existingTask.setCompleted(!existingTask.isCompleted());
        return taskRepository.save(existingTask);
    }
}
