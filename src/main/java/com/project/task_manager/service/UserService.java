package com.project.task_manager.service;

import com.project.task_manager.dto.Response;
import com.project.task_manager.dto.UserRequest;
import com.project.task_manager.entity.User;

public interface UserService {

    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedInUser();

}
