package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardNotFound extends RuntimeException{
    public CardNotFound(String errorMessage){
        super(errorMessage);
    }
}
