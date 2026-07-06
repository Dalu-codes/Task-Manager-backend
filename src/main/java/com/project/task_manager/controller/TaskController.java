package com.project.task_manager.controller;

import com.project.task_manager.dto.Response;
import com.project.task_manager.dto.TaskRequest;
import com.project.task_manager.entity.Task;
import com.project.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<Response<Task>> createTask(@Valid @RequestBody TaskRequest taskRequest){
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @PutMapping()
    public ResponseEntity<Response<Task>> updateTask( @RequestBody TaskRequest taskRequest){
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @GetMapping()
    public ResponseEntity<Response<List<Task>>> getAllMyTasks(){
        return ResponseEntity.ok(taskService.getAllMyTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Task>> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteTask(@PathVariable Long id){
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping("/status")
    public ResponseEntity<Response<List<Task>>> getAllTasksByCompletedStatus(
            @RequestParam boolean completed
    ){
        return ResponseEntity.ok(taskService.getMyTasksByCompletedStatus(completed));
    }

    @GetMapping("/priority")
    public ResponseEntity<Response<List<Task>>> getMyTasksByPriority(
            @RequestParam String priority
    ){

        return ResponseEntity.ok(taskService.getMyTasksByPriority(priority));
    }


}


