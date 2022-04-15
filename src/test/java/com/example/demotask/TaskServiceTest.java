package com.example.demotask;


import com.example.demotask.domain.Task;
import com.example.demotask.repository.TaskRepository;
import com.example.demotask.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;


@SpringBootTest
 class TaskServiceTest {

    @Autowired
    TaskService taskService;
    @MockBean
    private TaskRepository repository;



    @Test
    void findTaskTest() {
        Task task = new Task(1L,"Task1",true);
        doReturn(Optional.of(task)).when(repository).findById(1L);
        Optional<Task> returnedTask = taskService.findTask(1L);
        Assertions.assertTrue(returnedTask.isPresent(), "Task was not found");
        Assertions.assertSame(returnedTask.get(), task, "The Task returned was not the same as the mock");
    }

    @Test
    void save() {
        Task task = new Task(1L,"Task1",true);
        doReturn(task).when(repository).save(task);
        Task returnedTask = taskService.save(task);
        Assertions.assertSame(returnedTask, task, "The Task returned was not the same as the mock");
    }
    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 8);
        Task task = new Task(1L,"Task1",true);
        Task task2 = new Task(2L,"Task2",false);
        Page<Task> page= new PageImpl<>(Arrays.asList(task,task2));
        doReturn(page).when(repository).findAll(pageable);
        Page<Task> tasks = taskService.findAllTasks(pageable);
        Assertions.assertEquals(2, tasks.getTotalElements(), "findAll should return 2 tasks");
    }

    @Test
    void updateTest(){
        Task taskToUpdate = new Task(1L,"Task1",false);
        doReturn(Optional.of(taskToUpdate)).when(repository).findById(taskToUpdate.getId());
        doReturn(taskToUpdate).when(repository).save(taskToUpdate);
        taskToUpdate.setComplete(true);
        Task updatedTask= taskService.update(1L,taskToUpdate);
        Assertions.assertSame(updatedTask.isComplete(), taskToUpdate.isComplete(), "The Task returned was not the same as the mock");


    }

}
