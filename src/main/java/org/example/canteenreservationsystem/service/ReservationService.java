package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.entity.Status;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.repository.CanteenRepository;
import org.example.canteenreservationsystem.repository.ReservationRepository;
import org.example.canteenreservationsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final CanteenRepository canteenRepository;

    public Reservation createReservation(Long studentId, Long canteenId, LocalDate date, LocalTime time, int duration) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Canteen canteen = canteenRepository.findById(canteenId).orElseThrow(() -> new RuntimeException("Canteen not found"));

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setCanteen(canteen);
        reservation.setReservationDate(date);
        reservation.setReservationTime(time);
        reservation.setDuration(duration);
        reservation.setStatus(Status.ACTIVE);

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(Status.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByStudent(Long studentId) {
        return reservationRepository.findByStudentId(studentId);
    }
}
