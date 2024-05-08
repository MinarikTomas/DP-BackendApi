package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
