package org.example.canteenreservationsystem.service;

import lombok.AllArgsConstructor;
import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.MealSlot;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.entity.Status;
import org.example.canteenreservationsystem.repository.CanteenRepository;
import org.example.canteenreservationsystem.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CanteenService {
    private final CanteenRepository canteenRepository;
    private final ReservationRepository reservationRepository;

    public Canteen createCanteen(String name, String location, Integer capacity, List<MealSlot> workingHours) {
        Canteen canteen = new Canteen();
        canteen.setName(name);
        canteen.setLocation(location);
        canteen.setCapacity(capacity);
        canteen.setWorkingHours(workingHours);
        return canteenRepository.save(canteen);
    }

    public List<Canteen> getAllCanteens() {
        return canteenRepository.findAll();
    }

    public Canteen getCanteenById(Long id) {
        return canteenRepository.findById(id).orElseThrow(() -> new RuntimeException("Canteen not found"));
    }

    public Canteen updateCanteen(Long id, Canteen updatedCanteen) {
        Canteen canteen = getCanteenById(id);
        if(updatedCanteen.getName() != null) {
            canteen.setName(updatedCanteen.getName());
        }
        if(updatedCanteen.getLocation() != null) {
            canteen.setLocation(updatedCanteen.getLocation());
        }
        if(updatedCanteen.getCapacity() > 0){
            canteen.setCapacity(updatedCanteen.getCapacity());
        }
        if(updatedCanteen.getWorkingHours() != null){
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
}