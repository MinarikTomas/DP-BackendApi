package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

public class ActivityNotFound extends RuntimeException{
    public ActivityNotFound(String errorMessage){
        super(errorMessage);
    }
}
