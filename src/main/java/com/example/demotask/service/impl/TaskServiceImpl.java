package com.example.demotask.service.impl;

import com.example.demotask.domain.Task;
import com.example.demotask.repository.TaskRepository;
import com.example.demotask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Optional<Task> findTask(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Page<Task> findAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> findAllToDoTasks(boolean complete, Pageable pageable) {
        return taskRepository.getAllByComplete(complete, pageable);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Long id, Task task) {

        Optional<Task> taskFound = taskRepository.findById(id);
        if (taskFound.isPresent()) {
            Task taskToUpdate = taskFound.get();
            taskToUpdate.setLabel(task.getLabel());
            taskToUpdate.setComplete(task.isComplete());
            return taskRepository.save(taskToUpdate);
        } else throw new RuntimeException("Task Not Existing");

    }
}
