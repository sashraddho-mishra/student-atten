package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {
    User registerTeacher(String username, String password, String firstName, String lastName, Integer age, String phone);
}
