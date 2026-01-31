package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> findAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        repository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }


    //<?> signifies wild card, response body can be of any Java type
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskData){
        return repository.findById(id)
                .map(task -> {
                    task.setTitle(taskData.getTitle());
                    task.setDescription(taskData.getDescription());
                    task.setCompleted(taskData.isCompleted());
                    repository.save(task);

                    // line below to create and return HTTP 204 No Content. This response
                    // indicates that request was successfully processed but no body to be returned
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    //if a task with this ID exists, delete it, return HTTP 204
    public ResponseEntity<?> deleteTask(@PathVariable Long id){
        return repository.findById(id)
                .map(task -> {
                    repository.delete(task);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
