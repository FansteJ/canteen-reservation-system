package org.example.canteenreservationsystem.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.canteenreservationsystem.controller.CanteenController;

import java.util.List;

@Getter
@Setter
public class CanteenStatusResponse {
    private Long canteenId;
    public List<SlotInfo> slots;

    public CanteenStatusResponse(Long id, List<SlotInfo> slots) {
        this.canteenId = id;
        this.slots = slots;
    }
}
