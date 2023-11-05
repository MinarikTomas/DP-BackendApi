package sk.stuba.fei.uim.dp.attendanceapi.response;

import lombok.Data;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Card;

@Data
public class CardResponse {
    private Integer id;
    private String name;
    private String serialNumber;

    public CardResponse(Card card){
        this.id = card.getId();
        this.name = card.getName();
        this.serialNumber = card.getSerialNumber();
    }
}
