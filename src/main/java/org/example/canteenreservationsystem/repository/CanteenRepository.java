package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CanteenRepository extends JpaRepository<Canteen, Long> {
    Optional<Canteen> findByName(String name);
}
