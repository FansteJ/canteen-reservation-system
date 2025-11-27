package org.example.canteenreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SlotInfo {
    public String date;
    public String meal;
    public String startTime;
    public int remainingCapacity;

    public SlotInfo(LocalDate date, String meal, LocalTime startTime, int remaining) {
        this.date = date.toString();
        this.meal = meal;
        this.startTime = startTime.toString();
        this.remainingCapacity = remaining;
    }
}
