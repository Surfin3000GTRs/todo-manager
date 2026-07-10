package com.surfin3000gtrs.todomanager.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surfin3000gtrs.todomanager.model.Task;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SequencedMap;

@Repository
public class JsonTaskRepository implements TaskRepository {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final Path storagePath;
    private final SequencedMap<Long, Task> tasks = new LinkedHashMap<>();

    private long nextId = 1L;

    public JsonTaskRepository(@Value("${app.storage.file:./tasks.json}") String storageFile) {
        this.storagePath = Paths.get(storageFile);
    }

    @PostConstruct
    @Override
    public synchronized void loadTasks() {
        tasks.clear();
        nextId = 1L;

        if (!Files.exists(storagePath)) {
            return;
        }

        try {
            Task[] persistedTasks = objectMapper.readValue(storagePath.toFile(), Task[].class);
            for (Task task : persistedTasks) {
                Task storedTask = normalize(task);
                tasks.put(storedTask.id(), storedTask);
                nextId = Math.max(nextId, storedTask.id() + 1L);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load tasks from " + storagePath, exception);
        }
    }

    @PreDestroy
    @Override
    public synchronized void saveTasks() {
        try {
            Path parent = storagePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(storagePath.toFile(), findAll());
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to save tasks to " + storagePath, exception);
        }
    }

    @Override
    public synchronized List<Task> findAll() {
        return tasks.sequencedValues().stream().toList();
    }

    @Override
    public synchronized Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public synchronized Task save(Task task) {
        Task storedTask = normalize(Objects.requireNonNull(task, "task must not be null"));

        if (storedTask.id() == null || storedTask.id() <= 0L) {
            storedTask = storedTask.withId(nextId++);
        } else {
            nextId = Math.max(nextId, storedTask.id() + 1L);
        }

        tasks.put(storedTask.id(), storedTask);
        return storedTask;
    }

    @Override
    public synchronized boolean deleteById(long id) {
        return tasks.remove(id) != null;
    }

    private Task normalize(Task source) {
        if (source.id() == null || source.id() <= 0L) {
            return new Task(
                    null,
                    source.title(),
                    source.description(),
                    source.completed(),
                    source.createdAt());
        }

        return new Task(
                source.id(),
                source.title(),
                source.description(),
                source.completed(),
                source.createdAt());
    }
}
