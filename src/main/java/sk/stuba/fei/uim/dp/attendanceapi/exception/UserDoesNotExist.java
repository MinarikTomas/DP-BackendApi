package sk.stuba.fei.uim.dp.attendanceapi.exception;

import lombok.Getter;

@Getter
public class UserDoesNotExist extends RuntimeException{
    private final String MESSAGE = "User does not exist.";
}
