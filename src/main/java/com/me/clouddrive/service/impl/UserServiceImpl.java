package com.me.clouddrive.service.impl;

import com.me.clouddrive.entity.User;
import com.me.clouddrive.repository.UserRepository;
import com.me.clouddrive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void register(String username, String email, String password) {
        var newUser = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(newUser);
    }
}
