package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

public class ActivityAlreadyStartedException extends RuntimeException{
    public ActivityAlreadyStartedException(String errorMessage){
        super(errorMessage);
    }
}
