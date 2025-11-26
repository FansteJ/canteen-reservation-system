package org.example.canteenreservationsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.MealSlot;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.service.CanteenService;
import org.example.canteenreservationsystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/canteens")
public class CanteenController {
    private final CanteenService canteenService;
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Canteen> createCanteen(@RequestHeader("studentId") Long studentId, @RequestBody CreateCanteenRequest request) {
        Student student = studentService.getStudent(studentId);
        if(student.isAdmin()) {
            Canteen canteen = canteenService.createCanteen(request.getName(), request.getLocation(), request.getCapacity(), request.getWorkingHours());
            return new ResponseEntity<>(canteen, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping
    public ResponseEntity<List<Canteen>> getAllCanteens() {
        return ResponseEntity.ok(canteenService.getAllCanteens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Canteen> getCanteen(@PathVariable Long id) {
        return ResponseEntity.ok(canteenService.getCanteenById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Canteen> updateCanteen(@PathVariable Long id, @RequestHeader("studentId") Long studentId, @RequestBody CreateCanteenRequest request) {
        Student student = studentService.getStudent(studentId);
        if(student.isAdmin()) {
            Canteen updated = new Canteen();
            updated.setName(request.getName());
            updated.setLocation(request.getLocation());
            updated.setCapacity(request.getCapacity());
            updated.setWorkingHours(request.getWorkingHours());

            return ResponseEntity.ok(canteenService.updateCanteen(id, updated));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Canteen> deleteCanteen(@PathVariable Long id, @RequestHeader("studentId") Long studentId) {
        Student student = studentService.getStudent(studentId);
        if(student.isAdmin()) {
            canteenService.deleteCanteen(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Getter
    public static class CreateCanteenRequest {
        private String name;
        private String location;
        private Integer capacity;
        private List<MealSlot> workingHours;
    }
}
