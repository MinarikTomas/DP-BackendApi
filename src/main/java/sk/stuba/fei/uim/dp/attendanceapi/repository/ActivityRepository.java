package sk.stuba.fei.uim.dp.attendanceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "SELECT a.* FROM activity a \n" +
            "JOIN participant p ON p.activity_id = a.id\n" +
            "JOIN card c ON p.card_id = c.id \n" +
            "WHERE c.user_id = :uid",
            nativeQuery = true)
    List<Activity> getAllUserAttendedActivities(@Param("uid")Integer uid);
}