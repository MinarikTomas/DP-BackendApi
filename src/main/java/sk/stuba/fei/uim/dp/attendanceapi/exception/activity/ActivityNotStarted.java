package sk.stuba.fei.uim.dp.attendanceapi.exception.activity;

import lombok.Getter;

public class ActivityNotStarted extends RuntimeException{
    public ActivityNotStarted(String errorMessage){
        super(errorMessage);
    }
}
