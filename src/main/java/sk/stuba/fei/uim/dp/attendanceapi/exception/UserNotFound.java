package sk.stuba.fei.uim.dp.attendanceapi.exception;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException{
    private final String MESSAGE = "User not found.";
}
