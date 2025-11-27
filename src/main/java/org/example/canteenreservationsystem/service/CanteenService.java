package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.dto.CanteenStatusResponse;
import org.example.canteenreservationsystem.dto.SlotInfo;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.MealSlot;
import org.example.canteenreservationsystem.entity.Status;
import org.example.canteenreservationsystem.repository.CanteenRepository;
import org.example.canteenreservationsystem.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
    private final ReservationRepository reservationRepository;

    public Canteen createCanteen(String name, String location, Integer capacity, List<MealSlot> workingHours) {
        Canteen canteen = new Canteen();
        if(name==null || name.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be empty");
        if (canteenRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Canteen with this name already exists");
        }
        canteen.setName(name);
        if(location==null || location.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location cannot be empty");
        canteen.setLocation(location);
        if(capacity==null || capacity < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity cannot be negative");
        canteen.setCapacity(capacity);
        if(workingHours==null || workingHours.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "WorkingHours cannot be empty");
        canteen.setWorkingHours(workingHours);
        return canteenRepository.save(canteen);
    }

    public List<Canteen> getAllCanteens() {
        return canteenRepository.findAll();
    }

    public Canteen getCanteenById(Long id) {
        return canteenRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canteen not found"));
    }

    public Canteen updateCanteen(Long id, Canteen updatedCanteen) {
        Canteen canteen = getCanteenById(id);
        if (updatedCanteen.getName() != null) {
            canteen.setName(updatedCanteen.getName());
        }
        if (updatedCanteen.getLocation() != null) {
            canteen.setLocation(updatedCanteen.getLocation());
        }
        if (updatedCanteen.getCapacity() > 0) {
            canteen.setCapacity(updatedCanteen.getCapacity());
        }
        if (updatedCanteen.getWorkingHours() != null) {
            canteen.setWorkingHours(updatedCanteen.getWorkingHours());
        }
        return canteenRepository.save(canteen);
    }

    public void deleteCanteen(Long id) {
        Canteen canteen = getCanteenById(id);
        canteen.getReservations().forEach(reservation -> {
            reservation.setStatus(Status.CANCELLED);
            reservationRepository.save(reservation);
        });
        canteenRepository.deleteById(id);
    }

    public List<CanteenStatusResponse> getStatus(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            int durationMinutes
    ) {
        if(durationMinutes != 30 && durationMinutes != 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Canteen duration should be 30 or 60");
        }
        List<Canteen> canteens = canteenRepository.findAll();
        List<CanteenStatusResponse> result = new ArrayList<>();

        for (Canteen canteen : canteens) {
            List<SlotInfo> slotInfos = new ArrayList<>();

            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                for (MealSlot mealSlot : canteen.getWorkingHours()) {

                    LocalTime slotStart = mealSlot.getFrom().isAfter(startTime) ? mealSlot.getFrom() : startTime;
                    LocalTime slotEnd = mealSlot.getTo().isBefore(endTime) ? mealSlot.getTo() : endTime;

                    LocalTime time = slotStart;
                    while (!time.plusMinutes(durationMinutes).isAfter(slotEnd)) {

                        int reserved = reservationRepository.countByCanteenAndDateAndTimeRange(
                                canteen, date, time, time.plusMinutes(durationMinutes)
                        );

                        slotInfos.add(new SlotInfo(
                                date,
                                mealSlot.getMeal(),
                                time,
                                canteen.getCapacity() - reserved
                        ));

                        time = time.plusMinutes(durationMinutes);
                    }
                }

                date = date.plusDays(1);
            }

            result.add(new CanteenStatusResponse(canteen.getId(), slotInfos));
        }

        return result;
    }

    public CanteenStatusResponse getStatusForCanteen(
            Long id,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            int durationMinutes
    ) {
        if(durationMinutes != 30 && durationMinutes != 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Canteen duration should be 30 or 60");
        }
        Canteen canteen = getCanteenById(id);

        List<SlotInfo> slotInfos = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            for (MealSlot mealSlot : canteen.getWorkingHours()) {

                LocalTime slotStart = mealSlot.getFrom().isAfter(startTime) ? mealSlot.getFrom() : startTime;
                LocalTime slotEnd = mealSlot.getTo().isBefore(endTime) ? mealSlot.getTo() : endTime;

                LocalTime time = slotStart;
                while (!time.plusMinutes(durationMinutes).isAfter(slotEnd)) {

                    int reserved = reservationRepository.countByCanteenAndDateAndTimeRange(
                            canteen, date, time, time.plusMinutes(durationMinutes)
                    );

                    slotInfos.add(new SlotInfo(
                            date,
                            mealSlot.getMeal(),
                            time,
                            canteen.getCapacity() - reserved
                    ));

                    time = time.plusMinutes(durationMinutes);
                }
            }

            date = date.plusDays(1);
        }

        return new CanteenStatusResponse(id, slotInfos);
    }
}