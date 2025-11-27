package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.entity.Status;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.repository.CanteenRepository;
import org.example.canteenreservationsystem.repository.ReservationRepository;
import org.example.canteenreservationsystem.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Student not found"));
        Canteen canteen = canteenRepository.findById(canteenId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Canteen not found"));

        LocalTime reservationEnd = time.plusMinutes(duration);

        boolean validSlot = canteen.getWorkingHours().stream()
                .anyMatch(slot -> !time.isBefore(slot.getFrom()) && !reservationEnd.isAfter(slot.getTo()));

        if (!validSlot) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reservation time does not fall within any meal slot of the canteen"
            );
        }

        if(duration!=30 && duration!=60) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reservation duration should be between 30 and 60 minutes"
            );
        }

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setCanteen(canteen);
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setDuration(duration);
        reservation.setStatus(Status.ACTIVE);

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId, Long studentId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Reservation not found"));
        if(!reservation.getStudent().getId().equals(studentId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are not allowed to cancel this reservation!");
        }
        reservation.setStatus(Status.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByStudent(Long studentId) {
        return reservationRepository.findByStudentId(studentId);
    }
}
