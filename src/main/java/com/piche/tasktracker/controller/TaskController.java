package com.piche.tasktracker.controller;

import com.piche.tasktracker.model.Page;
import com.piche.tasktracker.model.SortOrder;
import com.piche.tasktracker.model.Task;
import com.piche.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<Task> getTasks(@RequestParam(defaultValue = "0") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(required = false) String sortField,
                               @RequestParam(required = false) SortOrder sortOrder,
                               @RequestParam(defaultValue = "-1") int id,
                               @RequestParam(defaultValue = "-1") int statusId,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate) {

        Task filter = new Task();
        filter.setId(id);
        filter.setStatusId(statusId);
        filter.setTitle(title);
        filter.setDescription(description);
        filter.setDueDate(dueDate);

        return taskService.getTasks(filter, pageNum, pageSize, sortField, sortOrder);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try{
            int rowsAffected = taskService.addTask(task);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("Task added successfully!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to add task.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<String> updateTaskStatus(@PathVariable int taskId, @RequestParam int statusId) {
        int rowsAffected = taskService.updateTaskStatus(taskId, statusId);
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Task status updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update task status. Task not found.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deactivateTask(@PathVariable int taskId) {
        int rowsAffected = taskService.deactivateTask(taskId);
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Task deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete task. Task not found.", HttpStatus.NOT_FOUND);
        }
    }
}
