package sk.stuba.fei.uim.dp.attendanceapi.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    private final String MESSAGE = "User with this email already exists";
}
