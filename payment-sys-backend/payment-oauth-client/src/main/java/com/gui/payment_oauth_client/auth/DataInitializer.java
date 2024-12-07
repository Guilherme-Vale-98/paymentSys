package com.gui.payment_oauth_client.auth;

import com.gui.payment_oauth_client.entities.User;
import com.gui.payment_oauth_client.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.findByName("testuser").isPresent()) {
            User testUser = new User();
            testUser.setName("testuser");
            testUser.setEmail("testuser@example.com");
            testUser.setEnabled(true);
            testUser.setPassword(passwordEncoder.encode("testpassword"));
            userRepository.save(testUser);

            System.out.println("Test user created with username 'testuser' and password 'testpassword'.");
        }
    }
}