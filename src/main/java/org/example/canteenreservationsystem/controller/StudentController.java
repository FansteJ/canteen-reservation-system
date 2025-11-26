package org.example.canteenreservationsystem.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest request) {
        Student student = studentService.createStudent(request.getName(), request.getEmail(), request.isAdmin());
        StudentResponse studentResponse = new StudentResponse(student);
        return new ResponseEntity<>(studentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        StudentResponse studentResponse = new StudentResponse(student);
        return ResponseEntity.ok(studentResponse);
    }

    @Getter
    @Setter
    public static class StudentRequest {
        private String name;
        private String email;
        @JsonProperty("isAdmin")
        private boolean admin;
    }

    @Getter
    @Setter
    public static class StudentResponse{
        private Long id;
        private String name;
        private String email;
        @JsonProperty("isAdmin")
        private boolean admin;

        public StudentResponse(Student student) {
            this.id = student.getId();
            this.name = student.getFirstName() + " " + student.getLastName();
            this.email = student.getEmail();
            this.admin = student.isAdmin();
        }
    }
}
