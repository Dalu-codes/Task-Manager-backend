package com.project.task_manager.service.impl;

import com.project.task_manager.dto.Response;
import com.project.task_manager.dto.TaskRequest;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.User;
import com.project.task_manager.enums.Priority;
import com.project.task_manager.exceptions.NotFoundExceptions;
import com.project.task_manager.repo.TaskRepository;
import com.project.task_manager.service.TaskService;
import com.project.task_manager.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final JsonMapper.Builder builder;

    @Override
    public Response<Task> createTask(TaskRequest taskRequest) {
       log.info("Inside createTask()");
       User user = userService.getCurrentLoggedInUser();
       Task taskToSave = Task.builder()
               .title(taskRequest.getTitle())
               .description(taskRequest.getDescription())
               .completed(taskRequest.getCompleted())
               .priority(taskRequest.getPriority())
               .dueDate(taskRequest.getDueDate())
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .user(user)
               .build();

       Task savedTask = taskRepository.save(taskToSave);

       return Response.<Task>builder()
               .StatusCode(HttpStatus.OK.value())
               .message("Task Created Successfully")
               .data(savedTask)
               .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getAllMyTasks() {
       log.info("Inside getAllMyTasks");
       User currentUser = userService.getCurrentLoggedInUser();

       List<Task> tasks = taskRepository.findByUser(currentUser, Sort.by(Sort.Direction.DESC, "id"));

       return Response.<List<Task>>builder()
               .StatusCode(HttpStatus.OK.value())
               .message("Task retrieved successfully")
               .data(tasks)
               .build();
    }

    @Override
    public Response<Task> getTaskById(Long id) {
            log.info("Inside getTaskById");

            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new NotFoundExceptions("Task not found"));

        return Response.<Task>builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Task retrieved successfully")
                .data(task)
                .build();
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("Inside updateTask");

        Task task = taskRepository.findById(taskRequest.getId())
                .orElseThrow(() -> new NotFoundExceptions("Task not found"));

       if(taskRequest.getTitle() != null) task.setTitle(taskRequest.getTitle());
       if(taskRequest.getDescription() != null) task.setDescription(taskRequest.getDescription());
       if(taskRequest.getCompleted() != null) task.setCompleted(taskRequest.getCompleted());
       if(taskRequest.getPriority() != null) task.setPriority(taskRequest.getPriority());
       if(taskRequest.getDueDate() != null) task.setDueDate(taskRequest.getDueDate());
       task.setUpdatedAt(LocalDateTime.now());

       //update the task
        Task updatedTask = taskRepository.save(task);

        return Response.<Task>builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Task updated successfully")
                .data(updatedTask)
                .build();
    }

    @Override
    public Response<Void> deleteTask(Long id) {
        log.info("Inside deleteTask");

        if(!taskRepository.existsById(id)){
            throw new NotFoundExceptions("Task does not exist");
        }
        taskRepository.deleteById(id);

        return Response.<Void>builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Task deleted successfully")
                .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getMyTasksByCompletedStatus(boolean completed) {
       log.info("Inside getMyTasksByCompletedStatus()");

       User currentUser = userService.getCurrentLoggedInUser();
       List<Task> tasks = taskRepository.findByCompletedAndUser(completed, currentUser);

        return Response.<List<Task>>builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Task filtered by completed status for user")
                .data(tasks)
                .build();
    }

    @Override
    public Response<List<Task>> getMyTasksByPriority(String priority) {
        log.info("Inside getMyTasksByPriority()");

        User currentUser = userService.getCurrentLoggedInUser();
        Priority priority1 = Priority.valueOf(priority.toUpperCase());

        List<Task> tasks = taskRepository.findByPriorityAndUser
                (priority1, currentUser, Sort.by(Sort.Direction.DESC, "id"));

        return Response.<List<Task>>builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Task filtered by priority status for user")
                .data(tasks)
                .build();
    }
}
