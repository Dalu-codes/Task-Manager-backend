package com.project.task_manager.service.impl;

import com.project.task_manager.dto.Response;
import com.project.task_manager.dto.UserRequest;
import com.project.task_manager.entity.User;
import com.project.task_manager.enums.Role;
import com.project.task_manager.exceptions.NotFoundExceptions;
import com.project.task_manager.repo.UserRepository;
import com.project.task_manager.security.JwtUtils;
import com.project.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    public Response<?> signUp(UserRequest userRequest) {
       log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());

        if(existingUser.isPresent()){
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Username already taken"
            );
        }
        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));


        //save user
        userRepository.save(user);
        return Response.builder()
                .StatusCode(HttpStatus.OK.value())
                .message("user registered successfully")
                .build();
    }

    @Override
    public Response<?> login(UserRequest userRequest) {

        log.info("Inside login()");
        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(()-> new NotFoundExceptions("User Not Found"));

        if(!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())){
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid Password");
        }
        String token = jwtUtils.generateToken(user.getUsername());

        return Response.builder()
                .StatusCode(HttpStatus.OK.value())
                .message("Logged in successfully!")
                .data(token)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundExceptions("User not found"));

    }




}
