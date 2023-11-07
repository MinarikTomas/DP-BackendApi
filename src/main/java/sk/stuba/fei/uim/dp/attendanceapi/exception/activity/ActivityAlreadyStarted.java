package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

import lombok.Getter;

public class ActivityAlreadyStarted extends RuntimeException{
    public ActivityAlreadyStarted(String errorMessage){
        super(errorMessage);
    }
}
