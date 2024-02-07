package sk.stuba.fei.uim.dp.attendanceapi.exception.user;

public class GoogleLoginException extends RuntimeException{
    public GoogleLoginException(String errorMessage){
        super(errorMessage);
    }
}
