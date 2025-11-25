package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanteenRepository extends JpaRepository<Canteen, Long> {
}
