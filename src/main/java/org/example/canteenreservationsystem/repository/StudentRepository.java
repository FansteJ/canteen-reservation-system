package org.example.canteenreservationsystem.repository;

import org.example.canteenreservationsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
