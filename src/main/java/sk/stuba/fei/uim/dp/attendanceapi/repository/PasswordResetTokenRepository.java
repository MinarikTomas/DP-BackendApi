package sk.stuba.fei.uim.dp.attendanceapi.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.stuba.fei.uim.dp.attendanceapi.entity.PasswordResetToken;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}