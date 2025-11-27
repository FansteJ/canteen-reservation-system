package org.example.canteenreservationsystem.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class MealSlot {
    private String meal;

    @Column(name = "start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime from;
    @Column(name = "end_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime to;
}
