package sk.stuba.fei.uim.dp.attendanceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Card findBySerialNumber(String serialNumber);
}