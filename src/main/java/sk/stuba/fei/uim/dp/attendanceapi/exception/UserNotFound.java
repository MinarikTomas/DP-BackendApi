package sk.stuba.fei.uim.dp.attendanceapi.exception;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException{
    public UserNotFound(String errorMessage){
        super(errorMessage);
    }
}
