package com.surfin3000gtrs.todomanager.repository;

import com.surfin3000gtrs.todomanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(long id);

    Task save(Task task);

    boolean deleteById(long id);

    void loadTasks();

    void saveTasks();
}
