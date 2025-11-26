package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(String name, String email, boolean isAdmin) {
        Student student = new Student();
        String parts[] = name.split(" ", 2);
        student.setFirstName(parts[0]);
        student.setLastName(parts.length > 1 ? parts[1] : "");
        student.setEmail(email);
        student.setAdmin(isAdmin);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
