package com.piche.tasktracker.controller;

import com.piche.tasktracker.model.*;
import com.piche.tasktracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void getTaskById_TaskExists_ReturnsTask() throws Exception {
        Task task = new Task();
        task.setId(1);
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getTaskById_TaskNotExists_ReturnsNotFound() throws Exception {
        when(taskService.getTaskById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addTask_ValidTask_AddsTaskAndReturnsSuccess() throws Exception {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Valid Title");
        task.setStatusId(1);
        when(taskService.addTask(any())).thenReturn(1);

        String taskJson = "{ \"id\": 1, \"statusId\": 1, \"title\": \"Valid Title\" }";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Task added successfully!"));
    }

    @Test
    void addTask_InvalidStatus_ReturnsBadRequest() throws Exception {
        String taskJson = "{ \"id\": 1, \"statusId\": -1, \"title\": \"Valid Title\" }";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("statusId should be a positive integer")));
    }

    @Test
    void addTask_EmptyTitle_ReturnsBadRequest() throws Exception {
        String taskJson = "{ \"statusId\": 1, \"title\": \"\" }";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Title is required")));
    }

    @Test
    void addTask_LongTitle_ReturnsBadRequest() throws Exception {
        String taskJson = "{ \"statusId\": 1, \"title\": \"000000000011111111112222222222333333333344444444445555555555" +
                "6666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555" +
                "6666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555\" }";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Title should not exceed 255 characters")));
    }

    @Test
    void getTasks_ReturnsTasks() throws Exception {
        List<Task> mockTasks = Arrays.asList(
                new Task(),
                new Task()
        );
        Page<Task> mockPage = new Page<>(mockTasks, 1, 10, 2, "id", SortOrder.DESC);

        when(taskService.getTasks(any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/tasks").param("pageNum", "0").param("pageSize", "10")
                        .param("sortField", "id").param("sortOrder", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.pageNum", is(1)))
                .andExpect(jsonPath("$.totalItems", is(2)))
                .andExpect(jsonPath("$.sortField", is("id")))
                .andExpect(jsonPath("$.sortOrder", is("DESC")));
    }

    @Test
    public void updateTaskStatus_ShouldReturnSuccess() throws Exception {
        int taskId = 1;
        int statusId = 2;

        when(taskService.updateTaskStatus(taskId, statusId)).thenReturn(1); // Предполагаем, что обновление прошло успешно

        mockMvc.perform(put("/api/tasks/" + taskId + "/status")
                        .param("statusId", String.valueOf(statusId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task status updated successfully!"));
    }

    @Test
    public void updateTaskStatus_ShouldReturnUnsuccessful() throws Exception {
        int taskId = 1;
        int statusId = 2;

        when(taskService.updateTaskStatus(taskId, statusId)).thenReturn(0); // Предполагаем, что обновление не удалось

        mockMvc.perform(put("/api/tasks/" + taskId + "/status")
                        .param("statusId", String.valueOf(statusId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to update task status. Task not found."));
    }

    @Test
    public void deactivateTask_ShouldReturnSuccess() throws Exception {
        int taskId = 1;

        when(taskService.deactivateTask(taskId)).thenReturn(1); // Предполагаем, что деактивация прошла успешно

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully!"));
    }

    @Test
    public void deactivateTask_ShouldReturnUnsuccessful() throws Exception {
        int taskId = 1;

        when(taskService.deactivateTask(taskId)).thenReturn(0); // Предполагаем, что деактивация не удалась

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to delete task. Task not found."));
    }

}
