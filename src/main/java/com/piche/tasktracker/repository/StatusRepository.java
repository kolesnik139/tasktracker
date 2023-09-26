package com.piche.tasktracker.repository;

import com.piche.tasktracker.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatusRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final StatusRowMapper statusRowMapper = new StatusRowMapper();

    public List<Status> getStatuses() {
        String sql = "SELECT * FROM statuses";
        return jdbcTemplate.query(sql, statusRowMapper);
    }
}
