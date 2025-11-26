package org.example.canteenreservationsystem.controller;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody StudentRequest request) {
        Student student = studentService.createStudent(request.getName(), request.getEmail(), request.isAdmin());
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        return ResponseEntity.ok(student);
    }

    @Getter
    public static class StudentRequest {
        private String name;
        private String email;
        private boolean isAdmin;
    }
}
