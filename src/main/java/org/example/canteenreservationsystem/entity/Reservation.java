package org.example.canteenreservationsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Canteen canteen;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private int duration;

    @Enumerated(EnumType.STRING)
    private Status status;
}
