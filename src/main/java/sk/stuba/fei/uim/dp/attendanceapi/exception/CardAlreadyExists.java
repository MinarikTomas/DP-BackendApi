package sk.stuba.fei.uim.dp.attendanceapi.exception;

public class CardAlreadyExists extends RuntimeException{
    public CardAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
