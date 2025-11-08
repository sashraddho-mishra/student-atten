package com.example.demo.service;

import com.example.demo.model.Student;

import java.util.List;

public interface StudentService {
    Student save(Student student);
    List<Student> listAll();
    void delete(Long id);
    Student findById(Long id);
    List<Student> listByCourse(String course);
    boolean existsByRollNumber(String rollNumber);
    java.util.Optional<Student> findByRollNumber(String rollNumber);
}
