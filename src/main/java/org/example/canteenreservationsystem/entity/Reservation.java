package org.example.canteenreservationsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;

    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private int duration;

    @Enumerated(EnumType.STRING)
    private Status status;
}
