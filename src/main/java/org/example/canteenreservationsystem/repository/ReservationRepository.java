package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
