package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;
public class ActivityAlreadyEnded extends RuntimeException{
    public ActivityAlreadyEnded(String errorMessage){
        super(errorMessage);
    }
}
