package org.example.canteenreservationsystem;

import org.example.canteenreservationsystem.entity.Canteen;
import org.example.canteenreservationsystem.entity.MealSlot;
import org.example.canteenreservationsystem.entity.Reservation;
import org.example.canteenreservationsystem.entity.Student;
import org.example.canteenreservationsystem.repository.CanteenRepository;
import org.example.canteenreservationsystem.repository.ReservationRepository;
import org.example.canteenreservationsystem.repository.StudentRepository;
import org.example.canteenreservationsystem.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ReservationServiceTest {

    private ReservationService reservationService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CanteenRepository canteenRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Student student;
    private Canteen canteen;

    @BeforeEach
    public void setup() {
        student = new Student();
        student.setFirstName("Test");
        student.setLastName("STudent");
        student.setEmail("student@test.com");
        student.setAdmin(false);
        student = studentRepository.save(student);

        canteen = new Canteen();
        canteen.setName("Test Canteen");
        canteen.setLocation("Test Location");
        canteen.setCapacity(10);
        List<MealSlot> mealSlots = new ArrayList<>();
        MealSlot mealSlot1 = new MealSlot();
        MealSlot mealSlot2 = new MealSlot();
        MealSlot mealSlot3 = new MealSlot();
        mealSlot1.setFrom(LocalTime.of(8, 0));
        mealSlot1.setTo(LocalTime.of(12, 0));
        mealSlot1.setMeal("breakfast");
        mealSlot2.setFrom(LocalTime.of(12, 0));
        mealSlot2.setTo(LocalTime.of(16, 0));
        mealSlot2.setMeal("lunch");
        mealSlot3.setFrom(LocalTime.of(16, 0));
        mealSlot3.setTo(LocalTime.of(18, 0));
        mealSlot3.setMeal("dinner");
        mealSlots.add(mealSlot1);
        mealSlots.add(mealSlot2);
        mealSlots.add(mealSlot3);
        canteen.setWorkingHours(mealSlots);
        canteen = canteenRepository.save(canteen);

        reservationService = new ReservationService(reservationRepository, studentRepository, canteenRepository);
    }

    @Test
    public void testCreateReservationSuccess() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 30;

        Reservation reservation = reservationService.createReservation(
                student.getId(),
                canteen.getId(),
                date,
                time,
                duration
        );

        assertNotNull(reservation.getId());
        assertEquals(student.getId(), reservation.getStudent().getId());
        assertEquals(canteen.getId(), reservation.getCanteen().getId());
        assertEquals(date, reservation.getDate());
        assertEquals(time, reservation.getTime());
        assertEquals(duration, reservation.getDuration());
    }

    @Test
    public void testCreateReservationFailsForPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 30;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.createReservation(student.getId(), canteen.getId(), pastDate, time, duration);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void testCreateReservationFailsForOverlapping() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 30;

        reservationService.createReservation(student.getId(), canteen.getId(), date, time, duration);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.createReservation(student.getId(), canteen.getId(), date, time, duration);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
