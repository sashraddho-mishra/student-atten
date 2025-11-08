package com.example.demo.controller;

import com.example.demo.model.Attendance;
import com.example.demo.model.Student;
import com.example.demo.model.Subject;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.StudentService;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private com.example.demo.repository.AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) Long subjectId, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = userRepository.findByUsername(username).orElse(null);
        // provide subjects for selection
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("date", LocalDate.now());

        List<Student> students;
        String selectedCourse = null;
        if (subjectId != null) {
            Subject subj = subjectRepository.findById(subjectId).orElse(null);
            if (subj != null) {
                selectedCourse = subj.getName();
                students = studentService.listByCourse(selectedCourse);
            } else {
                students = studentService.listAll();
            }
        } else {
            // default: show all students
            students = studentService.listAll();
        }

        model.addAttribute("students", students);
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("course", selectedCourse);
        return "teachers/dashboard";
    }

    @GetMapping("/students")
    public String students(Model model) {
        model.addAttribute("students", studentService.listAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "teachers/students";
    }

    @GetMapping("/students/add")
    public String addStudentForm(@RequestParam(required = false) String course, Model model) {
        model.addAttribute("course", course);
        return "teachers/add-student";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam String name, @RequestParam String rollNumber, @RequestParam(required = false) String course, Model model) {
        // basic validation
        if (rollNumber == null || rollNumber.isBlank() || name == null || name.isBlank()) {
            model.addAttribute("error", "Name and roll number are required");
            model.addAttribute("course", course);
            return "teachers/add-student";
        }

        if (studentService.existsByRollNumber(rollNumber)) {
            // exact message requested by user
            model.addAttribute("error", "cannot add students with duplicate roll");
            model.addAttribute("course", course);
            return "teachers/add-student";
        }

        studentService.save(new Student(name, rollNumber, course));
        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/attendance")
    public String attendanceForm(Model model) {
        model.addAttribute("students", studentService.listAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "teachers/attendance";
    }

    @GetMapping("/attendance/report")
    public String attendanceReport(@RequestParam(required = false) Long subjectId,
                                   @RequestParam(required = false) String date,
                                   Model model) {
        model.addAttribute("subjects", subjectRepository.findAll());
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
        model.addAttribute("date", d);

        Subject subj = (subjectId == null) ? null : subjectRepository.findById(subjectId).orElse(null);
        List<Student> students = (subj == null) ? studentService.listAll() : studentService.listByCourse(subj.getName());

        // build a map of studentId -> present for the selected subject/date
        java.util.List<Attendance> attendanceList = (subj == null) ? java.util.Collections.emptyList() : attendanceService.listBySubjectAndDate(subj, d);
        java.util.Map<Long, Boolean> presentMap = new java.util.HashMap<>();
        for (Attendance a : attendanceList) {
            if (a.getStudent() != null) presentMap.put(a.getStudent().getId(), a.isPresent());
        }

        model.addAttribute("students", students);
        model.addAttribute("presentMap", presentMap);
        model.addAttribute("selectedSubjectId", subjectId);
        model.addAttribute("subject", subj);
        return "teachers/attendance-report";
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(@RequestParam Long studentId, @RequestParam Long subjectId,
                                 @RequestParam(defaultValue = "true") boolean present,
                                 @RequestParam(required = false) String date) {
        Student s = studentService.findById(studentId);
        Subject subj = subjectRepository.findById(subjectId).orElse(null);
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
        attendanceService.markAttendance(s, subj, d, present);
        // update student's current present flag
        if (s != null) {
            s.setPresent(present);
            studentService.save(s);
        }
        return "redirect:/teacher/attendance";
    }

    @PostMapping("/attendance/mark-bulk")
    public String markAttendanceBulk(@RequestParam(required = false) List<Long> presentIds,
                                      @RequestParam(required = false) String date,
                                      @RequestParam(required = false) Long subjectId) {
        if (subjectId == null) return "redirect:/teacher/dashboard";
        Subject subj = subjectRepository.findById(subjectId).orElse(null);
        if (subj == null) return "redirect:/teacher/dashboard";
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
        List<Student> students = studentService.listByCourse(subj.getName());
        for (Student s : students) {
            boolean present = presentIds != null && presentIds.contains(s.getId());
            attendanceService.markAttendance(s, subj, d, present);
            // persist present flag on student record as well
            s.setPresent(present);
            studentService.save(s);
        }
        return "redirect:/teacher/dashboard?subjectId=" + subjectId;
    }
}
