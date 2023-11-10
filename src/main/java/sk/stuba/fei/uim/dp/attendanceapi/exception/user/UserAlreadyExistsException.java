package sk.stuba.fei.uim.dp.attendanceapi.exception.user;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
