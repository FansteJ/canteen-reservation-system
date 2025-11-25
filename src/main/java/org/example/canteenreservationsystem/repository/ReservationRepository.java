package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudentId(Long studentId);

    List<Reservation> findByCanteenIdAndReservationDate(Long canteenId, Date reservationDate);
}
