package org.example.canteenreservationsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody CreateReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request.getStudentId(), request.getCanteenId(), request.getDate(), request.getTime(), request.getDuration());
        ReservationResponse reservationResponse = new ReservationResponse(reservation);
        return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id, @RequestHeader("studentId") Long studentId) {
        Reservation reservation = reservationService.cancelReservation(id, studentId);
        ReservationResponse reservationResponse = new ReservationResponse(reservation);
        return ResponseEntity.ok(reservationResponse);
    }

    @Getter
    @Setter
    public static class CreateReservationRequest {
        private Long studentId;
        private Long canteenId;
        private LocalDate date;
        private LocalTime time;
        private Integer duration;
    }

    @Getter
    @Setter
    public static class ReservationResponse {
        private Long id;
        private String status;
        private Long studentId;
        private Long canteenId;
        private LocalDate date;
        private LocalTime time;
        private Integer duration;

        public ReservationResponse(Reservation reservation) {
            this.id = reservation.getId();
            this.status = reservation.getStatus().name();
            this.studentId = reservation.getStudent().getId();
            this.canteenId = reservation.getCanteen().getId();
            this.date = reservation.getReservationDate();
            this.time = reservation.getReservationTime();
            this.duration = reservation.getDuration();
        }
    }
}
