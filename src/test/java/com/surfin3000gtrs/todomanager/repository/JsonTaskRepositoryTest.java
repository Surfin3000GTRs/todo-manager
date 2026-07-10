package com.surfin3000gtrs.todomanager.repository;

import com.surfin3000gtrs.todomanager.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JsonTaskRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void loadAndSaveTasksToJsonFile() {
        Path storageFile = tempDir.resolve("tasks.json");
        JsonTaskRepository repository = new JsonTaskRepository(storageFile.toString());

        repository.loadTasks();
        Task createdTask = repository
                .save(new Task(null, "first task", "description", false, LocalDateTime.of(2026, 7, 5, 12, 0)));
        repository.saveTasks();

        JsonTaskRepository reloadedRepository = new JsonTaskRepository(storageFile.toString());
        reloadedRepository.loadTasks();

        assertThat(reloadedRepository.findAll()).hasSize(1);
        assertThat(reloadedRepository.findById(createdTask.id())).isPresent();
        assertThat(reloadedRepository.findById(createdTask.id()).orElseThrow().title()).isEqualTo("first task");
    }
}
