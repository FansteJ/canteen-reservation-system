package org.example.canteenreservationsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @Positive
    private int capacity;

    @ElementCollection
    private List<MealSlot> workingHours;

    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
