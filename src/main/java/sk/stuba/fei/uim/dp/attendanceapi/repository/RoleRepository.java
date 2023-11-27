package sk.stuba.fei.uim.dp.attendanceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}