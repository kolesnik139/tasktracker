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

    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskById_ShouldReturnTask() { // Just for example. There is actually no business logic, so nothing to test.
        Task task = new Task();
        task.setId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1);

        assertTrue(result.isPresent());
        assertEquals(task.getId(), result.get().getId());
    }

}
