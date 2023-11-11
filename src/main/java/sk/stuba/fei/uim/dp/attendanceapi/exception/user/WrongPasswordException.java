package sk.stuba.fei.uim.dp.attendanceapi.exception.user;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String errorMessage){
        super(errorMessage);
    }
}
