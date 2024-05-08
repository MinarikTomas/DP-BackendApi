package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardWithoutUserException extends RuntimeException{
    public CardWithoutUserException(String errorMessage){
        super(errorMessage);
    }
}
