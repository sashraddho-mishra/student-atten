package com.example.demo.service;

import com.example.demo.model.Attendance;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    Attendance markAttendance(Student student, Subject subject, LocalDate date, boolean present);
    List<Attendance> listBySubjectAndDate(Subject subject, LocalDate date);
}
