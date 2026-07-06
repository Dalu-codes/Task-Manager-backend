package com.project.task_manager.security;

import com.project.task_manager.entity.User;
import com.project.task_manager.exceptions.NotFoundExceptions;
import com.project.task_manager.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new NotFoundExceptions("User not found"));

        return AuthUser.builder()
                .user(user)
                .build();
    }
}
