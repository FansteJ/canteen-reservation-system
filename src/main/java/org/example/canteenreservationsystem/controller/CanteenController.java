package org.example.canteenreservationsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.canteenreservationsystem.dto.CanteenStatusResponse;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.MealSlot;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.service.CanteenService;
import org.example.canteenreservationsystem.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<CanteenResponse>> getAllCanteens() {
        return ResponseEntity.ok(canteenService.getAllCanteens().stream().map(CanteenResponse::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanteenResponse> getCanteen(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new CanteenResponse(canteenService.getCanteenById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanteenResponse> updateCanteen(@PathVariable("id") Long id, @RequestHeader("studentId") Long studentId, @RequestBody CreateCanteenRequest request) {
        Student student = studentService.getStudent(studentId);
        if(student.isAdmin()) {
            Canteen updated = new Canteen();
            if(request.getName() != null)
                updated.setName(request.getName());
            if(request.getLocation() != null)
                updated.setLocation(request.getLocation());
            if(request.getCapacity() != null)
                updated.setCapacity(request.getCapacity());
            if(request.getWorkingHours() != null)
                updated.setWorkingHours(request.getWorkingHours());

            return ResponseEntity.ok(new CanteenResponse(canteenService.updateCanteen(id, updated)));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Canteen> deleteCanteen(@PathVariable("id") Long id, @RequestHeader("studentId") Long studentId) {
        Student student = studentService.getStudent(studentId);
        if(student.isAdmin()) {
            canteenService.deleteCanteen(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/status")
    public ResponseEntity<List<CanteenStatusResponse>> getCanteenStatus(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("endTime") LocalTime endTime,
            @RequestParam("duration") int duration
    ) {
        return ResponseEntity.ok(
                canteenService.getStatus(startDate, endDate, startTime, endTime, duration)
        );
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<CanteenStatusResponse> getCanteenStatusForCanteen(
            @PathVariable("id") Long id,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("endTime") LocalTime endTime,
            @RequestParam("duration") int duration
    ) {
        return ResponseEntity.ok(
                canteenService.getStatusForCanteen(id, startDate, endDate, startTime, endTime, duration)
        );
    }

    @Getter
    public static class CreateCanteenRequest {
        private String name;
        private String location;
        private Integer capacity;
        private List<MealSlot> workingHours;
    }

    @Getter
    @Setter
    public static class CanteenResponse{
        private Long id;
        private String name;
        private String location;
        private Integer capacity;
        private List<MealSlot> workingHours;

        public CanteenResponse() {}

        public CanteenResponse(Canteen canteen) {
            this.id = canteen.getId();
            this.name = canteen.getName();
            this.location = canteen.getLocation();
            this.capacity = canteen.getCapacity();
            this.workingHours = canteen.getWorkingHours();
        }
    }
}
