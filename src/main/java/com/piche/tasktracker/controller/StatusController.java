package com.piche.tasktracker.controller;

import com.piche.tasktracker.model.Status;
import com.piche.tasktracker.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping
    public List<Status> getStatuses() { // Not in the requirements, but you'll definitely need it
        return statusService.getStatuses();
    }
}
