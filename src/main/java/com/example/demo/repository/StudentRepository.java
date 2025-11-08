package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
	java.util.List<Student> findByCourse(String course);
	java.util.Optional<Student> findByRollNumber(String rollNumber);
}
