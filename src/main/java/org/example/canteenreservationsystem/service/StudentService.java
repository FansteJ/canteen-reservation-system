package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student createStudent(String name, String email, boolean isAdmin) {

        if(studentRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        Student student = new Student();
        String parts[] = name.split(" ", 2);
        if(parts.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        }
        student.setFirstName(parts[0]);
        student.setLastName(parts.length > 1 ? parts[1] : "");
        if(email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        student.setEmail(email);
        student.setAdmin(isAdmin);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
