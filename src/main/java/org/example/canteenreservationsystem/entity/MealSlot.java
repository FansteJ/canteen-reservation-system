package org.example.canteenreservationsystem.entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class MealSlot {
    private String mealName;
    private LocalTime startTime;
    private LocalTime endTime;
}
