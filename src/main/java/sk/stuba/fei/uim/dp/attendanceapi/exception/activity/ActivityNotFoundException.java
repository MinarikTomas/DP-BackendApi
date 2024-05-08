package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
