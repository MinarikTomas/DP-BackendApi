package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardAlreadyExistsException extends RuntimeException{
    public CardAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
