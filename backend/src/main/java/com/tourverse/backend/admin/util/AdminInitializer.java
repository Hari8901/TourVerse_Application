package com.tourverse.backend.admin.util;

import com.tourverse.backend.admin.entity.Admin;
import com.tourverse.backend.user.entity.User;
import com.tourverse.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
    	
        if (!userRepository.existsByEmail("admin@tourverse.com")) {
            Admin admin = Admin.builder()
                    .name("Super Admin")
                    .email("admin@tourverse.com")
                    .phone("1234567890")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .employeeId("EMP001")
                    .department("IT/Operations")
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin user created successfully.");
        }
    }
}