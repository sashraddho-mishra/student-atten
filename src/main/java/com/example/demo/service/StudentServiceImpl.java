package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student save(Student student) { return studentRepository.save(student); }

    @Override
    public List<Student> listAll() { return studentRepository.findAll(); }

    @Override
    public void delete(Long id) { studentRepository.deleteById(id); }

    @Override
    public Student findById(Long id) { return studentRepository.findById(id).orElse(null); }

    @Override
    public List<Student> listByCourse(String course) { return studentRepository.findByCourse(course); }

    @Override
    public boolean existsByRollNumber(String rollNumber) { return studentRepository.findByRollNumber(rollNumber).isPresent(); }

    @Override
    public java.util.Optional<Student> findByRollNumber(String rollNumber) { return studentRepository.findByRollNumber(rollNumber); }
}
