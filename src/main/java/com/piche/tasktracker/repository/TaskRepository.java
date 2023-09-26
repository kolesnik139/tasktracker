package com.piche.tasktracker.repository;

import com.piche.tasktracker.model.SortOrder;
import com.piche.tasktracker.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import com.piche.tasktracker.model.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    List<String> fields = Arrays.asList("id", "status_id", "title", "description", "due_date");
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TaskRowMapper taskRowMapper = new TaskRowMapper();

    public Optional<Task> findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ? AND active=true";
        return jdbcTemplate.query(sql, taskRowMapper, id).stream().findFirst();
    }

    public Page<Task> getTasks(Task filter, int pageNum, int pageSize, String sortField, SortOrder sortOrder) {
        StringBuilder dataSql = new StringBuilder("SELECT * FROM tasks WHERE active=true");
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM tasks WHERE active=true");
        StringBuilder body = new StringBuilder();
        StringBuilder tail = new StringBuilder();

        List<Object> commonParams = new ArrayList<>();
        List<Object> dataParams = new ArrayList<>();

        if (filter.getId() > 0) {
            body.append(" AND id = ?");
            commonParams.add(filter.getId());
        }

        if (filter.getStatusId() > 0) {
            body.append(" AND status_id = ?");
            commonParams.add(filter.getStatusId());
        }

        if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
            body.append(" AND LOWER(title) like ?");
            commonParams.add("%"+filter.getTitle().toLowerCase()+"%");
        }

        if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
            body.append(" AND LOWER(description) like ?");
            commonParams.add("%"+filter.getDescription().toLowerCase()+"%");
        }

        if (filter.getDueDate() != null) {
            body.append(" AND due_date = ?");
            commonParams.add(filter.getDueDate());
        }

        if (sortField != null && !sortField.trim().isEmpty()) {
            if(fields.contains(sortField)) {    // To avoid SQL injection
                tail.append(" ORDER BY "+sortField+" "+(sortOrder != null ? sortOrder : ""));
            }
        }

        tail.append(" LIMIT ? OFFSET ?");
        dataParams.add(pageSize);
        dataParams.add(pageNum * pageSize);

        String countSqlStr = countSql.append(body).toString();
        int totalItems = jdbcTemplate.queryForObject(countSqlStr, commonParams.toArray(), Integer.class);

        String dataSqlStr = dataSql.append(body).append(tail).toString();
        dataParams.addAll(0, commonParams);
        List<Task> tasks = jdbcTemplate.query(dataSqlStr, dataParams.toArray(), taskRowMapper);

        return new Page<Task>(tasks, pageNum, pageSize, totalItems, sortField, sortOrder);
    }

    public int addTask(Task task) {
        String sql = "INSERT INTO tasks (status_id, title, description, due_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, task.getStatusId(), task.getTitle(), task.getDescription(), task.getDueDate());
    }

    public int deactivateTask(int taskId) {
        String sql = "UPDATE tasks SET active = FALSE WHERE id = ?";
        return jdbcTemplate.update(sql, taskId);
    }

    public int updateTaskStatus(int taskId, int statusId) {
        String sql = "UPDATE tasks SET status_id = ? WHERE id = ? AND active=true";
        return jdbcTemplate.update(sql, statusId, taskId);
    }

    public void cleanUp(){ // Just for tests
        jdbcTemplate.update("DELETE FROM tasks");
        jdbcTemplate.update("ALTER TABLE tasks ALTER COLUMN id RESTART WITH 1");
    }
}
