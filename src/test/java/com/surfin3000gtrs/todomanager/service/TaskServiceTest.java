package com.surfin3000gtrs.todomanager.service;

import com.surfin3000gtrs.todomanager.model.Task;
import com.surfin3000gtrs.todomanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void createUpdateDeleteAndReloadFromJson() {
        Path jsonFile = tempDir.resolve("tasks.json");
        TaskRepository repository = new TaskRepository(jsonFile.toString());
        TaskService service = new TaskService(repository);

        Task created = service.create("title", "desc");
        assertEquals(1, service.findAll().size());

        service.update(created.getId(), "updated", "updated-desc", true);
        Task updated = service.findById(created.getId());
        assertEquals("updated", updated.getTitle());
        assertTrue(updated.isCompleted());

        TaskRepository reloadedRepository = new TaskRepository(jsonFile.toString());
        TaskService reloadedService = new TaskService(reloadedRepository);
        assertEquals(1, reloadedService.findAll().size());

        reloadedService.delete(created.getId());
        assertTrue(reloadedService.findAll().isEmpty());
    }
}
