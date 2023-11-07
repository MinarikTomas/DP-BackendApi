package sk.stuba.fei.uim.dp.attendanceapi.exception;

public class CardNotFound extends RuntimeException{
    public CardNotFound(String errorMessage){
        super(errorMessage);
    }
}
