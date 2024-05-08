package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;
public class ActivityAlreadyEndedException extends RuntimeException{
    public ActivityAlreadyEndedException(String errorMessage){
        super(errorMessage);
    }
}
