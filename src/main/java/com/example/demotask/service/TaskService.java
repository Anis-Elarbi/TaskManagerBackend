package com.example.demotask.service;

import com.example.demotask.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {

    Optional<Task> findTask(Long id);

    Page<Task> findAllTasks(Pageable pageable);

    Page<Task> findAllToDoTasks(boolean complete, Pageable pageable);

    Task save(Task task);

    Task update(Long id, Task task);


}
