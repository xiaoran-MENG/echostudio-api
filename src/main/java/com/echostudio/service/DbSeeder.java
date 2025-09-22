package com.echostudio.service;

import com.echostudio.document.User;
import com.echostudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.echostudio.constant.SecurityConstants.DEFAULT_ADMIN_USER_EMAIL;
import static com.echostudio.constant.SecurityConstants.DEFAULT_ADMIN_USER_PASSWORD;

@Service
@RequiredArgsConstructor
@Slf4j
public class DbSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByEmail(DEFAULT_ADMIN_USER_EMAIL)) {
            log.info("Default admin user already exists");
            return;
        }

        var user = User.builder()
                .email(DEFAULT_ADMIN_USER_EMAIL)
                .password(DEFAULT_ADMIN_USER_PASSWORD)
                .role(User.Role.ADMIN)
                .build();

        this.userRepository.save(user);
        log.info("Default admin user created");
    }
}
