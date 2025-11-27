package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudentId(Long studentId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.canteen = :canteen AND r.date = :date AND r.time >= :startTime AND r.time < :endTime")
    int countByCanteenAndDateAndTimeRange(@Param("canteen") Canteen canteen, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

}
