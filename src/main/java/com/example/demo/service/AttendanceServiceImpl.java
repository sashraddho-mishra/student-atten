package com.example.demo.service;

import com.example.demo.model.Attendance;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;
import com.example.demo.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public Attendance markAttendance(Student student, Subject subject, LocalDate date, boolean present) {
        // if an attendance record for this student/subject/date already exists, update it
        Optional<Attendance> existing = attendanceRepository.findByStudentAndSubjectAndDate(student, subject, date);
        if (existing.isPresent()) {
            Attendance a = existing.get();
            a.setPresent(present);
            return attendanceRepository.save(a);
        }

        Attendance a = new Attendance(student, subject, date, present);
        return attendanceRepository.save(a);
    }

    @Override
    public List<Attendance> listBySubjectAndDate(Subject subject, LocalDate date) {
        return attendanceRepository.findBySubjectAndDate(subject, date);
    }
}
