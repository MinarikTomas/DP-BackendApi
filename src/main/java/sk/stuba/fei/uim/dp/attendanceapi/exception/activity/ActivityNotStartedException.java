package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

public class ActivityNotStartedException extends RuntimeException{
    public ActivityNotStartedException(String errorMessage){
        super(errorMessage);
    }
}
