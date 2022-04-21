package com.example.demotask;

import com.example.demotask.domain.Task;
import com.example.demotask.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void createTask_isCreated() throws Exception {
        Task task = new Task(1L,"Task1",true);
        when(taskService.save(Mockito.any(Task.class))).thenReturn(task);

        MvcResult mvcResult = mockMvc.perform(post("/api/task").contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        Task result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Task.class);
        assertThat(result).isNotNull();
    }

    @Test
    public void getTaskById_NotFound() throws Exception {
        when(taskService.findTask(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/task/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTaskById_found() throws Exception {
        Task task = new Task(1L,"Task1",true);
        when(taskService.findTask(1L)).thenReturn(Optional.of(task));

        MvcResult mvcResult = mockMvc.perform(get("/api/task/1").contentType(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andExpect(jsonPath("$.label",equalTo(task.getLabel())))
                                     .andExpect(jsonPath("$.complete",equalTo(task.isComplete())))
                                     .andReturn();
        Task result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Task.class);
        assertThat(result).isNotNull();
    }

    @Test
    public void findAllToDoTasks_isOk() throws Exception {
        Pageable pageable = PageRequest.of(0, 8);
        Task task1 = new Task(1L,"Task1",true);
        Task task2 = new Task(2L,"Task2",true);
        Page<Task> page= new PageImpl<>(Arrays.asList(task1,task2));
        when(taskService.findAllToDoTasks(anyBoolean(),any())).thenReturn(page);

        mockMvc.perform(get("/api/tasksComplete").contentType(MediaType.APPLICATION_JSON)
                                                           .param("complete",String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].label",equalTo(task1.getLabel())));
    }

    @Test
    public void findAllToDoTasks_isNoContent() throws Exception {
        when(taskService.findAllToDoTasks(anyBoolean(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/tasksComplete").contentType(MediaType.APPLICATION_JSON)
                        .param("complete",String.valueOf(true)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateTask_isOk() throws Exception {
        Task task = new Task(1L,"Task1",true);
        when(taskService.update(anyLong(),Mockito.any(Task.class))).thenReturn(task);

        MvcResult mvcResult = mockMvc.perform(put("/api/task/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andReturn();

        Task result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Task.class);
        assertThat(result).isNotNull();
        assertThat(result.getLabel()).isEqualTo(task.getLabel());
        assertThat(result.isComplete()).isEqualTo(task.isComplete());
    }


}
