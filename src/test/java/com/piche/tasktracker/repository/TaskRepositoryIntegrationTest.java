package com.piche.tasktracker.repository;

import com.piche.tasktracker.model.Page;
import com.piche.tasktracker.model.SortOrder;
import com.piche.tasktracker.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TaskRepository.class)
public class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void add20TasksTest(){
        for(int i=1; i<=20; i++){
            Task task = new Task();
            task.setStatusId((i % 7) + 1);
            task.setTitle("Title "+i);
            task.setDescription("Description "+i);
            task.setDueDate(new Date(123, 8, i));
            int result = taskRepository.addTask(task);
            assertEquals(1, result);
        }
    }

    @AfterEach
    public void cleanUp(){
        taskRepository.cleanUp();
    }
    

    @Test
    void findTaskByIdTest() {
        Task task = taskRepository.findById(1).orElse(null);
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals(2, task.getStatusId());
        assertEquals("Title 1", task.getTitle());
        assertEquals("Description 1", task.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(new Date(123, 8, 1), task.getDueDate());
    }

    @Test
    void deactivateTaskTest(){
        int result = taskRepository.deactivateTask(1);
        assertEquals(1, result);
        Task task = taskRepository.findById(1).orElse(null);
        assertNull(task);
    }
    
    @Test
    void filterByStatusIdTest(){
        Task filter = new Task();
        filter.setStatusId(5);
        Page<Task> page = taskRepository.getTasks(filter, 0, 100, null, null);
        assertEquals(3, page.getTotalItems());
        for(Task task: page.getContent()){
            assertEquals(5, task.getStatusId());
        }
    }

    @Test
    void filterByDueDateTest(){
        Task filter = new Task();
        filter.setDueDate(new Date(123, 8, 15));
        Page<Task> page = taskRepository.getTasks(filter, 0, 100, null, null);
        assertEquals(1, page.getTotalItems());
        assertEquals(new Date(123, 8, 15), page.getContent().get(0).getDueDate());

    }

    @Test
    void paginationTest(){
        Page<Task> page = taskRepository.getTasks(new Task(), 2, 5, null, null);
        assertEquals(2, page.getPageNum());
        assertEquals(5, page.getPageSize());
        assertEquals(20, page.getTotalItems());
        assertEquals(5, page.getContent().size());
        assertEquals(11, page.getContent().get(0).getId());
    }

    @Test
    void sortTitleASCTest(){
        Page<Task> page = taskRepository.getTasks(new Task(), 0, 100, "title", SortOrder.ASC);
        assertEquals("Title 1", page.getContent().get(0).getTitle());
        assertEquals("Title 9", page.getContent().get(19).getTitle());
        assertEquals("title", page.getSortField());
        assertEquals(SortOrder.ASC, page.getSortOrder());
    }

    @Test
    void sortDescriptionDESCTest(){
        Page<Task> page = taskRepository.getTasks(new Task(), 0, 100, "description", SortOrder.DESC);
        assertEquals("Description 9", page.getContent().get(0).getDescription());
        assertEquals("Description 1", page.getContent().get(19).getDescription());
        assertEquals("description", page.getSortField());
        assertEquals(SortOrder.DESC, page.getSortOrder());
    }

    @Test
    void filterSortPaginationTest(){
        Task filter = new Task();
        filter.setDescription("Description 1");
        filter.setTitle("Title 1");
        Page<Task> page = taskRepository.getTasks(filter, 2, 3, "description", SortOrder.DESC);
        assertEquals(2, page.getPageNum());
        assertEquals(3, page.getPageSize());
        assertEquals(11, page.getTotalItems());
        assertEquals(3, page.getContent().size());
        assertEquals("description", page.getSortField());
        assertEquals(SortOrder.DESC, page.getSortOrder());
        assertEquals("Description 13", page.getContent().get(0).getDescription());
        assertEquals("Description 12", page.getContent().get(1).getDescription());
        assertEquals("Description 11", page.getContent().get(2).getDescription());
    }

    @Test
    void updateTaskStatusTest(){
        int result = taskRepository.updateTaskStatus(1, 5);
        assertEquals(1, result);
        Task task = taskRepository.findById(1).orElse(null);
        assertEquals(5, task.getStatusId());
    }

    //Negative tests

    @Test
    void findByIdNotExistingTaskTest() {
        Task task = taskRepository.findById(555).orElse(null);
        assertNull(task);
    }

    @Test
    void deactivateNotExistingTaskTest(){
        int result = taskRepository.deactivateTask(555);
        assertEquals(0, result);
    }

    @Test
    void updateStatusOfNotExistingTaskTest(){
        int result = taskRepository.updateTaskStatus(555, 5);
        assertEquals(0, result);
    }

    @Test
    void updateTaskToNotExistingStatusTest(){
        assertThrows(DataIntegrityViolationException.class, () -> {
            taskRepository.updateTaskStatus(1, 555);
        });
    }

    @Test
    void addTaskWithNotExistingStatus(){
        assertThrows(DataIntegrityViolationException.class, () -> {
            Task task = new Task();
            task.setStatusId(555);
            task.setTitle("Some title");
            task.setDescription("Some description");
            taskRepository.addTask(task);
        });
    }

}
