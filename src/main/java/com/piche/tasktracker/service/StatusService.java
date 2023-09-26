package com.piche.tasktracker.service;

import com.piche.tasktracker.model.Status;
import com.piche.tasktracker.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public List<Status> getStatuses() {
        return statusRepository.getStatuses();
    }
}