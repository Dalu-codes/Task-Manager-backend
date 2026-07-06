package com.project.task_manager.repo;

import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.User;
import com.project.task_manager.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task ,Long> {
    List<Task> findByUser(User user, Sort sort);
    List<Task> findByCompletedAndUser(Boolean completed, User user);
    List<Task> findByPriorityAndUser(Priority priority, User user, Sort sort);
}
