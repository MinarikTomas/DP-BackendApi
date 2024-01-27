package sk.stuba.fei.uim.dp.attendanceapi.exception.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
