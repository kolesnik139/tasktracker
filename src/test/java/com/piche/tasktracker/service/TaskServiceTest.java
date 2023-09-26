package com.piche.tasktracker.service;

import com.piche.tasktracker.model.*;
import com.piche.tasktracker.repository.TaskRepository;
import com.piche.tasktracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    private StatusService statusService;

    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTask_withExistingStatusId() {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setStatusId(1);

        when(statusService.existsById(task.getStatusId())).thenReturn(true); // Mocking existing statusId
        when(taskRepository.addTask(task)).thenReturn(1);

        int rowsAffected = taskService.addTask(task);

        assertEquals(1, rowsAffected);
        verify(statusService).existsById(task.getStatusId());
        verify(taskRepository).addTask(task);
    }

    @Test
    void addTask_withNonExistingStatusId() {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setStatusId(1);

        when(statusService.existsById(task.getStatusId())).thenReturn(false); // Mocking non-existing statusId

        assertThrows(IllegalArgumentException.class, () -> taskService.addTask(task));
        verify(statusService).existsById(task.getStatusId());
        verify(taskRepository, never()).addTask(task);
    }

    // Other methods have no business logic, so nothing to test.

}
