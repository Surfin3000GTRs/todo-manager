package com.surfin3000gtrs.todomanager.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.surfin3000gtrs.todomanager.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final Path filePath;
    private final ObjectMapper objectMapper;
    private final List<Task> tasks;
    private long nextId;

    public TaskRepository(@Value("${app.tasks.file:tasks.json}") String fileName) {
        this.filePath = Paths.get(fileName);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.tasks = loadTasks();
        this.nextId = this.tasks.stream()
                .map(Task::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    public synchronized List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public synchronized Optional<Task> findById(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    public synchronized Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
            tasks.add(task);
        } else {
            tasks.removeIf(existingTask -> existingTask.getId().equals(task.getId()));
            tasks.add(task);
        }
        saveTasks();
        return task;
    }

    public synchronized void deleteById(Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
        saveTasks();
    }

    private List<Task> loadTasks() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(filePath.toFile(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("タスクファイルの読み込みに失敗しました: " + filePath, e);
        }
    }

    private void saveTasks() {
        try {
            Path parent = filePath.toAbsolutePath().getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), tasks);
        } catch (IOException e) {
            throw new IllegalStateException("タスクファイルの保存に失敗しました: " + filePath, e);
        }
    }
}
