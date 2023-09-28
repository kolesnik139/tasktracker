package com.piche.tasktracker.service;

import com.piche.tasktracker.model.SortOrder;
import com.piche.tasktracker.model.Task;
import com.piche.tasktracker.model.Page;
import com.piche.tasktracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StatusService statusService;

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Page<Task> getTasks(Task filter, int pageNum, int pageSize, String sortField, SortOrder sortOrder) {
        return taskRepository.getTasks(filter, pageNum, pageSize, sortField, sortOrder);
    }

    public int addTask(Task task) {
        if (!statusService.existsById(task.getStatusId())) {
            throw new IllegalArgumentException("Invalid statusId provided");
        }
        return taskRepository.addTask(task);
    }

    public int deactivateTask(int taskId) {
        return taskRepository.deactivateTask(taskId);
    }

    public int updateTaskStatus(int taskId, int statusId) {
        return taskRepository.updateTaskStatus(taskId, statusId);
    }
}