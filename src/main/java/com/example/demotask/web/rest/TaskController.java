package com.example.demotask.web.rest;


import com.example.demotask.domain.Task;
import com.example.demotask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        try {
            Task newTask = taskService.save(task);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        Optional<Task> task = taskService.findTask(id);

        if (task.isPresent()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<Task>> findAllTasks(Pageable pageable) {
        try {
            Page<Task> page = taskService.findAllTasks(pageable);
            if (page.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(page, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasksComplete")
    public ResponseEntity<Page<Task>> findAllToDoTasks(@RequestParam boolean complete, Pageable pageable) {
        try {
            Page<Task> page = taskService.findAllToDoTasks(complete, pageable);
            if (page.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(page, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {

        return new ResponseEntity<>(taskService.update(id, task), HttpStatus.OK);

    }
}
