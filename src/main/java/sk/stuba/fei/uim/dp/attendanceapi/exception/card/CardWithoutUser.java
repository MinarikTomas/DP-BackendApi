package sk.stuba.fei.uim.dp.attendanceapi.exception.card;

public class CardWithoutUser extends RuntimeException{
    public CardWithoutUser(String errorMessage){
        super(errorMessage);
    }
}
