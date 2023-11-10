package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardAlreadyExists extends RuntimeException{
    public CardAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
