package com.me.clouddrive.service;

import com.me.clouddrive.config.TestConfig;
import com.me.clouddrive.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

@Testcontainers
@SpringBootTest(classes = TestConfig.class)
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String VALID_NAME = "user1";
    private static final String VALID_PASSWORD = "password1";
    private static final String VALID_EMAIL = "user1@gmail.com";

    @Test
    void register_ifArgsIsValid_shouldBeSaved() {
        userService.register(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

        var savedUser = userRepository.findByUsername(VALID_NAME);

        assertThat(savedUser).isNotEmpty();
        var actualUser = savedUser.get();
        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser.getEmail()).isEqualTo(VALID_EMAIL);
        var isSamePassword = passwordEncoder.matches(VALID_PASSWORD, actualUser.getPassword());
        assertThat(isSamePassword).isTrue();
    }

    @Test
    void register_ifAddTwoUserWithSameUsername_shouldThrown() {
        var username = "user1";
        assertThatThrownBy(() -> {
            userService.register(username, anyString(), anyString());
            userService.register(username, anyString(), anyString());
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
