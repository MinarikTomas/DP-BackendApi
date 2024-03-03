package sk.stuba.fei.uim.dp.attendanceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Participant;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findByActivity_Id(Integer id);
}