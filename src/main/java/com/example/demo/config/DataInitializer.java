package com.example.demo.config;

import com.example.demo.model.Subject;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SubjectRepository subjectRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public DataInitializer(SubjectRepository subjectRepository, UserService userService, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (subjectRepository.count() == 0) {
            subjectRepository.save(new Subject("BBA"));
            subjectRepository.save(new Subject("BCA"));
            subjectRepository.save(new Subject("BMS"));
            subjectRepository.save(new Subject("Cybersecurity"));
        }

        // Create default teacher if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            try {
                userService.registerTeacher("admin", "admin", "Admin", "User", 30, "");
                System.out.println("Created default teacher 'admin' with password 'admin'");
            } catch (Exception e) {
                System.out.println("Could not create default admin: " + e.getMessage());
            }
        }
    }
}
