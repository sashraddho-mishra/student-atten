package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerTeacher(String username, String password, String firstName, String lastName, Integer age, String phone) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("username exists");
        }
        // course removed from registration. A teacher can teach multiple courses — they will select course in the dashboard when marking attendance.
        User u = new User(username, passwordEncoder.encode(password), "ROLE_TEACHER", firstName, lastName, age, phone, null);
        return userRepository.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(),
                List.of(new SimpleGrantedAuthority(u.getRole())));
    }
}
