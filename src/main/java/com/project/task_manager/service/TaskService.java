package com.project.task_manager.service;

import com.project.task_manager.dto.Response;
import com.project.task_manager.dto.TaskRequest;
import com.project.task_manager.entity.Task;

import java.util.List;

public interface TaskService {
    Response<Task> createTask(TaskRequest taskRequest);
    Response<List<Task>> getAllMyTasks();
    Response<Task> getTaskById(Long id);
    Response<Task> updateTask(TaskRequest taskRequest);
    Response<Void> deleteTask(Long id);
    Response<List<Task>> getMyTasksByCompletedStatus(boolean completed);
    Response<List<Task>> getMyTasksByPriority(String priority);
}
